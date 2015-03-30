package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ChangeHouse extends PacketRequest
{

	@Expose
	private int houseID = 0;// 新切换的楼栋ID
	@Expose
	private int houseFloorID = 0;// 新切换的楼层ID
	@Expose
	private int houseUnitID = 0;// 新切换的单元ID
	@Expose
	private String room = "";// 新切换的门牌号
	@Expose
	private String verify = "";// 校验码
	
	public ChangeHouse()
	{
		command = HttpDefine.CHANGE_HOUSE;
	}


	public int getHouseID() {
		return houseID;
	}

	public void setHouseID(int houseID) {
		this.houseID = houseID;
	}

	public int getHouseFloorID() {
		return houseFloorID;
	}

	public void setHouseFloorID(int houseFloorID) {
		this.houseFloorID = houseFloorID;
	}

	public int getHouseUnitID() {
		return houseUnitID;
	}

	public void setHouseUnitID(int houseUnitID) {
		this.houseUnitID = houseUnitID;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	
}
