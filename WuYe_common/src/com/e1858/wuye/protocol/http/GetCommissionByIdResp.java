package com.e1858.wuye.protocol.http;


import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommissionByIdResp  extends PacketResp{
	@Expose
	Commission  commission;//通知详细信息
	public GetCommissionByIdResp(){
		command = HttpDefine.GET_COMMISSION_BY_ID_RESP;
	}
	public Commission getCommission()
	{
		return commission;
	}
	public void setCommission(Commission commission)
	{
		this.commission = commission;
	}

	
}
