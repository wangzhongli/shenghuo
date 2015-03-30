package com.e1858.wuye.protocol.http;
import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class EditNickname extends PacketRequest
{
	@Expose
	private String nickname;//头像

	public EditNickname()
	{
		command = HttpDefine.EDIT_NICKNAME;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	
	
	
}
