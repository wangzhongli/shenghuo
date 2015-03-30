package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallComment;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetCommentResp extends PacketResp {

	public List<MallComment>	mallComments;	//评论条数

	public MallGetCommentResp() {
		command = HttpDefine.MallGetComment_RESP;
	}

	public List<MallComment> getMallComments() {
		return mallComments;
	}

	public void setMallComments(List<MallComment> mallComments) {
		this.mallComments = mallComments;
	}

}
