package com.e1858.wuye.protocol.http;

import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommunityByLocationResp extends PacketResp {
	@Expose
	private List<Community> communities;
	public GetCommunityByLocationResp(){
		command=HttpDefine.GET_COMMUNITY_BYLOCATION_RESP;
	}
	public List<Community> getCommunities() {
		return communities;
	}
	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}
}
