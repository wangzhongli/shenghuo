package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetComplaintsResp  extends PacketResp{
	@Expose
	List<Complaint>  complaints;//投诉建议列表
	public GetComplaintsResp(){
		command = HttpDefine.GET_COMPLAINTS_RESP;
		complaints=new ArrayList<Complaint>();
	}
	public List<Complaint> getComplaints()
	{
		return complaints;
	}
	public void setComplaints(List<Complaint> complaints)
	{
		this.complaints = complaints;
	}

}
