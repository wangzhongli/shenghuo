package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ConsumeType implements Serializable
{
	@Expose
	private long type = -1;// 类型ID
	@Expose
	private String name = "";// 类型名称

	public long getType()
	{
		return type;
	}

	public void setType(long commissionType)
	{
		this.type = commissionType;
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
