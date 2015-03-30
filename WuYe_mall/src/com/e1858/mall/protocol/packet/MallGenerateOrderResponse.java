package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGenerateOrderResponse extends PacketResp {
	private List<MallOrderBean>	orders;

	public MallGenerateOrderResponse() {
		command = HttpDefine.MallGenerateOrderNewVersion_RESP;
	}

	public List<MallOrderBean> getOrders() {
		return orders;
	}

	public void setOrders(List<MallOrderBean> orders) {
		this.orders = orders;
	}

}
