package com.e1858.wuye.protocol.http;


import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConsumeByIdResp extends PacketResp
{
	@Expose
	Consume consume;// 账单详细信息

	public GetConsumeByIdResp()
	{
		command = HttpDefine.GET_CONSUME_BY_ID_RESP;
	}

	public Consume getConsume()
	{
		return consume;
	}

	public void setConsume(Consume consume)
	{
		this.consume = consume;
	}

}
