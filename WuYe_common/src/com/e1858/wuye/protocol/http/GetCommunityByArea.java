package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommunityByArea extends PacketRequest{	
	@Expose
	private String city="";//地区，对应t_Community中的city

	public GetCommunityByArea(){
		command = HttpDefine.GET_COMMUNITY_BY_AREA;
	}
	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}


}
