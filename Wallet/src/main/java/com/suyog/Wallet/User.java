package com.suyog.Wallet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.mindrot.jbcrypt.BCrypt; //hashpassword


@Entity
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)//Creates single table for user type like BusinessUser and PersonalUser in single table

@Inheritance(strategy=InheritanceType.JOINED) //creates separate table for BusinessUser and PersonalUser

//@DiscriminatorColumn(name="User_type",discriminatorType=DiscriminatorType.STRING)

public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userID;
	
	@Column(nullable=false,unique=true)
	private String email;
	

	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String userType;
	
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval= true, fetch = FetchType.LAZY) 
    @OnDelete(action =OnDeleteAction.CASCADE)  //ensure wallet is deleted when user is deleted
    private Wallet wallet;
    
	public User(){}  //default constructor required by hibernate
	
	
	//paramaterized costructor for new users
	public User( String email,String password) {
		this.email = email;
		setPassword(password);
		
		
	}
	
	
	//hash password
	private String hashPassword (String plainPassword) {
		return BCrypt.hashpw(plainPassword,BCrypt.gensalt(12));
	}
	
	//verify password
	boolean verifyPassword (String password) {
		return BCrypt.checkpw(password,this.password);
	}
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		//preventing re-hashing an already hashed password 
		//if someone change user password after regestration it will be storeed in plain test bypassing hasing so need to check if it is hash or not
		if(!password.startsWith("$2a$")){
			this.password=hashPassword(password);
		}
		else {
			this.password=password; //alredy  hashed
		}
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	
	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	public Wallet getWallet() {
		return wallet;
	}


	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}




	
	
	
	

}
