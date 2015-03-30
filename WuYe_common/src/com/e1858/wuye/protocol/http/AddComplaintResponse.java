package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddComplaintResponse extends PacketRequest
{

	@Expose
	private byte complaint = 0;// 回复的投诉建议ID
	@Expose
	private String content = "";// 回复内容

	public AddComplaintResponse()
	{
		command = HttpDefine.ADD_COMPLAINTRESPONSE;
	}

	public byte getComplaint()
	{
		return complaint;
	}

	public void setComplaint(byte complaint)
	{
		this.complaint = complaint;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
