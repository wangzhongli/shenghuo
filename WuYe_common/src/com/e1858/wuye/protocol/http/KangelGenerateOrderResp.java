package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelGenerateOrderResp extends PacketResp {

	public KangelGenerateOrderResp(){
		command=HttpDefine.KANGEL_GENERATE_ORDER_RESP;
	}
	@Expose
	private String orderSn;
	@Expose
	private String uuid;
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
