package com.e1858.wuye.protocol.http;

@SuppressWarnings("serial")
public class GetMyProfile extends PacketRequest{	
	
	public GetMyProfile(){
		command = HttpDefine.GET_MY_PROFILE;
	}
	
}
