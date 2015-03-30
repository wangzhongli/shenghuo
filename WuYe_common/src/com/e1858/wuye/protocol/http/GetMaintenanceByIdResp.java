package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetMaintenanceByIdResp  extends PacketResp{
	@Expose
	Maintenance  maintenance;//报修详细信息
	public GetMaintenanceByIdResp(){
		command = HttpDefine.GET_MAINTENANCE_BY_ID_RESP;
	}
	public Maintenance getMaintenance()
	{
		return maintenance;
	}
	public void setMaintenance(Maintenance maintenance)
	{
		this.maintenance = maintenance;
	}

	
}
