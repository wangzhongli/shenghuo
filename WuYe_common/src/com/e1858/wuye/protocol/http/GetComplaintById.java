package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetComplaintById extends PacketRequest{	
	@Expose
	private int ID=-1;//投诉建议ID

	public GetComplaintById(){
		command = HttpDefine.GET_COMPLAINT_BY_ID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	
}