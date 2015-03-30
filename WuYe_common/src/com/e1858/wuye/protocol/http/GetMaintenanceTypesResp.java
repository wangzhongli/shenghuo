package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetMaintenanceTypesResp extends PacketResp
{
	@Expose
	List<MaintenanceType> types;// 代办事物列表

	public GetMaintenanceTypesResp()
	{
		command = HttpDefine.GET_MAINTENANCE_TYPES_RESP;
		types = new ArrayList<MaintenanceType>();
	}

	public List<MaintenanceType> getTypes() {
		return types;
	}

	public void setTypes(List<MaintenanceType> types) {
		this.types = types;
	}



}
