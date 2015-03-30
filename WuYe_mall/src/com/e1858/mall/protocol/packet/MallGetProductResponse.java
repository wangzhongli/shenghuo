package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallProductBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetProductResponse extends PacketResp {
	public MallProductBean	product;

	public MallGetProductResponse() {
		command = HttpDefine.MallGetProductDetail_RESP;
	}

	public MallProductBean getProduct() {
		return product;
	}

	public void setProduct(MallProductBean product) {
		this.product = product;
	}

}
