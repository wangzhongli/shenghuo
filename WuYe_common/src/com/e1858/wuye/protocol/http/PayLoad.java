package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class PayLoad implements Serializable{

	@Expose
	private int ID=-1;
	@Expose
	private String content="";
	@Expose
	private int typeID=-1;//(1、通知 2、代办 3、投诉 4、报修)
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getTypeID() {
		return typeID;
	}
	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	
	
}
