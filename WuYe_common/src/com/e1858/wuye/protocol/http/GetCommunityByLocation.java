package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommunityByLocation extends PacketRequest {
	@Expose
	private double latitude;
	@Expose
	private double longitute;
	@Expose
	private String city;
	public GetCommunityByLocation(){
		command=HttpDefine.GET_COMMUNITY_BYLOCATION;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitute() {
		return longitute;
	}
	public void setLongitute(double longitute) {
		this.longitute = longitute;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
}
