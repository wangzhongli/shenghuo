package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetHouseUnits extends PacketRequest{
	@Expose
	private int houseID=-1;//楼层ID

	public GetHouseUnits(){
		command = HttpDefine.GET_HOUSEUNITS;
	}

	public int getHouseID() {
		return houseID;
	}

	public void setHouseID(int houseID) {
		this.houseID = houseID;
	}
}
