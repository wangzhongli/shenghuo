package com.e1858.wuye.protocol.http;


import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddComplaintResponseResp extends PacketResp
{
	@Expose
	long response = -1;// 增加成功后返回新增的ID；不成功为-1；

	public AddComplaintResponseResp()
	{
		command = HttpDefine.ADD_COMPLAINTRESPONSE_RESP;
	}

	public long getResponse()
	{
		return response;
	}

	public void setResponse(long response)
	{
		this.response = response;
	}

}
