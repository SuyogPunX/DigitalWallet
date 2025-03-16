package com.suyog.Wallet;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class BankService {
	Scanner sc=new Scanner(System.in);
	
	public void linkBankAccount(SessionFactory sf, Wallet wallet) {
		Session session =sf.openSession();
		//check if wallet is already linked
		if(wallet.getBankAccount()!=null) {
			System.out.println("A bank is already linked to this wallet !");
			
		}
		else {
			System.out.println("Enter Bank Name: ");
			String bankName=sc.nextLine();
			System.out.println("Enter Bank Account Number: ");
			String accountNumber=sc.nextLine();
			
            // Try to fetch the bank from the database based on the provided details
            String hql = "FROM Bank WHERE bankName = :bankName AND accountNo = :accountNo";
            Bank bankAccount = (Bank) session.createQuery(hql)
                                              .setParameter("bankName", bankName)
                                              .setParameter("accountNo", accountNumber)
                                              .uniqueResult();
            if (bankAccount == null) {
                System.out.println("Bank not found in the system. Please try again.");
                return;
            }
            
            //link the existing bank to the wallet
            wallet.setBankAccount(bankAccount);   
//            Bank bank=new Bank(bankName,accountNumber);
			Transaction tx=session.beginTransaction();
			session.update(bankAccount); //update the wallet with the bank linked
		//	session.update(bank);//update bank name and account
			tx.commit();
			session.close();
			System.out.println("Bank account linked sucessfully !");
		}
		
	}
	
	public void depositToWallet(SessionFactory sf,Wallet wallet) {
		 Session session = sf.openSession();
	     Transaction tx = session.beginTransaction();
	     
	     //chcek if bank is linked to wallet
	     Bank bank=wallet.getBankAccount();
	     
		if(wallet.getBankAccount()==null) {
			System.out.println("No bank is linked to this wallet. Please link your bank first!");
			return;
		}

	
		System.out.println("Enter amount to deposit:");
		double amount = sc.nextDouble();
		     
		if(amount<=0) {
			System.out.println("Invalid amount. Please enter a vlaid amount.");
			return;
		 }
		
		//checking if bank has sufficient amount
		if(bank.getMainBalance()<amount) {
			System.out.println("Insufficient funds in the bank account.");
			session.close();
			return; 
		}
		     
		 //update wallet and bank balance
		wallet.setBalance(wallet.getBalance()+amount);
		bank.setMainBalance(bank.getMainBalance()-amount);
		session.update(wallet);
		session.update(bank);
		
        // Record the deposit transaction
        BankTransection deposit = new BankTransection(wallet, amount, TransectionType.DEPOSIT, bank.getBankName(), bank.getAccountNo());
        session.save(deposit);  // Save deposit transaction
        
		tx.commit();
		session.close();
		System.out.println("Deposit successful! New Wallet Balance: " + wallet.getBalance());
		
	}
	
	public void withDrawlToBank(SessionFactory sf,Wallet wallet) {
		 Session session = sf.openSession();
	     Transaction tx = session.beginTransaction();
	     
	     //chcek if bank is linked to wallet
	     Bank bank=wallet.getBankAccount();
	     
		if(wallet.getBankAccount()==null) {
			System.out.println("No bank is linked to this wallet. Please link your bank first!");
			return;
		}

	
		System.out.println("Enter amount to withdrawl:");
		double amount = sc.nextDouble();
		     
		if(amount<=0) {
			System.out.println("Invalid amount. Please enter a vlaid amount.");
			return;
		 }
		
		//checking if bank has sufficient amount
		if(wallet.getBalance()<amount) {
			System.out.println("Insufficient  balance for withdrawl !.");
			session.close();
			return; 
		}
		     
		 //update wallet and bank balance
		wallet.setBalance(wallet.getBalance()-amount);
		bank.setMainBalance(bank.getMainBalance()+amount);
		session.update(wallet);
		session.update(bank);
		
       // Record the withdrwal transaction
       BankTransection withDrawl = new BankTransection(wallet, amount, TransectionType.WITHDRAWAL, bank.getBankName(), bank.getAccountNo());
       session.save(withDrawl);  // Save withdrawl transaction
       
		tx.commit();
		session.close();
		System.out.println("WithDrawl  successful! New Wallet Balance: " + wallet.getBalance());
		
	}
	
	public void viewBankTransaction(SessionFactory sf,User user) {
		Session session=sf.openSession();
		
		try {
			Wallet wallet=user.getWallet();
			Bank bank=wallet.getBankAccount();
			String hql="From BankTransection WHERE wallet.walletId= :walletId ORDER BY timestamp DESC";
			List<BankTransection> transections=session.createQuery(hql,BankTransection.class)
											 .setParameter("walletId",wallet.getWalletId())
											 .getResultList();
			if(transections.isEmpty()){
				System.out.println("No transections found");
				return;
			}
			
			System.out.println("\nTransaction History:");
			for(BankTransection transection: transections){
				if(transection.getTransectionType()==TransectionType.DEPOSIT) {
				System.out.println(transection.getTimestamp()+" | Deposit Rs "+transection.getAmount()+ " From : "+bank.getBankName());	
				}
				else if(transection.getTransectionType()==TransectionType.WITHDRAWAL) {
					System.out.println(transection.getTimestamp()+" | WithDrawl Rs "+transection.getAmount()+ " To : "+bank.getBankName());	
				}
				
	
			}
		
		}catch(Exception e) {
		    System.out.println("Error retrieving transaction history: " + e.getMessage());
		}
		finally {
			session.close();
		}
	}


	
	
}
