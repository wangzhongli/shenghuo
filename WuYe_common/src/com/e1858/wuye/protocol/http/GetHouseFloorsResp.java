package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetHouseFloorsResp  extends PacketResp{
	@Expose
	List<HouseFloor>  houseFloors;//楼层列表
	
	public GetHouseFloorsResp(){
		command = HttpDefine.GET_HOUSEFLOORS_RESP;
		houseFloors=new ArrayList<HouseFloor>();
	}

	public List<HouseFloor> getHouseFloors()
	{
		return houseFloors;
	}

	public void setHouseFloors(List<HouseFloor> housesFloor)
	{
		this.houseFloors = housesFloor;
	}
	

}
