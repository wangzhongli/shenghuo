package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class MallConfirmOrderBean implements Serializable {
	public String						shopID;
	public List<MallOrderProductBean>	orderProducts;
	public String						remark;			//	string	备注
	public float						shippingCost;

	@Expose(serialize = false)
	public String						shopName;
	@Expose(serialize = false)
	public int							sortIndex	= 0;

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<MallOrderProductBean> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<MallOrderProductBean> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

}
