package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelCancelOrder extends PacketRequest {

	public KangelCancelOrder(){
		command=HttpDefine.KANGEL_CANCLE_ORDER;
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
