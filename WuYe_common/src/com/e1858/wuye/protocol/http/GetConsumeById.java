package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConsumeById extends PacketRequest
{
	@Expose
	private long consume = -1;// 账单ID

	public GetConsumeById()
	{
		command = HttpDefine.GET_CONSUME_BY_ID;
	}

	public long getConsume()
	{
		return consume;
	}

	public void setConsume(long consume)
	{
		this.consume = consume;
	}

}
