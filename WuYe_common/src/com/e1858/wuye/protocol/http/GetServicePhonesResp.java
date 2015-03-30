package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetServicePhonesResp extends PacketResp
{
	@Expose
	List<Phone> phones;// 电话列表

	public GetServicePhonesResp()
	{
		command = HttpDefine.GET_SERVICE_PHONES_RESP;
		phones = new ArrayList<Phone>();
	}

	public List<Phone> getPhones()
	{
		return phones;
	}

	public void setPhones(List<Phone> phones)
	{
		this.phones = phones;
	}

}
