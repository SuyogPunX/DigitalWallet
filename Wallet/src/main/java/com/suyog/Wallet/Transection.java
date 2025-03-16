package com.suyog.Wallet;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "Transaction")
public class Transection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
	
	@ManyToOne
	@JoinColumn(name="senderWalletId",nullable=false)
    private Wallet senderWallet;
	
    @ManyToOne
    @JoinColumn(name = "receiverWalletId", nullable = false)
    private Wallet receiverWallet;
    
	@Column(nullable=false)
    private double amount;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)  //Stores Enum as String in DB
	private TransectionType transectionType;
	
	@Column(nullable=false)
    private LocalDateTime timestamp;
	
	

	public Wallet getSenderwallet() {
		return senderWallet;
	}


	public void setSenderwallet(Wallet senderWallet) {
		this.senderWallet = senderWallet;
	}


	public Wallet getReceiverWallet() {
		return receiverWallet;
	}


	public void setReceiverWallet(Wallet receiverWallet) {
		this.receiverWallet = receiverWallet;
	}


	public Transection() {}
	
	
	// Constructor for PersonalUser-to-PersonalUser  and personal-business transactions
    public Transection(Wallet sender, Wallet recipient, double amount, TransectionType transactionType) {
        this.senderWallet = sender;
        this.receiverWallet=recipient;
        this.amount = amount;
        this.transectionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }
    

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}



	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

	public TransectionType getTransectionType() {
		return transectionType;
	}


	public void setTransectionType(TransectionType transectionType) {
		this.transectionType = transectionType;
	}



	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	

    
}
