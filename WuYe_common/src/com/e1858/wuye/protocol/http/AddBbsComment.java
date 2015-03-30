package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class AddBbsComment extends PacketRequest{	

	@Expose
	private long topicID=-1;//话题ID
	@Expose
	private String title="";//帖子标题
	@Expose
	private String content="";//帖子内容


	public AddBbsComment(){
		command = HttpDefine.ADD_BBS_COMMENT;
	}


	public long getTopicID() {
		return topicID;
	}


	public void setTopicID(long topicID) {
		this.topicID = topicID;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


}
