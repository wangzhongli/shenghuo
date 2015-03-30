package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class GetFeeTypeResp extends PacketResp {

	@Expose
	private List<FeeType> feeTypes=new ArrayList<FeeType>();

	public List<FeeType> getFeeTypes() {
		return feeTypes;
	}

	public void setFeeTypes(List<FeeType> feeTypes) {
		this.feeTypes = feeTypes;
	}
	
}
