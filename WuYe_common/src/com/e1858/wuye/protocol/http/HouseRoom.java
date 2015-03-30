package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class HouseRoom implements Serializable
{
	@Expose
	private int ID = 0;// 门牌号ID
	@Expose
	private String name = "";// 门牌号


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
