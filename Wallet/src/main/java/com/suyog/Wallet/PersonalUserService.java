package com.suyog.Wallet;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.mysql.cj.Query;

import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;

public class PersonalUserService {
	
	PersonalUserService(){}
	Scanner sc=new Scanner(System.in);

	public void transferMoneyBetweenPersonalUsers(SessionFactory sf,PersonalUser personaluser) {
		Session session=sf.openSession();
		Transaction tx =session.beginTransaction();
		
		try {
			System.out.println("Enter recipient's phone number:");
			String recipientPhone=sc.nextLine();
			System.out.println("Enter amount to transfer:");
			double amount =sc.nextDouble();
			sc.nextLine();
			
			if(amount <=0) {
				System.out.println("Invalid amount. Please enter a valid amount.");
				return;
			}
			
			//fetching recipient by phone number
			PersonalUser recipient=(PersonalUser) session.createQuery("From PersonalUser WHERE phone=:phone",PersonalUser.class)
															.setParameter("phone",recipientPhone)
															.uniqueResult();
			
			if(recipient==null) {
				System.out.println("Recipient phone no not found");
				return;
			}
			
			Wallet senderwallet=personaluser.getWallet();
			Wallet recipientwallet=recipient.getWallet();
			
			if(amount>senderwallet.getBalance()) {
				System.out.println("Insufficient balance !");
				return;
			}
			
			senderwallet.setBalance(senderwallet.getBalance()-amount);
			recipientwallet.setBalance(recipientwallet.getBalance()+amount);
			
			//create transections
			Transection transection=new Transection(senderwallet,recipientwallet,amount,TransectionType.TRANSFER);
			session.save(transection);
			session.update(senderwallet);
			session.update(recipientwallet);
			tx.commit();
			
			System.out.println("Amount transfered Sucessfully ! New Balance:"+senderwallet.getBalance());
		}catch(Exception e){
			 tx.rollback();
		     System.out.println("Transaction failed: " + e.getMessage());
		}
		finally {
			session.close();
		}
		
	}
	
	public void makePayment(SessionFactory sf,PersonalUser personaluser){
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
		
		try {
				//fetching and displaying  BusinessUsers
				List<BusinessUser> BusinessUsers=session.createQuery("FROM BusinessUser",BusinessUser.class).list();
				if(BusinessUsers.isEmpty()) {
					System.out.println("No BusinessUsers available !");
					session.close();
					return;
				}
				
				System.out.println("\n--- Available BusinessUsers ---");
			    for (BusinessUser BusinessUser : BusinessUsers) {
			        System.out.println("Business Name: " + BusinessUser.getBusinessName() + " | Owner: " + BusinessUser.getName());
			    }
			    
			    //Ask user to select business to pay
				System.out.println("Enter Business name:");
				String businessname=sc.nextLine();
				
				//fetching BusinessUser
				BusinessUser BusinessUser=(BusinessUser) session.createQuery("From BusinessUser WHERE businessName= :businessName")
																.setParameter("businessName", businessname)
																.uniqueResult();
				if(BusinessUser==null) {
					 System.out.println("Error: BusinessUser not found!");
				     session.close();
				     return;
				}
				
				//fetching BusinessUser wallet
				Wallet bwallet =BusinessUser.getWallet();
				if(bwallet==null) {
					System.out.println("BusinessUser Wallet not found");
					return;
				}	
				
				//fetching PersonalUser wallet
				Wallet pwallet=personaluser.getWallet();
				if(pwallet==null) {
					System.out.println("Error: Your wallet not found !");
					return;
				}
				
				//payment process
				System.out.println("Enter amount to pay:");
				double amount =sc.nextDouble();
				
				//deduct from PersonalUser
				if(amount >0 && pwallet.getBalance()>=amount) {
					pwallet.setBalance(pwallet.getBalance()-amount);
					session.update(pwallet);
				}
				
				//credit to BusinessUser
				bwallet.setBalance(bwallet.getBalance()+amount);
				System.out.println("Payement Sucessfull ! New Balance: Rs"+pwallet.getBalance());
				
				// create transection
				Transection transection=new Transection(pwallet,bwallet,amount,TransectionType.PAYMENT);
				session.save(transection);
				session.update(pwallet);
				session.update(bwallet);
				tx.commit();
			}catch(Exception e) {
				 tx.rollback();
			     System.out.println("Payment failed: " + e.getMessage());
		}finally {
			session.close();
		}
		
    }
	


