package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetCartProductCountResponse extends PacketResp {
	private int	count;

	public MallGetCartProductCountResponse() {
		command = HttpDefine.MallGetCartProductCount_RESP;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
