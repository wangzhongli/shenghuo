package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallDeleteRecommendRequest extends AutoFillPacketRequest {
	public String	recommendID;

	public MallDeleteRecommendRequest() {
		super(HttpDefine.MallDeleteRecommend);
	}

	public String getRecommendID() {
		return recommendID;
	}

	public void setRecommendID(String recommendID) {
		this.recommendID = recommendID;
	}

}
