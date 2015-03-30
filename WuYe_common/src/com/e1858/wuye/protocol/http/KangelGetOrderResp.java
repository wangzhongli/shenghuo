package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelGetOrderResp extends PacketResp {

	public KangelGetOrderResp(){
		command=HttpDefine.KANGEL_GET_ORDER_RESP;
	}
	@Expose
	private KangelOrder order;
	public KangelOrder getOrder() {
		return order;
	}
	public void setOrder(KangelOrder order) {
		this.order = order;
	}
	
}
