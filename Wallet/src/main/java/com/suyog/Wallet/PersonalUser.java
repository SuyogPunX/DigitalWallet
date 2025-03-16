package com.suyog.Wallet;

import java.util.List;
import java.util.Scanner;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;



@Entity
//@DiscriminatorValue("PersonalUser")
@PrimaryKeyJoinColumn(name="userID") //r is deletedlinks to user table
@OnDelete(action =OnDeleteAction.CASCADE) //Ensure PersonalUser is deleted when Use
public class PersonalUser extends User{
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String phone;
	
	public PersonalUser() {} //default constructor
	
	public PersonalUser(String name, String email,String password, String phone) {
		super.setEmail(email);
		super.setPassword(password);
		super.setUserType("PersonalUser");
		this.name=name;
		this.phone=phone;
		
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	
}
