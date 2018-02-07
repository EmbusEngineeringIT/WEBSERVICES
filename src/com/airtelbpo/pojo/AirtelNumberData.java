package com.airtelbpo.pojo;

import com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl;

public class AirtelNumberData {
	
	private String id,number;
	private boolean flag;
	
	public AirtelNumberData(String id,String number) {
		super();
		this.id = id;
		this.flag = false;
		this.number = number;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
    @Override

    public String toString() {
    	return "AirtelNumberData [ID=" + id + ", PHONE_NUMBER=" + number+"]";
    }

}
