package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.common.Constant;
import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallGeneratePaymentOrderRequest extends AutoFillPacketRequest {

	public List<String>	orderIDs;						//

	public String		mobileOS	= Constant.ANDROID; //		WinPhone/ANDROID/IOS

	public MallGeneratePaymentOrderRequest() {
		super(HttpDefine.MallGeneratePaymentOrder);
	}

	public List<String> getOrderIDs() {
		return orderIDs;
	}

	public void setOrderIDs(List<String> orderIDs) {
		this.orderIDs = orderIDs;
	}

	public String getMobileOS() {
		return mobileOS;
	}

	public void setMobileOS(String mobileOS) {
		this.mobileOS = mobileOS;
	}

}
