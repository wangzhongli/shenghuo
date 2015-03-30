package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddCommissionResponse extends PacketRequest
{

	@Expose
	private byte commission = 0;// 回复的代办ID
	@Expose
	private String content = "";// 回复内容

	public AddCommissionResponse()
	{
		command = HttpDefine.ADD_COMMISSIONRESPONSE;
	}

	public byte getCommission()
	{
		return commission;
	}

	public void setCommission(byte commission)
	{
		this.commission = commission;
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
