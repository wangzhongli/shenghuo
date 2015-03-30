package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallAddHeartRequest extends AutoFillPacketRequest {

	public String		recommendID;
	public int			heartFlag;			//	0点赞，1取消（暂留）

	public MallAddHeartRequest() {
		super(HttpDefine.MallAddHeart);
	}

	public String getRecommendID() {
		return recommendID;
	}

	public void setRecommendID(String recommendID) {
		this.recommendID = recommendID;
	}

	public int getHeartFlag() {
		return heartFlag;
	}

	public void setHeartFlag(int heartFlag) {
		this.heartFlag = heartFlag;
	}
	
}
