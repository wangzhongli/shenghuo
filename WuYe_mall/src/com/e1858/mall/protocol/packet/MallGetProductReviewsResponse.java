package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.bean.MallProductReviewBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetProductReviewsResponse extends PacketResp {

	private List<MallProductReviewBean>	reviews;	//	
	private int							totalCount;

	public MallGetProductReviewsResponse() {}

	public List<MallProductReviewBean> getReviews() {
		return reviews;
	}

	public void setReviews(List<MallProductReviewBean> reviews) {
		this.reviews = reviews;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
