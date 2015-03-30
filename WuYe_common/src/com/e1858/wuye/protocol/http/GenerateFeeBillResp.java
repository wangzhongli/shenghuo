package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class GenerateFeeBillResp extends PacketResp {

	@Expose
	private FeeBill	feeBill;

	@Expose
	private boolean	pointsPaidOff;

	public boolean isPointsPaidOff() {
		return pointsPaidOff;
	}

	public void setPointsPaidOff(boolean pointsPaidOff) {
		this.pointsPaidOff = pointsPaidOff;
	}

	public FeeBill getFeeBill() {
		return feeBill;
	}

	public void setFeeBill(FeeBill feeBill) {
		this.feeBill = feeBill;
	}

}
