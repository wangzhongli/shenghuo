package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class BbsTopic implements Serializable
{
	@Expose
	private int ID = -1;// 话题ID
	@Expose
	private String title="";
	@Expose
	private String content="";
	@Expose
	private int viewCount = 0;// 浏览数
	@Expose
	private int commentCount = 0;// 回复数
	@Expose
	private int topCount = 0;// 顶数
	@Expose
	private Boolean isCream =false;// 是否精华话题  true：是
	@Expose
	private Boolean isTop = false;// 是否置顶话题  true：是
	@Expose
	private Boolean disable = false;// 是否禁止发帖  true：禁止
	@Expose
	private String lastCommentTime = "";// 最后回复时间
	@Expose
	private String createTime="";// 主贴子
	@Expose 
	private int creatorID=-1;
	@Expose
	private String creatorNickname="";
	@Expose
	private String headPortrait="";
	@Expose
	private List<String> images=new ArrayList<String>();
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
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getTopCount() {
		return topCount;
	}
	public void setTopCount(int topCount) {
		this.topCount = topCount;
	}

	public Boolean getIsCream() {
		return isCream;
	}
	public void setIsCream(Boolean isCream) {
		this.isCream = isCream;
	}
	public Boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
	public String getLastCommentTime() {
		return lastCommentTime;
	}
	public void setLastCommentTime(String lastCommentTime) {
		this.lastCommentTime = lastCommentTime;
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
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}

}
