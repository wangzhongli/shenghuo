package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class GetFees extends PacketRequest {
	
	public GetFees(){
		command=HttpDefine.GET_FEES;
	}
	@Expose
	private int feeTypeID=-1;
	@Expose
	private String waterNumber="";
	
	@Expose	
	private String electricNumber="";
	
	@Expose
	private String gasNumber="";
	
	public int getFeeTypeID() {
		return feeTypeID;
	}
	public void setFeeTypeID(int feeTypeID) {
		this.feeTypeID = feeTypeID;
	}
	public String getWaterNumber() {
		return waterNumber;
	}
	public void setWaterNumber(String waterNumber) {
		this.waterNumber = waterNumber;
	}
	public String getElectricNumber() {
		return electricNumber;
	}
	public void setElectricNumber(String electricNumber) {
		this.electricNumber = electricNumber;
	}
	public String getGasNumber() {
		return gasNumber;
	}
	public void setGasNumber(String gasNumber) {
		this.gasNumber = gasNumber;
	}
}
