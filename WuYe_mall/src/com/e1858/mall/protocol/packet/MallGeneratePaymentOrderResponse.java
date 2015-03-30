package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.PacketResp;
import com.e1858.wuye.protocol.http.PaymentParamBean;

@SuppressWarnings("serial")
public class MallGeneratePaymentOrderResponse extends PacketResp {
	public String			ID;			//	String	付款订单的ID
	public PaymentParamBean	paymentParam;	//	String	付款参数

	public MallGeneratePaymentOrderResponse() {
		command = HttpDefine.MallGeneratePaymentOrder_RESP;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public PaymentParamBean getPaymentParam() {
		return paymentParam;
	}

	public void setPaymentParam(PaymentParamBean paymentParam) {
		this.paymentParam = paymentParam;
	}

}
