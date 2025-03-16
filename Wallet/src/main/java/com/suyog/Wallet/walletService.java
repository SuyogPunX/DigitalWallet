package com.suyog.Wallet;

import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;



public class walletService {
	Scanner sc = new Scanner(System.in);
	Hibernate hibernate =new Hibernate();
	PersonalUserService personaluserservice=new PersonalUserService();
	BusinessUserService businessuserservice=new BusinessUserService();
	BankService bankService=new BankService();

	public walletService() {}
	

    public void registerUser(){
    	//open hiberenate
    	Session session=hibernate.openSession();
    	Transaction transaction=hibernate.beginTransaction(session);
    	
    	System.out.println("Enter Email");
    	String email=sc.nextLine();
    	sc.next();
  
    	System.out.println("Enter Password");
    	String password=sc.nextLine();
    	
    	System.out.println("Are you a (1) PersonalUser or (2) BusinessUser ?");
    	int type=sc.nextInt();
    	sc.nextLine();
    	
    	User newUser = null;
    	
    	if(type==1) { //as PersonalUser
    		
    		System.out.println("Enter Name:");
    	    String name = sc.nextLine();
    	    System.out.println("Enter Phone:");
    	    String phone = sc.nextLine();
    		newUser=new PersonalUser(name,email,password,phone);
    	}
    	else if (type==2) {  // as BusinessUser
    		System.out.println("Enter Name:");
    		String name=sc.nextLine();
    		System.out.println("Enter Business name");
    		String businessName=sc.nextLine();
    		System.out.println("Enter Phone:");
     	    String phone = sc.nextLine();
    		newUser=new BusinessUser(name,email,password,phone,businessName);
    	}
    	
    	//initializing wallet to users
    	Wallet wallet=new Wallet(newUser);
    	//wallet.setUser(newUser);
    	newUser.setWallet(wallet);// Not necessary anymore, but for safety the constructor set wallet to user

        // Save the user and wallet
        hibernate.saveEntity(session, newUser);  // Save the newUser entity
        hibernate.saveEntity(session, wallet);   // Save the wallet entity

        hibernate.commitTransaction(transaction);  // Commit the transaction
        hibernate.closeSession(session);           // Close the session

    	System.out.println("Registration Sucessfull");
    	
    }

    public void loginUser(){
    	Session session=hibernate.openSession();

    	//fetch user based on email
    	System.out.println("Enter Email");
    	String email=sc.nextLine();
    	System.out.println("Enter Password");
     	String password=sc.nextLine();
    	
    	//fetching email by hql
    	String hql="From User where email= :email";
    	User user = (User) session.createQuery(hql)
    							.setParameter("email",email)
    							.uniqueResult();
    	
    	hibernate.closeSession(session);
    	
    	
    	//verifying user and logging in
    	if(user!=null && user.verifyPassword(password)) {
    		System.out.println("Login successfull !");
    		if(user instanceof PersonalUser) {
    			PersonalUserMenu((PersonalUser) user);
    		}
    		else {
    			BusinessUserMenu((BusinessUser) user);
    			}
    		}
    	
    	else {
    		System.out.println("Invalid credentials! Try again");
    	}
    	
    }
    
    
    //PersonalUser menu
    private void PersonalUserMenu(PersonalUser PersonalUser) {
    	while(true) {
    		System.out.println("\n--PersonalUser Menu---");
    		System.out.println("1. Link Bank Account");
    		System.out.println("2. Deposit Money from Bank");
    		System.out.println("3. View Wallet Balance");
    		System.out.println("4. Transfer Money");
    		System.out.println("5. Make Payment");
    		System.out.println("6. WithDrawl to Bank");
            System.out.println("7. View Transactions");
            System.out.println("8. View Bank Transactions");
            System.out.println("9. Pay for utilities.");
            System.out.println("10.View utility bills history");
            System.out.println("11.Set auto pay utilities.");
            System.out.println("12.Log Out	");
            System.out.print("Enter choice: ");
            
            int choice=sc.nextInt();
            

            switch (choice) {
	            case 1:
	                bankService.linkBankAccount(hibernate.getSessionFactory(), PersonalUser.getWallet());
	                break;
	            case 2:
	                bankService.depositToWallet(hibernate.getSessionFactory(), PersonalUser.getWallet());
	                break;
	            case 3:
	                personaluserservice.viewWalletBalance(PersonalUser);
	                break;
	            case 4:
	                personaluserservice.transferMoneyBetweenPersonalUsers(hibernate.getSessionFactory(), PersonalUser);
	                break;
	            case 5:
	                personaluserservice.makePayment(hibernate.getSessionFactory(), PersonalUser);
	                break;
	            case 6:
	            	bankService.withDrawlToBank(hibernate.getSessionFactory(),PersonalUser.getWallet());
	                break;
	            case 7:
	            	 personaluserservice.viewTransactionHistory(hibernate.getSessionFactory(), PersonalUser);
	            	 break;
	            case 8:
	            	 bankService.viewBankTransaction(hibernate.getSessionFactory(), PersonalUser);	 
	            	 break;
	            case 9:
	            	personaluserservice.payUtilityBill (PersonalUser,hibernate.getSessionFactory());
	            	break;
	            case 10:
	            	personaluserservice.viewUtilityBillHistory (PersonalUser,hibernate.getSessionFactory());
	            	break;
	            case 11:
	            	personaluserservice.setAutoPayForUtility(PersonalUser,hibernate.getSessionFactory());
	            	break;
	            case 12:
	            	return;
	            default:
	                System.out.println("Invalid choice! Try again.");
            }
        }
    	
    }
    
    //BusinessUser Menu
    private void BusinessUserMenu(BusinessUser BusinessUser) {
        while (true) {
            System.out.println("\n--- BusinessUser Menu ---");
            System.out.println("1. Link Bank Account");
            System.out.println("2. Deposit Money from Bank");
            System.out.println("3. View wallet Balance");
            System.out.println("4. View Payments");
            System.out.println("5. Withdraw to Bank");
            System.out.println("6. view Bank Transactions");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
            	case 1:
            		bankService.linkBankAccount(hibernate.getSessionFactory(), BusinessUser.getWallet());
            		break;
            	case 3:
            		businessuserservice.viewWalletBalance(BusinessUser);
            		break;
	            case 2:
	                bankService.depositToWallet(hibernate.getSessionFactory(), BusinessUser.getWallet());
	                break;
                case 4:
                    businessuserservice.viewPayments(hibernate.getSessionFactory(),BusinessUser);
                    break;
                case 5:
                    bankService.withDrawlToBank(hibernate.getSessionFactory(),BusinessUser.getWallet());
                    break;
                case 6:    
                    bankService.viewBankTransaction(hibernate.getSessionFactory(), BusinessUser);	
                    break;   
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
        
    }
    
    
    
    
    	
    	
   

}
