package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class House implements Serializable{
	@Expose
	private int ID=0;//楼栋ID，对应t_House中的ID
	@Expose
	private String name="";//楼栋名称，对应t_House中的name
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
