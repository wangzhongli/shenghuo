package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddFeedBack extends PacketRequest {
	@Expose
	private String content;
	@Expose
	private String mobile;
	@Expose
	private Boolean anonymous=false;//0匿名 1 非匿名
	public AddFeedBack(){
		command=HttpDefine.ADD_FEEDBACK;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Boolean getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(Boolean anonymous) {
		this.anonymous = anonymous;
	}
	
}
