package com.e1858.wuye.protocol.http;

import java.util.List;

import com.google.gson.annotations.Expose;

public class KangelGetPlatBaseRegionResp extends PacketResp {

	public KangelGetPlatBaseRegionResp(){
		command=HttpDefine.KANGEL_GET_PLATBASE_REGIN_RESP;
	}
	@Expose
	private KangelPlatBaseRegion platBaseRegion;
	@Expose
	private List<KangelPlatBaseRegionArea> platBaseRegionAreas;
	
	public KangelPlatBaseRegion getPlatBaseRegion() {
		return platBaseRegion;
	}
	public void setPlatBaseRegion(KangelPlatBaseRegion platBaseRegion) {
		this.platBaseRegion = platBaseRegion;
	}
	public List<KangelPlatBaseRegionArea> getPlatBaseRegionAreas() {
		return platBaseRegionAreas;
	}
	public void setPlatBaseRegionAreas(
			List<KangelPlatBaseRegionArea> platBaseRegionAreas) {
		this.platBaseRegionAreas = platBaseRegionAreas;
	}
	
}
