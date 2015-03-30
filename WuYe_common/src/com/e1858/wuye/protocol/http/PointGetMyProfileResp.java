package com.e1858.wuye.protocol.http;

public class PointGetMyProfileResp extends PacketResp {

	PointProfileBean	myProfile;	//	积分信息

	public PointGetMyProfileResp() {
		command = HttpDefine.PointGetMyProfile_RESP;
	}

	public PointProfileBean getMyProfile() {
		return myProfile;
	}

	public void setMyProfile(PointProfileBean myProfile) {
		this.myProfile = myProfile;
	}

}
