package com.e1858.wuye.protocol.http;

@SuppressWarnings("serial")
public class GetServicePhones extends PacketRequest{	

	public GetServicePhones(){
		command = HttpDefine.GET_SERVICE_PHONES;
	}

}
