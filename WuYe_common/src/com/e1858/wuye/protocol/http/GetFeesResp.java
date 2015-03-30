package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class GetFeesResp extends PacketResp {
	@Expose
	private List<Fee> fees=new ArrayList<Fee>();

	public List<Fee> getFees() {
		return fees;
	}
	public void setFees(List<Fee> fees) {
		this.fees = fees;
	}
	
	
}
