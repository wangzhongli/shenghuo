package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class AddComplaint extends PacketRequest {

	@Expose
	private Boolean	anonymous	= false;	//是否匿名 1:匿名 其他：不匿名
	@Expose
	private String	content		= "";		//投诉建议内容
	@Expose
	private String	phone;					// 联系电话

	@Expose
	private String	name;					// 联系人

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AddComplaint() {
		command = HttpDefine.ADD_COMPLAINT;
	}

	public Boolean getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(Boolean anonymous) {
		this.anonymous = anonymous;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
