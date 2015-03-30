package com.e1858.wuye.protocol.http;

@SuppressWarnings("serial")
public class Logout extends PacketRequest{
	
	public Logout(){
		command = HttpDefine.LOGOUT;
	}
	
}
