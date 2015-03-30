package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetBbsTopicsResp  extends PacketResp{
	@Expose
	List<BbsTopic>  bbsTopics;//小区论坛板块列表
	public GetBbsTopicsResp(){
		command = HttpDefine.GET_BBS_TOPICS_RESP;
		bbsTopics=new ArrayList<BbsTopic>();
	}
	public List<BbsTopic> getBbsTopics()
	{
		return bbsTopics;
	}
	public void setBbsTopics(List<BbsTopic> bbsTopics)
	{
		this.bbsTopics = bbsTopics;
	}
	

}
