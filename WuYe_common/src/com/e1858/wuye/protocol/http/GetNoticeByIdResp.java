package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetNoticeByIdResp  extends PacketResp{
	@Expose
	Notice  notice;//通知详细信息
	public GetNoticeByIdResp(){
		command = HttpDefine.READ_NOTICE_BY_ID_RESP;
	}
	public Notice getNotice()
	{
		return notice;
	}
	public void setNotice(Notice notice)
	{
		this.notice = notice;
	}


}
