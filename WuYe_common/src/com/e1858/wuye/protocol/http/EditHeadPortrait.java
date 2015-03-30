package com.e1858.wuye.protocol.http;
import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class EditHeadPortrait extends PacketRequest
{
	@Expose
	private String headPortrait;//头像

	public EditHeadPortrait()
	{
		command = HttpDefine.EDIT_HEADPORTRAIT;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	
	
}
