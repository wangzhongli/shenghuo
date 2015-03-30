package com.e1858.wuye.protocol.http;
import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetComplaintByIdResp  extends PacketResp{
	@Expose
	Complaint  complaint;//投诉与建议信息
	public GetComplaintByIdResp(){
		command = HttpDefine.GET_COMPLAINT_BY_ID_RESP;
	}
	public Complaint getComplaint()
	{
		return complaint;
	}
	public void setComplaint(Complaint complaint)
	{
		this.complaint = complaint;
	}


}
