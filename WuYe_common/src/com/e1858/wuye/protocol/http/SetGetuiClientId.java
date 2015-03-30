package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class SetGetuiClientId extends PacketRequest{	
	@Expose
	private int userID=-1;
	@Expose
	private String clientID="";
	@Expose
	private String deviceToken="";
	@Expose
	private int deviceType=1;//android 1 ios 2

	public SetGetuiClientId(){
		command = HttpDefine.SET_GETUI_CLIENTID;
	}
	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}


	public String getClientID() {
		return clientID;
	}


	public void setClientID(String clientID) {
		this.clientID = clientID;
	}


	public String getDeviceToken() {
		return deviceToken;
	}


	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}


	public int getDeviceType() {
		return deviceType;
	}


	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

}
