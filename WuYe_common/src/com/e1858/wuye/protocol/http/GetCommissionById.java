package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommissionById extends PacketRequest{	
	@Expose
	private int ID=-1;//代办事物ID

	public GetCommissionById(){
		command = HttpDefine.GET_COMMISSION_BY_ID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
