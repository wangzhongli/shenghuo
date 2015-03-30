package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelGetOrder extends PacketRequest {

	public KangelGetOrder(){
		command=HttpDefine.KANGEL_GET_ORDER;
	}
	@Expose
	private String uuid;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
