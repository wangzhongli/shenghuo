package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallAddCommentResp extends PacketResp {


	public MallAddCommentResp() {
		command = HttpDefine.MallAddComment_RESP;
	}

}
