package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallAddHeartResp extends PacketResp {
	public MallAddHeartResp() {
		command = HttpDefine.MallAddHeart_RESP;
	}

}
