package com.e1858.wuye.protocol.http;

import java.util.List;

import com.google.gson.annotations.Expose;

public class KangelGetOrdersResp extends PacketResp {

	public KangelGetOrdersResp(){
		command=HttpDefine.KANGEL_GET_ORDERS_RESP;
	}
	
	@Expose
	private List<KangelOrder> orders;

	public List<KangelOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<KangelOrder> orders) {
		this.orders = orders;
	}
	
}

