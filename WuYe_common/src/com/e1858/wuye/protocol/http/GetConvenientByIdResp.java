package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConvenientByIdResp extends PacketResp
{
	@Expose
	Convenient convenience;// 便民项目

	public GetConvenientByIdResp()
	{
		command = HttpDefine.GET_CONVENIENT_BY_ID_RESP;
	}

	public Convenient getConvenience() {
		return convenience;
	}

	public void setConvenience(Convenient convenience) {
		this.convenience = convenience;
	}

	


}
