package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConvenientTypesResp extends PacketResp
{
	@Expose
	List<ConvenientType> types;// 便民项目类型列表

	public GetConvenientTypesResp()
	{
		command = HttpDefine.GET_CONVENIENT_TYPES_RESP;
		types = new ArrayList<ConvenientType>();
	}

	public List<ConvenientType> getTypes() {
		return types;
	}

	public void setTypes(List<ConvenientType> types) {
		this.types = types;
	}
}
