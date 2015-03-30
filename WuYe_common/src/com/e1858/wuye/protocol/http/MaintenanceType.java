package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class MaintenanceType extends PacketRequest{
	@Expose
	private int ID=-1;//设备报修类型ID
	@Expose
	private String name="";//类型名称

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}


}
