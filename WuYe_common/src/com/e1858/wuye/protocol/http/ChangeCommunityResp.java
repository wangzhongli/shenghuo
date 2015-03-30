package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ChangeCommunityResp  extends PacketResp{
	@Expose
	private Community  community; //切换小区成功后返回新小区信息
	@Expose
	private UserHouse userHouse;
	public ChangeCommunityResp(){
		command=HttpDefine.CHANGE_COMMUNITY_RESP;
	}
	public Community getCommunity() {
		return community;
	}
	public void setCommunity(Community community) {
		this.community = community;
	}
	public UserHouse getUserHouse() {
		return userHouse;
	}
	public void setUserHouse(UserHouse userHouse) {
		this.userHouse = userHouse;
	}

}
