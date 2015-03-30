package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class LoginResp extends PacketResp
{
	@Expose
	private String key;
	@Expose
	private int userID;
	@Expose
	private Community community;
	@Expose
	private UserInfo userInfo;
	@Expose
	private UserHouse userHouse;
	
	public LoginResp()
	{
		command = HttpDefine.LOGIN_RESP;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public UserHouse getUserHouse() {
		return userHouse;
	}

	public void setUserHouse(UserHouse userHouse) {
		this.userHouse = userHouse;
	}

}
