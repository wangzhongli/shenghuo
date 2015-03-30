package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallDeleteOrderResponse extends PacketResp {
	public MallDeleteOrderResponse() {
		command = HttpDefine.MallDeleteOrder_RESP;
	}

}
