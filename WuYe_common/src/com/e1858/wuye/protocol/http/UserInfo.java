package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class UserInfo  implements Serializable
{
	@Expose
	private String headPortrait;
	@Expose
	private String nickname;
	@Expose
	private String name;
	@Expose
	private String mobile;
	@Expose
	private String email;
	@Expose
	private String carNumber;
	
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
