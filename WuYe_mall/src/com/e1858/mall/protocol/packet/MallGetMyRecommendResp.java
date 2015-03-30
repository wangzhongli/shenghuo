package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallRecommend;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetMyRecommendResp extends PacketResp {

	public List<MallRecommend>	recommends;		//所有的推荐的数组
	public int					heartNumToMe;	//别人对我的点赞数量
	public int					comNumToMe;		//别人对我的评价数量

	public MallGetMyRecommendResp() {
		command = HttpDefine.MallGetMyRecommend_RESP;
	}

	public List<MallRecommend> getRecommends() {
		return recommends;
	}

	public void setRecommends(List<MallRecommend> recommends) {
		this.recommends = recommends;
	}

	public int getHeartNumToMe() {
		return heartNumToMe;
	}

	public void setHeartNumToMe(int heartNumToMe) {
		this.heartNumToMe = heartNumToMe;
	}

	public int getComNumToMe() {
		return comNumToMe;
	}

	public void setComNumToMe(int comNumToMe) {
		this.comNumToMe = comNumToMe;
	}

}
