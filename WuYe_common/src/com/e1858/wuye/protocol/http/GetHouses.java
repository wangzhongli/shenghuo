package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetHouses extends PacketRequest{

	public GetHouses(){
		command = HttpDefine.GET_HOUSES;
	}
}
