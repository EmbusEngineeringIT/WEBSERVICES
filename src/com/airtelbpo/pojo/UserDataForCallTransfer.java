package com.airtelbpo.pojo;

public class UserDataForCallTransfer {
	
	public String Id,userName,userEmail,userKnownLanguage;
	
	public UserDataForCallTransfer(String Id,String userName,String userEmail,String userKnownLanguage) {
		super();
		this.Id = Id;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userKnownLanguage = userKnownLanguage;
	}
	
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		Id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserKnownLanguage() {
		return userKnownLanguage;
	}
	
	public void setUserKnownLanguage(String userKnownLanguage) {
		this.userKnownLanguage = userKnownLanguage;
	}

}
