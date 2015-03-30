package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MallProductReviewBean implements Serializable {
	public String					ID;					//	string	分类ID
	public int						creatorID;				//	int	评价人ID	匿名评价的话这个字段-1
	public String					creatorName;			//string	评价人名字
	public String					creatorHeadPortrait;	//string	评价人头像url
	public String					createTime;			//string	评价日期
	public String					content;				//	string	评价内容

	public float					ratingDescription;		//	描述符合星级评分,满分5.0
	public float					ratingQuality;			//float	商品质量星级评分,满分5.0
	public float					ratingSpeed;			//float	发货速度星级评分,满分5.0

	public List<String>				pictureUrls;			//	String数组	评价的图片数组
	public MallProductReviewBean	appendReview;			//	MallProductReview	追加的评价
	public String					orderProductID;		//	MallOrderProduct.ID	评价的订单内商品ID

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorHeadPortrait() {
		return creatorHeadPortrait;
	}

	public void setCreatorHeadPortrait(String creatorHeadPortrait) {
		this.creatorHeadPortrait = creatorHeadPortrait;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getRating() {
		return (ratingDescription + ratingQuality + ratingSpeed) / 3.f;
	}

	public float getRatingDescription() {
		return ratingDescription;
	}

	public void setRatingDescription(float ratingDescription) {
		this.ratingDescription = ratingDescription;
	}

	public float getRatingQuality() {
		return ratingQuality;
	}

	public void setRatingQuality(float ratingQuality) {
		this.ratingQuality = ratingQuality;
	}

	public float getRatingSpeed() {
		return ratingSpeed;
	}

	public void setRatingSpeed(float ratingSpeed) {
		this.ratingSpeed = ratingSpeed;
	}

	public List<String> getPictureUrls() {
		return pictureUrls;
	}

	public void setPictureUrls(List<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

	public MallProductReviewBean getAppendReview() {
		return appendReview;
	}

	public void setAppendReview(MallProductReviewBean appendReview) {
		this.appendReview = appendReview;
	}

	public String getOrderProductID() {
		return orderProductID;
	}

	public void setOrderProductID(String orderProductID) {
		this.orderProductID = orderProductID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + creatorID;
		result = prime * result + ((orderProductID == null) ? 0 : orderProductID.hashCode());
		result = prime * result + ((pictureUrls == null) ? 0 : pictureUrls.hashCode());
		result = prime * result + Float.floatToIntBits(ratingDescription);
		result = prime * result + Float.floatToIntBits(ratingQuality);
		result = prime * result + Float.floatToIntBits(ratingSpeed);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MallProductReviewBean other = (MallProductReviewBean) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (creatorID != other.creatorID)
			return false;
		if (orderProductID == null) {
			if (other.orderProductID != null)
				return false;
		} else if (!orderProductID.equals(other.orderProductID))
			return false;
		if (pictureUrls == null) {
			if (other.pictureUrls != null)
				return false;
		} else if (!pictureUrls.equals(other.pictureUrls))
			return false;
		if (Float.floatToIntBits(ratingDescription) != Float.floatToIntBits(other.ratingDescription))
			return false;
		if (Float.floatToIntBits(ratingQuality) != Float.floatToIntBits(other.ratingQuality))
			return false;
		if (Float.floatToIntBits(ratingSpeed) != Float.floatToIntBits(other.ratingSpeed))
			return false;
		return true;
	}

}
