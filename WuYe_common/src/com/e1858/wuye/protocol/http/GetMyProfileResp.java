package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetMyProfileResp extends PacketResp
{
	@Expose
	private UserInfo userInfo;
	public GetMyProfileResp()
	{
		command = HttpDefine.GET_MY_PROFILE_RESP;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}


}
