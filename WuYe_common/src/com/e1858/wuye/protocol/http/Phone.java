package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Phone implements Serializable{
	@Expose
	private String title="";//对应t_ServicePhone中的title
	@Expose
	private String edescription="";//对应t_ServicePhone中的description
	@Expose
	private String phone="";//对应t_servic ePhone中的phone
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getEdescription() {
		return edescription;
	}
	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
}
