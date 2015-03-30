package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelGetAddressResp extends PacketResp {
	@Expose
	private String	province;
	@Expose
	private String	city;
	@Expose
	private String	district;

	@Expose
	private String	takeDetailAddress;			//取件详细地址
	@Expose
	private int		takePlatBaseRegionID;		//取件区域ID
	@Expose
	private int		takePlatBaseRegionAreaID;	//基础数据中的取件片区 ID
	@Expose
	private String	membername;				//会员名
	@Expose
	private String	mobilephone;				//联系电话

	public KangelGetAddressResp() {
		command = HttpDefine.KANGEL_GET_ADDRESS_RESP;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}
