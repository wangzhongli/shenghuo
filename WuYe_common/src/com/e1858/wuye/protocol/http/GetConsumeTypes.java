package com.e1858.wuye.protocol.http;
@SuppressWarnings("serial")
public class GetConsumeTypes extends PacketRequest
{

	public GetConsumeTypes()
	{
		command = HttpDefine.GET_CONSUME_TYPES;
	}
}
