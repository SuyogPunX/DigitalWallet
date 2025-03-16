package com.suyog.Wallet;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Wallet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int walletId;
	
	@OneToOne
	@JoinColumn(name="userID",nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)  // Deletes Wallet when User is deleted
	private User user;
	
	@Column(nullable = false)
    private double balance=0.0;
	
	@OneToOne  // many wallets can be linked to one bank
	@JoinColumn(name="bankId",nullable=true) //bank is optionasl
	private Bank bankAccount;
	
	@OneToMany(mappedBy="wallet",cascade=CascadeType.ALL)
	private List<UtilityPayment> payments=new ArrayList<>();
	
	public Wallet() {}
	
    //  New Constructor to Ensure Wallet is Linked to User
    public Wallet(User user) {
        this.user = user;
        user.setWallet(this);  // Ensure bidirectional relationship
    }
    
    // Constructor to link a Wallet to a Bank
    public Wallet(User user, Bank bankAccount) {
        this.user = user;
        this.bankAccount = bankAccount;
      //  user.setWallet(this); // Ensures the relationship between user and wallet
    }


	

	public int getWalletId() {
		return walletId;
	}

	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}



	public Bank getBankAccount() {
		return bankAccount;
	}



	public void setBankAccount(Bank bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	
	

  
}
