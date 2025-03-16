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

@Entity
public class BankTransection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transectionId;

    @ManyToOne
    @JoinColumn(name = "walletId", nullable = false)
    private Wallet wallet;
    
    @Column(nullable = true)
    private String bankName;

    @Column(nullable = true)
    private String accountNo;
    

    @Column(nullable = false)
    private double amount;
    
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)  //Stores Enum as String in DB deposit or withdrawl
	private TransectionType transectionType;

    @Column(nullable = false)
    private LocalDateTime timestamp;
  
    public BankTransection() {} 

    // Constructor
    public BankTransection(Wallet wallet, double amount, TransectionType transectionType, String bankName, String accountNo) {
        this.wallet = wallet;
        this.amount = amount;
        this.accountNo=accountNo;
        this.bankName=bankName;
        this.timestamp = LocalDateTime.now();
        this.transectionType=transectionType;
    }

    public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public TransectionType getTransectionType() {
		return transectionType;
	}

	public void setTransectionType(TransectionType transectionType) {
		this.transectionType = transectionType;
	}

	// Getters and Setters
  
    public Wallet getWallet() {
        return wallet;
    }

    public int getTransectionId() {
		return transectionId;
	}

	public void setTransectionId(int transectionId) {
		this.transectionId = transectionId;
	}

	public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
