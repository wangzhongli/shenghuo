package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallRCProduct;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetProductAlreadyResp extends PacketResp {
	public List<MallRCProduct>	mallRCProducts; //已购商品数组

	public MallGetProductAlreadyResp() {
		command = HttpDefine.MallGetProductAlready_RESP;
	}

	public List<MallRCProduct> getMallRCProducts() {
		return mallRCProducts;
	}

	public void setMallRCProducts(List<MallRCProduct> mallRCProducts) {
		this.mallRCProducts = mallRCProducts;
	}

}
