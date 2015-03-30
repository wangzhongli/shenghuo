package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommissionTypesResp  extends PacketResp{
	@Expose
	List<CommissionType>  types;//代办事物列表
	public GetCommissionTypesResp(){
		command = HttpDefine.GET_COMMISSION_TYPES_RESP;
		types=new ArrayList<CommissionType>();
	}
	public List<CommissionType> getTypes() {
		return types;
	}
	public void setTypes(List<CommissionType> types) {
		this.types = types;
	}
}
