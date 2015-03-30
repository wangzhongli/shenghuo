package com.e1858.wuye.protocol.http;
@SuppressWarnings("serial")
public class EditNicknameResp extends PacketResp {

	public EditNicknameResp(){
		command=HttpDefine.EDIT_NICKNAME_RESP;
	}
}
