package com.suyog.Wallet;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
 
@Entity
public class UtilityPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    
    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;  // Link payments to a wallet
    
    @ManyToOne
    @JoinColumn(name = "utility_id", nullable = false)
    private Utility utility;
   
    
    @Column(name = "paid_amount", nullable = false)
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;
    
    
    
    public UtilityPayment() {}
    
    public UtilityPayment(Wallet wallet, Utility utility, double amount, Date date) {
    	   this.utility=utility;
           this.wallet=wallet;
           this.amount=amount;
           this.paymentDate = date;
    }
    


    // Getters and Setter
	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Utility getUtility() {
		return utility;
	}

	public void setUtility(Utility utility) {
		this.utility = utility;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}


    @Override
    public String toString() {
        return "UtilityPayment{" +
                "paymentId=" + paymentId +
                ", wallet=" + wallet.getWalletId() +
                ", utility=" + utility.getUtilityType() +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate + '}';
    }
}

