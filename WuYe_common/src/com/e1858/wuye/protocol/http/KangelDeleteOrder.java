package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelDeleteOrder extends PacketRequest {

	public KangelDeleteOrder(){
		command=HttpDefine.KANGEL_DELETE_ORDER;
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
