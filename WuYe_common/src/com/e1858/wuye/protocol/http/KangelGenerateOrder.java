package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelGenerateOrder extends PacketRequest {

	public KangelGenerateOrder(){
		command=HttpDefine.KANGEL_GENERATE_ORDER;
	}
	@Expose
	private String edescription;
	@Expose
	private String takeDetailAddress;//取件详细地址
	@Expose
	private int takePlatBaseRegionID;//取件区域ID
	@Expose
	private String appointmentTime;//预约取件时间
	@Expose
	private int takePlatBaseRegionAreaID;//基础数据中的取件片区 ID
	@Expose
	private String membername;//会员名
	@Expose
	private String mobilephone;//联系电话
	public String getEdescription() {
		return edescription;
	}
	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}
	public String getTakeDetailAddress() {
		return takeDetailAddress;
	}
	public void setTakeDetailAddress(String takeDetailAddress) {
		this.takeDetailAddress = takeDetailAddress;
	}
	public int getTakePlatBaseRegionID() {
		return takePlatBaseRegionID;
	}
	public void setTakePlatBaseRegionID(int takePlatBaseRegionID) {
		this.takePlatBaseRegionID = takePlatBaseRegionID;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public int getTakePlatBaseRegionAreaID() {
		return takePlatBaseRegionAreaID;
	}
	public void setTakePlatBaseRegionAreaID(int takePlatBaseRegionAreaID) {
		this.takePlatBaseRegionAreaID = takePlatBaseRegionAreaID;
	}
	public String getMembername() {
		return membername;
	}
	public void setMembername(String membername) {
		this.membername = membername;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	
	
}
