package com.suyog.Wallet;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BusinessUserService {


	public  void viewPayments(SessionFactory sf,BusinessUser businessUser) {
		Session session=sf.openSession();
//		 Since sender and recipientPersonalUser are not primary keys, they are just references (foreign keys)
//		 to PersonalUser entities in the Transection table. we need to check both so to get transection where PersonalUser sent and recieve money
//		this ensures both sent and revieved transection are fetched
		
		try {
			String hql="From Transection WHERE receiverWalletId= :BusinessUserWallet  ORDER BY timestamp DESC";
			List<Transection> transections=session.createQuery(hql,Transection.class)
											 .setParameter("BusinessUserWallet",businessUser.getWallet())
											 .getResultList();
			if(transections.isEmpty()){
				System.out.println("No transections found");
				return;
			}
			
			System.out.println("\nTransaction History:");
			for(Transection transection: transections){
				Wallet senderWallet=transection.getSenderwallet();
				PersonalUser sender=(PersonalUser) senderWallet.getUser();
				System.out.println(transection.getTimestamp()+" | Received Rs "+transection.getAmount()+ " From: "+sender.getName());	
					
			}
			
		}catch(Exception e) {
		    System.out.println("Error retrieving transaction history: " + e.getMessage());
		}
		finally {
			session.close();
		}

	}
	
	public  void viewWalletBalance(BusinessUser businessUser) {
		Wallet wallet=businessUser.getWallet();
		System.out.println("Current balance : Rs "+wallet.getBalance());
}



}
