package com.e1858.wuye.protocol.http;
import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class EditMyProfile extends PacketRequest
{
	@Expose
	String name;// 姓名
	@Expose
	String mobile;// 手机
	@Expose
	String email;// 邮箱
	@Expose
	String carNumber;// 车牌号

	public EditMyProfile()
	{
		command = HttpDefine.EDIT_MY_PROFILE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

}
