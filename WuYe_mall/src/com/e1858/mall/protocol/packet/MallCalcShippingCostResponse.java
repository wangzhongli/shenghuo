package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallCalcShippingCostResponse extends PacketResp {
	private List<Float>	shippingCosts;

	public MallCalcShippingCostResponse() {}

	public List<Float> getShippingCosts() {
		return shippingCosts;
	}

	public void setShippingCosts(List<Float> shippingCosts) {
		this.shippingCosts = shippingCosts;
	}

}
