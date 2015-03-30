package com.e1858.mall.protocol.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MallComment implements Serializable {
	private String	commentID;			//评论ID
	private String	commentName;		//评论用户昵称
	private String	recommendID;		//推荐ID
	private String	commentDescription; //评论描述
	private String	commentIcon;		//评论用户头像图片链接
	private String	commentTime;		//评论时间

	public MallComment() {}

	public String getCommentID() {
		return commentID;
	}

	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}

	public String getCommentName() {
		return commentName;
	}

	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}

	public String getRecommendID() {
		return recommendID;
	}

	public void setRecommendID(String recommendID) {
		this.recommendID = recommendID;
	}

	public String getCommentDescription() {
		return commentDescription;
	}

	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}

	public String getCommentIcon() {
		return commentIcon;
	}

	public void setCommentIcon(String commentIcon) {
		this.commentIcon = commentIcon;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getID() {
		return commentID;
	}

}
