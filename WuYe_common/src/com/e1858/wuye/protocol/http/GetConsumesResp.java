package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConsumesResp extends PacketResp
{
	@Expose
	List<Consume> consumes = null;// 账单列表

	public GetConsumesResp()
	{
		command = HttpDefine.GET_CONSUMES_RESP;
		consumes = new ArrayList<Consume>();
	}

	public List<Consume> getConsumes()
	{
		return consumes;
	}

	public void setConsumes(List<Consume> consumes)
	{
		this.consumes = consumes;
	}
}
