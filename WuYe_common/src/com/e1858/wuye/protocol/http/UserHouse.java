package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class UserHouse implements Serializable{
	@Expose
	private House house=new House();//楼栋
	@Expose
	private HouseUnit houseUnit=new HouseUnit();//单元
	@Expose
	private HouseFloor houseFloor=new HouseFloor();//楼层
	@Expose
	private HouseRoom houseRoom=new HouseRoom();//门牌
	
	public House getHouse()
	{
		return house;
	}
	public void setHouse(House house)
	{
		this.house = house;
	}
	public HouseUnit getHouseUnit()
	{
		return houseUnit;
	}
	public void setHouseUnit(HouseUnit houseUnit)
	{
		this.houseUnit = houseUnit;
	}
	public HouseFloor getHouseFloor()
	{
		return houseFloor;
	}
	public void setHouseFloor(HouseFloor houseFloor)
	{
		this.houseFloor = houseFloor;
	}
	public HouseRoom getHouseRoom()
	{
		return houseRoom;
	}
	public void setHouseRoom(HouseRoom houseRoom)
	{
		this.houseRoom = houseRoom;
	}
	
}
