package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConsumes extends PacketRequest
{

	@Expose
	long type;// 类型
	@Expose
	String payNumnber;// 户号
	@Expose
	String startDate;// 起始日期
	@Expose
	String endDate;// 终止日期

	public GetConsumes()
	{
		command = HttpDefine.GET_CONSUMES;
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

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

}
