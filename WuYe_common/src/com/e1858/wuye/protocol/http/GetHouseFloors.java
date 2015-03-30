package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetHouseFloors extends PacketRequest{
	@Expose
	private int houseID=-1;//楼层ID
	@Expose
	private int houseUnitID=-1;

	public GetHouseFloors(){
		command = HttpDefine.GET_HOUSEFLOORS;
	}

	public int getHouseID() {
		return houseID;
	}

	public void setHouseID(int houseID) {
		this.houseID = houseID;
	}

	public int getHouseUnitID() {
		return houseUnitID;
	}

	public void setHouseUnitID(int houseUnitID) {
		this.houseUnitID = houseUnitID;
	}
	
}
