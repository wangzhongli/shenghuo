package com.e1858.mall.protocol.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MallRecommend implements Serializable {
	private String	recommendID;		//推荐ID
	private String	productName;		//推荐商品名称
	private String	productID;			//推荐商品ID
	private String	recDescription;	//推荐商品描述
	private String	productUrl;		//推荐商品网址链接
	private String	productIcon;		//推荐商品图片链接
	private String	recommendTime;		//推荐时间
	private String	recommendUserName;	//推荐用户名字
	private String	recommendUserIcon;	//推荐用户头像
	private int		recommendUserID;	//推荐用户ID
	private int		commentCount;		//评论数目
	private int		heartCount;		//点赞数目

	public MallRecommend() {}

	public String getRecommendID() {
		return recommendID;
	}

	public void setRecommendID(String recommendID) {
		this.recommendID = recommendID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getRecDescription() {
		return recDescription;
	}

	public void setRecDescription(String recDescription) {
		this.recDescription = recDescription;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String getProductIcon() {
		return productIcon;
	}

	public void setProductIcon(String productIcon) {
		this.productIcon = productIcon;
	}

	public String getRecommendTime() {
		return recommendTime;
	}

	public void setRecommendTime(String recommendTime) {
		this.recommendTime = recommendTime;
	}

	public String getRecommendUserName() {
		return recommendUserName;
	}

	public void setRecommendUserName(String recommendUserName) {
		this.recommendUserName = recommendUserName;
	}

	public String getRecommendUserIcon() {
		return recommendUserIcon;
	}

	public void setRecommendUserIcon(String recommendUserIcon) {
		this.recommendUserIcon = recommendUserIcon;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getHeartCount() {
		return heartCount;
	}

	public void setHeartCount(int heartCount) {
		this.heartCount = heartCount;
	}

	public int getRecommendUserID() {
		return recommendUserID;
	}

	public void setRecommendUserID(int recommendUserID) {
		this.recommendUserID = recommendUserID;
	}

	public String getID() {
		return recommendID;
	}

}
