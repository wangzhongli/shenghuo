package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallRefundResponse extends PacketResp {
	public MallRefundResponse() {
		command = HttpDefine.MallRefund_RESP;
	}

}
