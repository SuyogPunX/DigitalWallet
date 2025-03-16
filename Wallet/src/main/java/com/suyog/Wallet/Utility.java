package com.suyog.Wallet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Utility")
public class Utility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utility_id")
    private int utilityId;

    @Column(name = "utility_type", nullable = false)
    private String utilityType;  // "Electricity", "Water", "Wi-Fi"

    @Column(name = "user_id", unique = true)
    private String userId;  // For electricity and water
    
    @Column(name = "Amount", nullable=false)
    private double amount;

    @Column(name = "service_provider", nullable = false)
    private String serviceProvider; 
    
    @Column(name = "due_date",nullable=true)
    private Integer dueDate;  // The due date for the utility payment
    @Column(name = "auto_pay_enabled",nullable=true)
    private Boolean autoPayEnabled;
    
    @Column(name = "auto_pay_amount",nullable=true)
    private Double autoPayAmount;
    
    @ManyToOne
    @JoinColumn(name = "payment_wallet_id",nullable=true)
    private Wallet paymentWallet; // The wallet used for auto-pay
    
    @OneToMany(mappedBy="utility",cascade=CascadeType.ALL)
    private List<UtilityPayment> payments=new ArrayList<>();


    // Constructors
    public Utility() {}

    public Utility(String utilityType, String userId,String serviceProvider,double amount, boolean autoPayEnabled, Date dueDate, double autoPayAmount) {
        this.utilityType = utilityType;
        this.userId = userId;
        this.amount=amount;
        this.serviceProvider = serviceProvider;
        this.autoPayAmount = autoPayAmount;
        this.autoPayEnabled = autoPayEnabled;
        
    }

    // Getters and Setters

	public int getUtilityId() {
		return utilityId;
	}

	public void setUtilityId(int utilityId) {
		this.utilityId = utilityId;
	}

	public String getUtilityType() {
		return utilityType;
	}

	public void setUtilityType(String utilityType) {
		this.utilityType = utilityType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public List<UtilityPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<UtilityPayment> payments) {
		this.payments = payments;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Integer getDueDate() {
		return dueDate;
	}

	public void setDueDate(Integer i) {
		this.dueDate = i;
	}

	public Boolean isAutoPayEnabled() {
		return autoPayEnabled;
	}

	public void setAutoPayEnabled(Boolean autoPayEnabled) {
		this.autoPayEnabled = autoPayEnabled;
	}

	public Double getAutoPayAmount() {
		return autoPayAmount;
	}

	public void setAutoPayAmount(Double autoPayAmount) {
		this.autoPayAmount = autoPayAmount;
	}

	public Wallet getPaymentWallet() {
		return paymentWallet;
	}

	public void setPaymentWallet(Wallet paymentWallet) {
		this.paymentWallet = paymentWallet;
	}
	
	
	
	

}
