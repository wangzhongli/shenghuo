package com.e1858.wuye.protocol.http;
@SuppressWarnings("serial")
public class ChangePasswordResp extends PacketResp {

	public ChangePasswordResp(){
		command=HttpDefine.CHANGE_PASSWORD_RESP;
	}
}
