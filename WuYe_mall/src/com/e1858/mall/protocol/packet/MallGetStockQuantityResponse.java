package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetStockQuantityResponse extends PacketResp {

	public int	quantity;	//int	数量

	public MallGetStockQuantityResponse() {
		super();
		setCommand(HttpDefine.MallGetStockQuantity_RESP);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
