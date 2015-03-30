package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelGetPlatBaseRegion extends PacketRequest {

	public KangelGetPlatBaseRegion(){
		command=HttpDefine.KANGEL_GET_PLATBASE_REGIN;
	}
	@Expose
	private String province;
	@Expose
	private String city;
	@Expose
	private String district;
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
