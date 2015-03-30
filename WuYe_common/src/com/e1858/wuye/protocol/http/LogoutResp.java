package com.e1858.wuye.protocol.http;

@SuppressWarnings("serial")
public class LogoutResp  extends PacketResp{
	
	public LogoutResp(){
		command=HttpDefine.LOGOUT_RESP;
	}
}
