package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallRefundRequest extends AutoFillPacketRequest {

	public String		ID;
	public String		orderProductID; //
	public int			type;			//	1.退货2.换货
	public String		reason;		//	string	退换原因
	public String		edescription;	//详细描述
	public List<String>	imageUrls;		//图片链接数组

	public MallRefundRequest() {
		super(HttpDefine.MallRefund);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getOrderProductID() {
		return orderProductID;
	}

	public void setOrderProductID(String orderProductID) {
		this.orderProductID = orderProductID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEdescription() {
		return edescription;
	}

	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

}
