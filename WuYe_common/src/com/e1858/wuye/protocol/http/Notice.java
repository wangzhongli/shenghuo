package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Notice implements Serializable{
	@Expose
	private int ID=-1;//通知ID，对应t_NoticeReceiver中的ID
	@Expose
	private String communityName="";//小区名称
	@Expose
	private String title="";//通知标题，对应t_Notice中的Title
	@Expose
	private String content="";	//通知内容，对应t_Notice中的Content
	@Expose
	private String sendTime="";//发送时间，对应t_NoticeReceiver中的SendTime
	@Expose
	private String readTime="";//发送时间，对应t_NoticeReceiver中的SendTime
	@Expose
	private int viewCount;

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
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
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getReadTime()
	{
		return readTime;
	}
	public void setReadTime(String readTime)
	{
		this.readTime = readTime;
	}
	
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	
}
