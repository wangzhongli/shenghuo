package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddMaintenanceResponse extends PacketRequest
{

	@Expose
	private byte maintenance = 0;// 回复的报修ID
	@Expose
	private String content = "";// 回复内容

	public AddMaintenanceResponse()
	{
		command = HttpDefine.ADD_MAINTENANCERESPONSE;
	}

	public byte getMaintenance()
	{
		return maintenance;
	}

	public void setMaintenance(byte maintenance)
	{
		this.maintenance = maintenance;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
