package com.e1858.wuye.protocol.http;

@SuppressWarnings("serial")
public class ChangeCommunity extends PacketRequest{

	public ChangeCommunity(){
		command = HttpDefine.CHANGE_COMMUNITY;
	}
}
