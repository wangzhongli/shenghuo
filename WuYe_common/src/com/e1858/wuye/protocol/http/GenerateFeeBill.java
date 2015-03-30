package com.e1858.wuye.protocol.http;

import java.util.List;

import com.e1858.common.Constant;
import com.google.gson.annotations.Expose;

public class GenerateFeeBill extends PacketRequest {
	public GenerateFeeBill() {
		command = HttpDefine.GENERATE_FEEBILL;
	}

	@Expose
	private List<String>	feeIDs;
	@Expose
	private int				feeTypeID;
	@Expose
	private float			amount;

	@Expose
	private String			mobileOS	= Constant.ANDROID;

	@Expose
	private int				points;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public List<String> getFeeIDs() {
		return feeIDs;
	}

	public void setFeeIDs(List<String> feeIDs) {
		this.feeIDs = feeIDs;
	}

	public int getFeeTypeID() {
		return feeTypeID;
	}

	public void setFeeTypeID(int feeTypeID) {
		this.feeTypeID = feeTypeID;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getMobileOS() {
		return mobileOS;
	}

	public void setMobileOS(String mobileOS) {
		this.mobileOS = mobileOS;
	}

}
