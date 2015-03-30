package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ChangePassword extends PacketRequest {

	@Expose
	private String mobile;
	@Expose
	private String oldPassword;
	@Expose
	private String enewPassword;
	
	public ChangePassword(){
		command=HttpDefine.CHANGE_PASSWORD;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return enewPassword;
	}

	public void setNewPassword(String newPassword) {
		this.enewPassword = newPassword;
	}
	
	
}
