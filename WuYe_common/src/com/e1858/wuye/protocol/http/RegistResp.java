package com.e1858.wuye.protocol.http;
@SuppressWarnings("serial")
public class RegistResp  extends PacketResp{

	public RegistResp(){
		command=HttpDefine.REGIST_RESP;
	}
}
