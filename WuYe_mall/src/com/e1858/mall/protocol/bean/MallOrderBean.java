package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MallOrderBean implements Serializable {
	public String						ID;						//	string	订单ID
	public String						orderSn;					//	string	订单编号
	public MallManagedAddressBean		buyerAddress;				//	MallManagedAddress	买家地址
	public String						sellerNickname;			//	string	卖家名字
	public String						sellerMobile;				//	string	卖家电话
	public String						sellerAddress;				//	string	卖家地址
	public float						shippingCost;				//	Float	运费
	public float						amount;					//	Float	金额
	public List<MallOrderStatusBean>	orderStatus;				//	MallOrderStatus数组	倒叙排序的状态数组
	public String						remark;					//	string	备注
	public String						appointmentShippingTime;	//	string	预约送货时间
	public List<MallOrderProductBean>	orderProducts;				//	数组	
	public String						createTime;				//	string	预约送货时间
	public String						shopID;					//	
	public String						shopName;					//

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getID() {
		return ID;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public MallManagedAddressBean getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(MallManagedAddressBean buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getSellerNickname() {
		return sellerNickname;
	}

	public void setSellerNickname(String sellerNickname) {
		this.sellerNickname = sellerNickname;
	}

	public String getSellerMobile() {
		return sellerMobile;
	}

	public void setSellerMobile(String sellerMobile) {
		this.sellerMobile = sellerMobile;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public List<MallOrderStatusBean> getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(List<MallOrderStatusBean> orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAppointmentShippingTime() {
		return appointmentShippingTime;
	}

	public void setAppointmentShippingTime(String appointmentShippingTime) {
		this.appointmentShippingTime = appointmentShippingTime;
	}

	public List<MallOrderProductBean> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<MallOrderProductBean> orderProducts) {
		this.orderProducts = orderProducts;
	}

}
