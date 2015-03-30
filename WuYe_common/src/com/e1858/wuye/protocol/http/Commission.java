package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Commission implements Serializable{
	@Expose
	private int ID=-1;//代办事物ID，对应t_Commission中的ID
	@Expose
	private CommissionType type=new CommissionType();
	@Expose
	private String content="";//内容，对应t_Commission中的content
	@Expose
	private List<CommissionResponse> responses = new ArrayList<CommissionResponse>();//代办事物回复记录
	@Expose
	private String createTime = "";//发表时间，对应t_Complaint的createTime

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	
	
	public CommissionType getType() {
		return type;
	}
	public void setType(CommissionType type) {
		this.type = type;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public List<CommissionResponse> getResponses()
	{
		return responses;
	}
	public void setResponses(List<CommissionResponse> commissionResponses)
	{
		this.responses = commissionResponses;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	
	
}
