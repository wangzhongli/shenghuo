package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetVerifyCodeResp  extends PacketResp{
	@Expose
	private String verify;
	public GetVerifyCodeResp(){
		command = HttpDefine.GET_VERIFY_CODE_RESP;
	}
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	
}
