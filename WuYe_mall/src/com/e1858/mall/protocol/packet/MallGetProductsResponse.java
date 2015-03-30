package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallProductBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetProductsResponse extends PacketResp {
	public List<MallProductBean>	products;

	public MallGetProductsResponse() {
		command = HttpDefine.MallGetProducts_RESP;
	}

	public List<MallProductBean> getProducts() {
		return products;
	}

	public void setProducts(List<MallProductBean> products) {
		this.products = products;
	}

}
