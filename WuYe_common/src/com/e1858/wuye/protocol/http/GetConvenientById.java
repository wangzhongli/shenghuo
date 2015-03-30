package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConvenientById extends PacketRequest
{
	@Expose
	private int ID = -1;// 便民项目ID

	public GetConvenientById()
	{
		command = HttpDefine.GET_CONVENIENT_BY_ID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	

	
}
