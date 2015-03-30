package com.e1858.wuye.protocol.http;
@SuppressWarnings("serial")
public class ResetPassResp  extends PacketResp{

	public ResetPassResp(){
		command = HttpDefine.RESET_PASS_RESP;
	}
}
