package com.example.demo.model;

public class UserCredentials {

	String userName;
	String password;
	
	public UserCredentials()
	{
		
	}

	public UserCredentials(String userName, String password) {
		
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserCredentials [userName=" + userName + ", password=" + password + "]";
	}
	
	
	
	
}