	public void viewTransactionHistory(SessionFactory sf,PersonalUser personaluser) {
		Session session=sf.openSession();
//		 Since sender and recipientPersonalUser are not primary keys, they are just references (foreign keys)
//		 to PersonalUser entities in the Transection table. we need to check both so to get transection where PersonalUser sent and recieve money
//		this ensures both sent and revieved transection are fetched
		
		try {
			String hql="From Transection WHERE senderWalletId= :PersonalUser OR receiverWalletId= :PersonalUser ORDER BY timestamp DESC";
			List<Transection> transections=session.createQuery(hql,Transection.class)
											 .setParameter("PersonalUser",personaluser)
											 .getResultList();
			if(transections.isEmpty()){
				System.out.println("No transections found");
				return;
			}
			
			System.out.println("\nTransaction History:");
			for(Transection transection: transections){
				Wallet senderWallet=transection.getSenderwallet();
				Wallet receiverWallet=transection.getReceiverWallet();
				

			    // Fetch the sender and receiver users from their wallets
			    PersonalUser sender = (PersonalUser) senderWallet.getUser() ;
			    User receiver = receiverWallet.getUser();
				
				if(sender.getUserID()==personaluser.getUserID()) {
					//current user is sender
					if(receiver instanceof PersonalUser) {
						PersonalUser PersonalReceiver=(PersonalUser) receiver;
						System.out.println(transection.getTimestamp()+ " | Sent Rs "+ transection.getAmount()+"  To: "+PersonalReceiver.getName());
						}
						else if(receiver instanceof BusinessUser){
							BusinessUser BusinessReceiver=(BusinessUser) receiver;
							System.out.println(transection.getTimestamp()+ "| Paid Rs "+ transection.getAmount()+"  To BusinessName:"+BusinessReceiver.getBusinessName());	
						}
					}
				else { 
					//Current user is receiver
					// Current user is the receiver (Only PersonalUser can be a sender)
					 //always sender personaluser
						PersonalUser PersonalSender=(PersonalUser) sender;
						System.out.println(transection.getTimestamp()+" | Received Rs "+transection.getAmount()+ " From: "+PersonalSender.getName());				
				}
			}
		
		}catch(Exception e) {
		    System.out.println("Error retrieving transaction history: " + e.getMessage());
		}
		finally {
			session.close();
		}
	}

	public void viewWalletBalance(PersonalUser personalUser) {
			Wallet wallet=personalUser.getWallet();
			System.out.println("Current balance : Rs "+wallet.getBalance());
	}
	
	public void payUtilityBill(PersonalUser personalUser, SessionFactory sf) {
        System.out.println("\nSelect Utility to Pay:");
        System.out.println("1. Electricity");
        System.out.println("2. Water");
        System.out.println("3. Wi-Fi");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        String utilityType = "";
        String userId = "";
        double amount=0;// Fixed amount based on utility type
        switch(choice) {
            case 1: 
                utilityType = "Electricity";
                System.out.print("Enter Account ID: ");
                userId = sc.next(); // Account ID for electricity
                amount=500;
                break;
            case 2: 
                utilityType = "Water";
                System.out.print("Enter Account ID: ");
                userId= sc.next(); // Account ID for water
                amount=500;
                break;
            case 3: 
                utilityType = "WiFi";
                System.out.print("Enter UserId: ");
                userId= sc.next(); // UserId for Wi-Fi
                amount=1200;
            default: 
                System.out.println("Invalid choice.");
                return;
        }

        sc.nextLine();  // Consume newline character
        System.out.print("Enter Service Provider : ");
        String serviceProvider = sc.nextLine();
        
        System.out.println("Amount to be paid: " + amount); // Display the fixed amount
        System.out.println("Processing payment for " + utilityType + "...");

        
        // Fetch utility details from the database
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        String hql = "FROM Utility WHERE utilityType = :utilityType AND userId = :userId AND serviceProvider = :serviceProvider";
        Utility utility = (Utility) session.createQuery(hql, Utility.class)
					        .setParameter("utilityType", utilityType.toLowerCase())
					        .setParameter("userId", userId.toLowerCase())
					        .setParameter("serviceProvider", serviceProvider.toLowerCase())
					        .uniqueResult();

        if (utility == null) {
            System.out.println("Error: Utility account not found.");
            tx.rollback();
            session.close();
            return;
        }
        
        // Check balance and process payment
        Wallet userWallet = personalUser.getWallet();
        if (userWallet.getBalance() < amount) {
            System.out.println("Insufficient balance to make the payment.");
            return;
        }

        // Deduct balance
        userWallet.setBalance(userWallet.getBalance() - amount);
      
        utility.setAmount(utility.getAmount()+amount);
        

        // Create UtilityPayment and save to database
        UtilityPayment utilityPayment = new UtilityPayment(userWallet,utility,amount,new Date());
        session.save(utilityPayment);  // Save payment transaction
        session.update(userWallet);    // Update wallet balance
        tx.commit();
        session.close();
        System.out.println("Payment of Rs " + amount + " made successfully for " + utilityType);
    }
	
	
	
