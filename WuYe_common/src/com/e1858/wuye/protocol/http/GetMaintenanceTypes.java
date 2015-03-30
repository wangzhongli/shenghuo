package com.e1858.wuye.protocol.http;

@SuppressWarnings("serial")
public class GetMaintenanceTypes extends PacketRequest{	
	
	public GetMaintenanceTypes(){
		command = HttpDefine.GET_MAINTENANCE_TYPES;
	}



}
