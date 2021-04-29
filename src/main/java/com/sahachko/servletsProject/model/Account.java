package com.sahachko.servletsProject.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "Accounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = false)
	@Type(type = "date")
	private Date birthDate;
	
	@Column(nullable = false)
	@Type(type = "date")
	private Date signUpDate;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	
	public Account() {
	}
	
	public Account(String firstName, String lastName, Date birthDate, AccountStatus accountStatus) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.accountStatus = accountStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getSignUpDate() {
		return signUpDate;
	}

	public void setSignUpDate(Date signUpDate) {
		this.signUpDate = signUpDate;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
		return "Account [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + dateFormat.format(birthDate)
				+ ", signUpDate=" + dateFormat.format(signUpDate) + ", accountStatus=" + accountStatus + "]";
	}
}
