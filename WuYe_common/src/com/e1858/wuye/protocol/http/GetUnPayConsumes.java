package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetUnPayConsumes extends PacketRequest
{

	@Expose
	long type;// 类型
	@Expose
	String payNumnber;// 户号

	public GetUnPayConsumes()
	{
		command = HttpDefine.GET_UNPAY_CONSUMES;
	}

	public long getType()
	{
		return type;
	}

	public void setType(long type)
	{
		this.type = type;
	}

	public String getPayNumnber()
	{
		return payNumnber;
	}

	public void setPayNumnber(String payNumnber)
	{
		this.payNumnber = payNumnber;
	}



}
