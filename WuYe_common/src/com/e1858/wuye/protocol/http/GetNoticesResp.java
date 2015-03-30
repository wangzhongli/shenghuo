package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetNoticesResp  extends PacketResp{
	@Expose
	List<Notice>  notices;//小区列表（注意：这个List中Notice对象中的content为空串）
	public GetNoticesResp(){
		command = HttpDefine.GET_NOTICES_SELF_RESP;
		notices=new ArrayList<Notice>();
	}
	public List<Notice> getNotices()
	{
		return notices;
	}
	public void setNotices(List<Notice> notices)
	{
		this.notices = notices;
	}

}
