package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class KangelPlatBaseRegion implements Serializable {

	@Expose
	private int regionID;//区域ID
	@Expose
	private String province;//省
	@Expose
	private String city;//市
	@Expose
	private String district;//区
	public int getRegionID() {
		return regionID;
	}
	public void setRegionID(int regionID) {
		this.regionID = regionID;
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
