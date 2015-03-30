package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallAddProductReviewResponse extends PacketResp {

	private List<String>	IDs;
	private int				points; //	int	评价获得的积分

	public MallAddProductReviewResponse() {}

	public List<String> getIDs() {
		return IDs;
	}

	public void setIDs(List<String> iDs) {
		IDs = iDs;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
