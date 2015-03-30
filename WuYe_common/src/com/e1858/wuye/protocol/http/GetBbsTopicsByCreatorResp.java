package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetBbsTopicsByCreatorResp  extends PacketResp{
	@Expose
	List<BbsTopic>  bbsTopics;//小区论坛板块列表
	@Expose
	private int topicCount=0;
	@Expose
	private int commentCount=0;
	public GetBbsTopicsByCreatorResp(){
		command = HttpDefine.GET_BBS_TOPICS_BY_CREATOR_RESP;
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
	public int getTopicCount() {
		return topicCount;
	}
	public void setTopicCount(int topicCount) {
		this.topicCount = topicCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
}
