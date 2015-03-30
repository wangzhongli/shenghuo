package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetVerifyCode extends PacketRequest{
	@Expose
	private String mobile;//手机号
	@Expose
	private String deviceToken;
	@Expose
	private int actionType;

	public GetVerifyCode(){
		command = HttpDefine.GET_VERIFY_CODE;
	}
	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

}
