package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetHouseUnitsResp  extends PacketResp{
	@Expose
	List<HouseUnit>  houseUnits;//单元列表
	
	public GetHouseUnitsResp(){
		command = HttpDefine.GET_HOUSEUNITS_RESP;
		houseUnits=new ArrayList<HouseUnit>();
	}
	public List<HouseUnit> getHouseUnits()
	{
		return houseUnits;
	}

	public void setHouseUnits(List<HouseUnit> housesUnit)
	{
		this.houseUnits = housesUnit;
	}
}
