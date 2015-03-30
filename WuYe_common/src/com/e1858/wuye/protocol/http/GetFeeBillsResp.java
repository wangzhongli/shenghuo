package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class GetFeeBillsResp extends PacketResp{
	@Expose
	private List<FeeBill> feeBills=new ArrayList<FeeBill>();

	public List<FeeBill> getFeeBills() {
		return feeBills;
	}

	public void setFeeBills(List<FeeBill> feeBills) {
		this.feeBills = feeBills;
	}
	

}
