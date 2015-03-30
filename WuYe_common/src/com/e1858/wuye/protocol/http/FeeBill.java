package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class FeeBill implements Serializable {
	@Expose
	private String				ID			= "";
	@Expose
	private String				edescription;					//描述
	@Expose
	private float				amount;						//金额
	@Expose
	private String				createTime;
	@Expose
	private int					state;							//1已支付、2未支付、3已关闭
	@Expose
	private String				paymentType	= "无";
	@Expose
	private FeeType				feeType		= new FeeType();
	@Expose
	private String				orderNumber	= "";
	@Expose
	private String				houseRoom;
	@Expose
	private PaymentParamBean	paymentParam;					//		支付参数

	public FeeType getFeeType() {
		return feeType;
	}

	public void setFeeType(FeeType feeType) {
		this.feeType = feeType;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getEdescription() {
		return edescription;
	}

	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getHouseRoom() {
		return houseRoom;
	}

	public void setHouseRoom(String houseRoom) {
		this.houseRoom = houseRoom;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public PaymentParamBean getPaymentParam() {
		return paymentParam;
	}

	public void setPaymentParam(PaymentParamBean paymentParam) {
		this.paymentParam = paymentParam;
	}

}
