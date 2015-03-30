package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Fee implements Serializable {
	@Expose
	private String ID="";
	@Expose
	private String edescription="";//描述
	@Expose
	private float amount=0.0f;//金额
	@Expose
	private String createTime="";
	@Expose
	private int state;//缴费状态
	@Expose
	private String username="";//户主名称
	
	private int isChecked=0;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getEdescription() {
		return edescription;
	}
	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
	}
	
}
