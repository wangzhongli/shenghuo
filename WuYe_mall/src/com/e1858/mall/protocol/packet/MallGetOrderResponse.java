package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetOrderResponse extends PacketResp {

	private MallOrderBean	order;

	public MallGetOrderResponse() {
		command = HttpDefine.MallGetOrder_RESP;
	}

	public MallOrderBean getOrder() {
		return order;
	}

	public void setOrder(MallOrderBean order) {
		this.order = order;
	}

}
