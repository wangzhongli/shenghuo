package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class DeleteFeeBill extends PacketRequest {
	public DeleteFeeBill(){
		command=HttpDefine.DELETE_FEEBILL;
	}
	@Expose
	private String ID="";
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
}
