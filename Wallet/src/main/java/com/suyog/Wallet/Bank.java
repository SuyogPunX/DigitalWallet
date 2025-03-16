package com.suyog.Wallet;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;



@Entity
public class Bank {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int bankId;
	
	@Column(nullable=false)
	private String bankName;
	
	@Column(nullable=false,unique=true)
	private String accountNo;
	
	@Column(nullable = false)
    private double mainBalance=0.0;
	
	@OneToOne(mappedBy="bankAccount",cascade=CascadeType.ALL)// wallets linked to one bank
	private Wallet wallet;
	
	public Bank() {

	}
	
	public Bank(String bankName, String accountNumber) {
		this.accountNo=accountNumber;
		this.bankName=bankName;;
	}


	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
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

	public double getMainBalance() {
		return mainBalance;
	}

	public void setMainBalance(double mainBalance) {
		this.mainBalance = mainBalance;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	
	
	
}