    public void viewUtilityBillHistory(PersonalUser personalUser, SessionFactory sf) {
        Session session = sf.openSession();
        Wallet wallet=personalUser.getWallet();
        String hql = "FROM UtilityPayment WHERE wallet.walletId = :walletId ORDER BY paymentDate DESC";
        List<UtilityPayment> utilityPayments = session.createQuery(hql, UtilityPayment.class)
        								.setParameter("walletId", wallet.getWalletId())
        								.getResultList();
        
        for(UtilityPayment p:utilityPayments) {
        	System.out.println("Payment of Rs "+p.getAmount()+" made successfully for "+p.getUtility().getUtilityType()+","+" UserID:"+p.getUtility().getUserId());
      
        }
        
        session.close();
    }
    
    // Method to set auto-pay for utility bills
    public void setAutoPayForUtility(PersonalUser personalUser, SessionFactory sf) {
        // Ask the user if they want to enable auto-pay
        System.out.println("\nSelect Utility to Set Auto-Pay:");
        System.out.println("1. Electricity");
        System.out.println("2. Water");
        System.out.println("3. Wi-Fi");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        String utilityType = "";
        String userId = "";
        double amount = 0.0;
        switch(choice) {
            case 1: 
                utilityType = "Electricity";
                System.out.print("Enter user ID: ");
                userId = sc.next(); // Account ID for electricity
                System.out.println("Amount: Rs 500");
                amount = 500.00; // Fixed amount for electricity
                break;
            case 2: 
                utilityType = "Water";
                amount = 500.00; // Fixed amount for water
                System.out.print("Enter user ID: ");
                userId = sc.next(); // Account ID for water
                System.out.println("Amount: Rs 500");

                break;
            case 3: 
                utilityType = "WiFi";
                amount = 1200.00; // Fixed amount for Wi-Fi
                System.out.print("Enter UserId: ");
                userId = sc.next(); // UserId for Wi-Fi
                System.out.println("Amount: Rs 1200");
                break;
            default: 
                System.out.println("Invalid choice.");
                return;
        }
        
        
        sc.nextLine();  // Consume newline character
        System.out.println("Enter Service Provider: ");
        String serviceProvider = sc.nextLine();
        
       
        
        //fetching details from the database
        Session session=sf.openSession();
        Transaction tx=session.beginTransaction();
        
        try {
        String hql="From Utility where utilityType= :utilityType AND userId= :userId AND serviceProvider= :serviceProvider";
        Utility utility=(Utility) session.createQuery(hql,Utility.class)
        		  .setParameter("utilityType", utilityType.toLowerCase())
                  .setParameter("userId", userId.toLowerCase())
                  .setParameter("serviceProvider", serviceProvider.toLowerCase())
                  .uniqueResult();
        
        if(utility ==null) {
        	System.out.println("Error: Utility account is not found.");
        	tx.rollback();
        	session.close();
        	return;
        }
        
       
        
        // Step 1: Set the due date to the last day of the current month manually
 //       setDueDateToLastDayOfMonth(utility, sf);  // Set due date manually first
        
        // Step 2: Ask the user if they want to enable auto-pay
        System.out.print("Enable auto-pay for this utility? (yes/no): ");
        String autoPayChoice = sc.nextLine();
        
        //set payment wallet for autopay
        Wallet paymentWallet=personalUser.getWallet();
    
        if(autoPayChoice.equalsIgnoreCase("Yes")) {
        	utility.setAutoPayAmount(amount);
        	utility.setAutoPayEnabled(true);
        	utility.setPaymentWallet(paymentWallet);
        	
        	
            // Get the last day of the current month
            LocalDate today = LocalDate.now();
            YearMonth yearMonth = YearMonth.from(today);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
            
            utility.setDueDate(lastDayOfMonth.getDayOfMonth());
        	
        	     	
        }
        else {
        	System.out.println("Auto-pay not enabled.");
        }
        tx.commit();
        
      }
      catch(Exception e){
    	  tx.rollback();
    	  e.printStackTrace();
      }
       finally {
    	   session.close();
       }
    //    autoPayUtilityBills(personalUser,sf);
        
    }
    
//    public void autoPayUtilityBills(PersonalUser personalUser, SessionFactory sf) {
//    	  Session session = sf.openSession();
//    	  Transaction tx = session.beginTransaction();
//    	  
//    	  try {
//    		// Fetch all utilities with autoPayEnabled set to 
//    	        String hql = "FROM Utility WHERE userId = :userId AND autoPayEnabled = :autoPayEnabled";
//    		  List<Utility> utilities = session.createQuery(hql, Utility.class)
//                       .setParameter("userId", personalUser.getUserID())
//                       .setParameter("autoPayEnabled", true)
//                       .list();
//    		  
//    	      if (utilities.isEmpty()) {
//    	            System.out.println("No utilities with auto-pay enabled found.");
//    	            return;
//    	        }  
//    	      
//    	      Wallet userWallet=personalUser.getWallet();
//    	      
//    	      for (Utility utility : utilities) {
//    	            double amountDue = utility.getAmount();
//    	            
//    	       // Check balance before auto-payment
//    	       if (userWallet.getBalance() < amountDue) {
//    	             System.out.println("Insufficient balance for auto-payment of " + utility.getUtilityType());
//    	              continue;
//    	       	}
//    	       
//    	       //reminder
//    	       //get today date
//    	       Calendar currentCalendar=Calendar.getInstance();
//    	       Date today=currentCalendar.getTime();
//    	       
//    	       
//    	       //calculate the date # day from today
//    	       Calendar futureCalendar=Calendar.getInstance();
//    	       futureCalendar.add(Calendar.DAY_OF_YEAR, 3); //add 3 days to today
//    	       Date threeDaysFromNow=futureCalendar.getTime();
////    	       
////    	        Check if the due date is between today and 3 days from now
////    	       1st condition
////    	       What it checks: Is the due date today or later?
////    	    	We check this to ensure we don’t send reminders for past due dates.
////    	    	Why it’s needed: If the due date is already in the past, it’s not relevant for a 
////    	    	reminder for upcoming payments. We only care about future payments.
//    	       
////    	       What it checks: Is the due date within the next 3 days from today?
////    	    	We check this to ensure that the due date is not too far in the future — meaning, 
////    	    	we are only reminding the user about bills that are about to become due within the next 3 days.
////    	    	Why it’s needed: We don’t want to send reminders for bills that are due next week, next month, etc. 
////    	       Only bills due within 3 days should trigger the reminder
//   	       
//    	       if (!utility.getDueDate().before(today) && !utility.getDueDate().after(threeDaysFromNow)) {
//    	           System.out.println("Reminder: Payment for " + utility.getUtilityType() + " is due soon.");
//    	       }
//    	       
//    	       //only process the payment in duedate
//    	       if(utility.getDueDate().before(new Date())) { //This checks whether the utility's due date has already passed. cause If the due date is earlier than the current date, the payment is no longer valid for processing (it has passed).
//
//    	    	   //check if the due date is today date 
//    	    	   if(utility.getDueDate().equals(new Date())) {
//    	    		   //deduct balace
//    	    		   userWallet.setBalance(userWallet.getBalance()-amountDue);
//    	    		   
//    	    		    // Create UtilityPayment and save to database
//    	                UtilityPayment utilityPayment = new UtilityPayment(userWallet, utility, amountDue, new Date());
//    	                session.save(utilityPayment);  // Save payment transaction
//    	                session.update(userWallet);    // Update wallet balance
//    	                
//
//    	                // Reset utility amount (assuming utility amount is cleared after payment)
//    	                utility.setAmount(0);  // Set utility amount to zero after payment
//    	                session.update(utility);
//
//    	                System.out.println("Auto-payment of " + utility.getUtilityType() + " processed successfully.");
//    	    		   
//    	    	   	}
//    	    	   
//	    	       }
//	    	 
//	    	      
//	    	     }
//	    	     		  
//	    	  }catch(Exception e){
//	    		  System.out.println(e);
//	    		  
//	    	  }
//	    	  
//	    	  finally {
//	    		  session.close();
//	    	  }
//	    	  
//    }
//    
//    // Method to set due date to the last day of the current month
//    public void setDueDateToLastDayOfMonth(Utility utility, SessionFactory sf) {
//        // Get the current date
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        
//        // Set the date to the last day of the current month
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        
//        // The calculated last day of the month
//        Date lastDayOfMonth = calendar.getTime();
//        
//        // Set the calculated last day as the due date
//        utility.setDueDate(new java.sql.Date(lastDayOfMonth.getTime())); // Using java.sql.Date for DB compatibility
//         
//        // Save the updated utility details in the database
//        Session session = sf.openSession();
//        Transaction tx = session.beginTransaction();
//        session.update(utility); // Update the utility object in the database
//        tx.commit();
//        session.close();
//
//        System.out.println("Due date set to the last day of the month: " + lastDayOfMonth);
//    }
//

    	
}
   
  
