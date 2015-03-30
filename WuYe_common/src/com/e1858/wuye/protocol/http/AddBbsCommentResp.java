package com.e1858.wuye.protocol.http;


import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddBbsCommentResp  extends PacketResp{
	@Expose
	private int ID=-1;//增加成功后返回新增的ID；不成功为-1；
	public AddBbsCommentResp(){
		command = HttpDefine.ADD_BBS_COMMENT_RESP;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
}
