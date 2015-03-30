package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommissionsResp  extends PacketResp{
	@Expose
	List<Commission>  commissions;//代办事物列表
	public GetCommissionsResp(){
		command = HttpDefine.GET_COMMISSIONS_RESP;
		commissions=new ArrayList<Commission>();
	}
	public List<Commission> getCommissions()
	{
		return commissions;
	}
	public void setCommissions(List<Commission> commissions)
	{
		this.commissions = commissions;
	}

	
}
