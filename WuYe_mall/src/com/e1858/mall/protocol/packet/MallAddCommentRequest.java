package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallComment;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

/**
 * @author momo
 *增加评论接口
 */
@SuppressWarnings("serial")
public class MallAddCommentRequest extends AutoFillPacketRequest {

	public MallComment	mallComment;//评论实体类

	public MallAddCommentRequest() {
		super(HttpDefine.MallAddComment);
	}

	public MallComment getMallComment() {
		return mallComment;
	}

	public void setMallComment(MallComment mallComment) {
		this.mallComment = mallComment;
	}
	
}
