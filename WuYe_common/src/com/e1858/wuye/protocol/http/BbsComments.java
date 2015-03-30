package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class BbsComments implements Serializable
{
	@Expose
	private int ID = -1;// 帖子ID，对应t_BbsPost中的ID
	@Expose
	private String title = "";// 帖子标题
	@Expose
	private String content = "";// 帖子内容
	@Expose
	private String createTime = "";// 发送时间
	@Expose
	private int creatorID= -1;// 发帖人
	@Expose
	private String creatorNickname = "";// 发帖人昵称
	@Expose
	private String headPortrait = "";// 发帖人头像
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getCreatorID() {
		return creatorID;
	}
	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}
	public String getCreatorNickname() {
		return creatorNickname;
	}
	public void setCreatorNickname(String creatorNickname) {
		this.creatorNickname = creatorNickname;
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

}
