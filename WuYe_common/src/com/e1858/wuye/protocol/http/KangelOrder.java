package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class KangelOrder implements Serializable {

	@Expose
	private String mobilephone="";
	@Expose
	private String edescription="";
	@Expose
	private String platMemberName="";//订单会员名
	@Expose
	private String createDate="";//下单时间
	@Expose
	private String platSalesMan="";
	@Expose
	private String takeDetailAddress="";//取件详细地址
	@Expose
	private KangelPlatBaseRegion takePlatBaseRegion;//取件区域
	@Expose
	private String orderSn="";//订单号
	@Expose
	private String appointmentTime="";//预约取件时间
	@Expose
	private KangelPlatBaseRegionArea takePlatBaseRegionArea;//取货片区
	@Expose
	private String uuid="";//订单UUID
	@Expose
	private int waringType;//预警类型:0无1:超时未接单2:超时未收件3:超时未送件 
	@Expose
	private String receiveTime="";//签收时间
	@Expose
	private String takeTime="";//取件时间
	@Expose
	private String washTime="";//洗涤时间
	@Expose
	private List<KangelOrderStatus> orderStatus=new ArrayList<KangelOrderStatus>();
	
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getEdescription() {
		return edescription;
	}
	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}
	public String getPlatMemberName() {
		return platMemberName;
	}
	public void setPlatMemberName(String platMemberName) {
		this.platMemberName = platMemberName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getPlatSalesMan() {
		return platSalesMan;
	}
	public void setPlatSalesMan(String platSalesMan) {
		this.platSalesMan = platSalesMan;
	}
	public String getTakeDetailAddress() {
		return takeDetailAddress;
	}
	public void setTakeDetailAddress(String takeDetailAddress) {
		this.takeDetailAddress = takeDetailAddress;
	}
	public KangelPlatBaseRegion getTakePlatBaseRegion() {
		return takePlatBaseRegion;
	}
	public void setTakePlatBaseRegion(KangelPlatBaseRegion takePlatBaseRegion) {
		this.takePlatBaseRegion = takePlatBaseRegion;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public KangelPlatBaseRegionArea getTakePlatBaseRegionArea() {
		return takePlatBaseRegionArea;
	}
	public void setTakePlatBaseRegionArea(
			KangelPlatBaseRegionArea takePlatBaseRegionArea) {
		this.takePlatBaseRegionArea = takePlatBaseRegionArea;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getWaringType() {
		return waringType;
	}
	public void setWaringType(int waringType) {
		this.waringType = waringType;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getTakeTime() {
		return takeTime;
	}
	public void setTakeTime(String takeTime) {
		this.takeTime = takeTime;
	}
	public String getWashTime() {
		return washTime;
	}
	public void setWashTime(String washTime) {
		this.washTime = washTime;
	}
	public List<KangelOrderStatus> getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(List<KangelOrderStatus> orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
}
