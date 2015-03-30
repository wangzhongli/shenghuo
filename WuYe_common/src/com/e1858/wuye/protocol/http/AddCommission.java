package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class AddCommission extends PacketRequest {

	@Expose
	private int		typeID	= -1;	//代办类型Id
	@Expose
	private String	content	= "";	//代办内容

	@Expose
	private String	phone;			// 联系电话

	@Expose
	private String	name;			// 联系人

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

	public AddCommission() {
		command = HttpDefine.ADD_COMMISSION;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
