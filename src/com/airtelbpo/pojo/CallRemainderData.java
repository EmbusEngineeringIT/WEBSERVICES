package com.airtelbpo.pojo;

import org.codehaus.jackson.map.introspect.BasicClassIntrospector.GetterMethodFilter;

public class CallRemainderData {
	
	public int Id;
	public String userEmail;
	public String userCustomerName;
	public String userContactNumber;
	public String userRemainderComment;
	
	public CallRemainderData(int Id, String userEmail,String userCustomerName,String userContactNumber,String userRemainderComment) {
		
		this.Id = Id;
		this.userEmail = userEmail;
		this.userCustomerName = userCustomerName;
		this.userContactNumber = userContactNumber;
		this.userRemainderComment = userRemainderComment;
		
	}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserCustomerName() {
		return userCustomerName;
	}
	
	public void setUserCustomerName(String userCustomerName) {
		this.userCustomerName = userCustomerName;
	}
	
	public String getUserContactNumber() {
		return userContactNumber;
	}

	public void setUserContactNumber(String userContactNumber) {
		this.userContactNumber = userContactNumber;
	}
	
	
	public String getUserRemainderComment() {
		return userRemainderComment;
	}
	
	
	public void setUserRemainderComment(String userRemainderComment) {
		this.userRemainderComment = userRemainderComment;
	}
	
}
