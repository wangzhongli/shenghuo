package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallCartShopBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetCartProductsResponse extends PacketResp {
	public List<MallCartShopBean>	cartShops;

	public MallGetCartProductsResponse() {
		command = HttpDefine.MallGetCartProducts_RESP;
	}

	public List<MallCartShopBean> getCartShops() {
		return cartShops;
	}

	public void setCartShops(List<MallCartShopBean> cartShops) {
		this.cartShops = cartShops;
	}

}
