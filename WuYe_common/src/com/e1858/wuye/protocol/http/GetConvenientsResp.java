package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConvenientsResp extends PacketResp
{
	@Expose
	List<Convenient> conveniences;// 便民项目类型列表

	public GetConvenientsResp()
	{
		command = HttpDefine.GET_CONVENIENTS_RESP;
		conveniences = new ArrayList<Convenient>();
	}

	public List<Convenient> getConveniences() {
		return conveniences;
	}

	public void setConveniences(List<Convenient> conveniences) {
		this.conveniences = conveniences;
	}

	

}
