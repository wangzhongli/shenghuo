package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetMaintenancesResp extends PacketResp
{
	@Expose
	List<Maintenance> maintenances;// 代办事物列表

	public GetMaintenancesResp()
	{
		command = HttpDefine.GET_MAINTENANCES_RESP;
		maintenances = new ArrayList<Maintenance>();
	}

	public List<Maintenance> getMaintenances()
	{
		return maintenances;
	}

	public void setMaintenances(List<Maintenance> maintenances)
	{
		this.maintenances = maintenances;
	}

}
