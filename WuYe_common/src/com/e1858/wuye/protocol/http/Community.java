package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Community implements Serializable{
	@Expose
	private int ID=-1;//小区ID，对应t_Community中的ID
	@Expose
	private String address;
	@Expose
	private String name="";
	@Expose
	private String city="";
	@Expose
	private String province="";
	@Expose
	private String district="";
	@Expose
	private CommunityInfo communityInfo=new CommunityInfo();
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CommunityInfo getCommunityInfo() {
		return communityInfo;
	}
	public void setCommunityInfo(CommunityInfo communityInfo) {
		this.communityInfo = communityInfo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	
}
