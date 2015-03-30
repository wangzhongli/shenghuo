package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class CommissionType extends PacketRequest{
	@Expose
	private int ID=-1;//代办事物类型ID
	@Expose
	private String name="";//代办事物类型名称

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
