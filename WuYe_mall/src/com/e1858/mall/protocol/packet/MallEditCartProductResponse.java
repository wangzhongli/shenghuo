package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallCartProductBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallEditCartProductResponse extends PacketResp {
	private MallCartProductBean	cartProduct;

	public MallEditCartProductResponse() {
		command = HttpDefine.MallEditCartProduct_RESP;
	}

	public MallCartProductBean getCartProduct() {
		return cartProduct;
	}

	public void setCartProduct(MallCartProductBean cartProduct) {
		this.cartProduct = cartProduct;
	}

}
