package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ChangeHouseResp  extends PacketResp{

	@Expose
	private int roomID	= 0;//切换成功返回门牌号ID，不成功为-1

	public ChangeHouseResp(){
		command=HttpDefine.CHANGE_HOUSE_RESP;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

}
