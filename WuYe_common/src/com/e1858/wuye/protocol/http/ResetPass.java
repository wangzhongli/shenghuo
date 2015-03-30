package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ResetPass extends PacketRequest{

	@Expose
	private String mobile;//手机号
	@Expose
	private String verify;//验证码
	@Expose 
	private String password;
	
	public ResetPass(){
		command = HttpDefine.RESET_PASS;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}




}
