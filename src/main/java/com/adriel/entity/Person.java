package com.adriel.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Person")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "personOrders"})
public class Person extends SantaTrackerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private int personID;
	
	@Column(unique = true)
	private String username;
	
	private String pword;
	@Column(unique = true)
	private String email;
	private String address;
	private int admin;
	private int demo;
	private String resetPasswordToken;
	private String adminToken;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "person")
	private List<Order> personOrders = new ArrayList<Order>();
	
	public Person() {
		
	}
	
	public Person(String username, String password, String email, String address, int admin) {
		super();
		this.username = username;
		this.pword = password;
		this.email = email;
		this.address = address;
		this.admin = admin;
	}
	
	public int getPersonID() {
		return personID;
	}
	
	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPword() {
		return pword;
	}

	public void setPword(String pword) {
		this.pword = pword;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}

	public int getDemo() {
		return demo;
	}

	public void setDemo(int demo) {
		this.demo = demo;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public String getAdminToken() {
		return adminToken;
	}

	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}

	public List<Order> getPersonOrders() {
		return personOrders;
	}

	public void setPersonOrders(List<Order> personOrders) {
		this.personOrders = personOrders;
	}
	
	public void addOrder(Order ord) {
		this.personOrders.add(ord);
		ord.setPerson(this);
	}

	@Override
	public String toString() {
		return "Person [personID=" + personID + ", username=" + username + ", pword=" + pword + ", email=" + email
				+ ", address=" + address + ", admin=" + admin + "]";
	}
}
