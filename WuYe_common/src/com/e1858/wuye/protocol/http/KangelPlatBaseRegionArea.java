package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class KangelPlatBaseRegionArea implements Serializable {

	@Expose
	private int areaID;
	@Expose
	private int regionID;
	@Expose
	private String areaName;
	@Expose
	private String edescription;
	public int getAreaID() {
		return areaID;
	}
	public void setAreaID(int areaID) {
		this.areaID = areaID;
	}
	public int getRegionID() {
		return regionID;
	}
	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getEdescription() {
		return edescription;
	}
	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}
	
	
}
