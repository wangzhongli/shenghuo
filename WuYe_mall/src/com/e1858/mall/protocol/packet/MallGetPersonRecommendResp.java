package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallRecommend;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetPersonRecommendResp extends PacketResp {
	public List<MallRecommend>	mallRecommends; 
	public List<String>	myClickHearts; 

	public MallGetPersonRecommendResp() {
		command = HttpDefine.MallGetPersonRecommend_RESP;
	}

	public List<MallRecommend> getMallRecommends() {
		return mallRecommends;
	}

	public void setMallRecommends(List<MallRecommend> mallRecommends) {
		this.mallRecommends = mallRecommends;
	}

	public List<String> getMyClickHearts() {
		return myClickHearts;
	}

	public void setMyClickHearts(List<String> myClickHearts) {
		this.myClickHearts = myClickHearts;
	}
	

}
