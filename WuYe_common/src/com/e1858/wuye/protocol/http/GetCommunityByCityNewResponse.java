package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetCommunityByCityNewResponse  extends PacketResp{
	@Expose
	List<Community>  communities;//小区列表
	public GetCommunityByCityNewResponse(){
		command = HttpDefine.GETCOMMUNITYBYCITYNEW_RESP;
		communities=new ArrayList<Community>();
	}
	public List<Community> getCommunities() {
		return communities;
	}
	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}

}
