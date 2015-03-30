package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallAddRecommendRequest extends AutoFillPacketRequest {

	public String	commentPicture;	//推荐商品图片地址
	public String	recDescription;	//推荐商品描述
	public String	productID;		//推荐商品id（可为空）

	public MallAddRecommendRequest() {
		super(HttpDefine.MallAddRecommend);
	}

	public String getCommentPicture() {
		return commentPicture;
	}

	public void setCommentPicture(String commentPicture) {
		this.commentPicture = commentPicture;
	}

	public String getRecDescription() {
		return recDescription;
	}

	public void setRecDescription(String recDescription) {
		this.recDescription = recDescription;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

}
