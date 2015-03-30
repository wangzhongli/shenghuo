package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConsumeTypesResp extends PacketResp
{
	@Expose
	List<ConsumeType> consumeTypes;// 代办事物列表

	public GetConsumeTypesResp()
	{
		command = HttpDefine.GET_CONSUME_TYPES_RESP;
		consumeTypes = new ArrayList<ConsumeType>();
	}

	public List<ConsumeType> getConsumeTypes()
	{
		return consumeTypes;
	}

	public void setConsumeTypes(List<ConsumeType> consumeTypes)
	{
		this.consumeTypes = consumeTypes;
	}

}
