package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Complaint implements Serializable{
	@Expose
	private int ID=-1;//投诉建议ID，对应t_Complaint中的ID
	@Expose
	private String content="";//内容，对应t_Complaint中的content
	@Expose
	List<ComplaintResponse> responses = new ArrayList<ComplaintResponse>();//投诉建议回复记录，对应t_ComplaintResponse的信息
	@Expose
	String createTime = "";//发表时间，对应t_Complaint的createTime
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public List<ComplaintResponse> getResponses()
	{
		return responses;
	}
	public void setResponses(List<ComplaintResponse> complaintResponses)
	{
		this.responses = complaintResponses;
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
