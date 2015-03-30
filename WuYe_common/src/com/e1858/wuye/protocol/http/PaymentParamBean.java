package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class PaymentParamBean implements Serializable {
	@Expose
	private String	alipay;	//	支付宝参数
	@Expose
	private String	cebbank;	//	光大参数
	@Expose
	private String	unionpay;	//	银联参数

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getCebbank() {
		return cebbank;
	}

	public void setCebbank(String cebbank) {
		this.cebbank = cebbank;
	}

	public String getUnionpay() {
		return unionpay;
	}

	public void setUnionpay(String unionpay) {
		this.unionpay = unionpay;
	}

}
