package com.example.model;

import org.springframework.stereotype.Component;

@Component
public class User {

	private int userKey;
	
	private String account;
	
	private String password;
	
	public User() {}
	
	public User(int userKey, String account, String password) {
		this.setUserKey(userKey);
		this.setAccount(account);
		this.setPassword(password);
	}
	
	public int getUserKey() {
		return this.userKey;
	}
	
	public void setUserKey(int userKey) {
		this.userKey = userKey;
	}
	
	public String getAccount() {
		return this.account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}