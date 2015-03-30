package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallProductReviewBean;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallAddProductReviewRequest extends AutoFillPacketRequest {

	public List<MallProductReviewBean>	reviews;		//	
	public boolean						anonymous;		//	Boolean	是否匿名
	public int							reviewIndex;	//	int	第几次评价,从0计数

	public int getReviewIndex() {
		return reviewIndex;
	}

	public void setReviewIndex(int reviewIndex) {
		this.reviewIndex = reviewIndex;
	}

	public MallAddProductReviewRequest() {
		super(HttpDefine.MallAddProductReview);
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public List<MallProductReviewBean> getReviews() {
		return reviews;
	}

	public void setReviews(List<MallProductReviewBean> reviews) {
		this.reviews = reviews;
	}

}
