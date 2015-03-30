package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommunityByAreaResp  extends PacketResp{
	@Expose
	List<Community>  communities;//小区列表
	public GetCommunityByAreaResp(){
		command = HttpDefine.GET_COMMUNITY_BY_AREA_RESP;
		communities=new ArrayList<Community>();
	}
	public List<Community> getCommunities() {
		return communities;
	}
	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}

}
