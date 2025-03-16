package com.suyog.Wallet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
//@DiscriminatorValue("BusinessUser")
@PrimaryKeyJoinColumn(name="userID") //links to user table
@OnDelete(action = OnDeleteAction.CASCADE) // Ensure BusinessUser is deleted when User is deleted
public class BusinessUser extends User {
	@Column(nullable=false)
	private String Name;
	
	@Column(nullable=false,unique=true)
	private String businessName;
	
	@Column(nullable=false)
	private String Phone;
	

	
	
	public BusinessUser() {}
	
	public BusinessUser(String name, String email, String password,String phone,String businessName) {
		super.setEmail(email);
		super.setPassword(password);
		super.setUserType("BusinessUser");
		this.businessName =businessName;
		this.Name=name;
		this.Phone=phone;
		
	}
	
	
	
    

    public String getBusinessName() {
		return businessName;
	}





	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}





	public void receivePayment(){

    }
    public void withdrawToBank(){

    }





	public void viewPayments() {
		
		
	}





	public void withdrawFunds() {
		
		
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}
	
	
}
