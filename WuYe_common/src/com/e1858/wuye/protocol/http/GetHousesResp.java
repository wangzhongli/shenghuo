package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetHousesResp  extends PacketResp{
	@Expose
	List<House>  houses;//楼栋列表
	
	public GetHousesResp(){
		command = HttpDefine.GET_HOUSES_RESP;
		houses=new ArrayList<House>();
	}
	public List<House> getHouses()
	{
		return houses;
	}
	public void setHouses(List<House> houses)
	{
		this.houses = houses;
	}
}
