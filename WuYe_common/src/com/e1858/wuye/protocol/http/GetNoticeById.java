package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetNoticeById extends PacketRequest{	
	@Expose
	private int ID=-1;//通知ID

	public GetNoticeById(){
		command = HttpDefine.READ_NOTICE_BY_ID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}



}
