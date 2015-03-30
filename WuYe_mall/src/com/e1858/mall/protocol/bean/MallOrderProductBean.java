package com.e1858.mall.protocol.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MallOrderProductBean implements Serializable {
	public String					ID;			//	string	项目ID
	public String					productID;		//	string	商品ID
	public String					thumbUrl;		//	String	列表展示缩略图url
	public String					name;			//	string	商品名字
	public float					price;			//	float	商品价格
	public String					introduce;		//string	商品介绍
	public String					skuID;			//	string	MallSKU.ID
	public String					skuProperties;	//	string	商品属性描述,显示用的
	public int						quantity;		//int	数量
	public String					createTime;	//	string	创建时间
	public String					modifyTime;	//string	最后修改时间
	public String					shopID;
	public String					cartProductID;
	public MallRefundRecordBean		refund;		//	退换货,没有则为null
	public MallProductReviewBean	review;		//	这个商品的评价[]

	public MallProductReviewBean getReview() {
		return review;
	}

	public void setReview(MallProductReviewBean review) {
		this.review = review;
	}

	public String getCartProductID() {
		return cartProductID;
	}

	public void setCartProductID(String cartProductID) {
		this.cartProductID = cartProductID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public MallRefundRecordBean getRefund() {
		return refund;
	}

	public void setRefund(MallRefundRecordBean refund) {
		this.refund = refund;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getSkuProperties() {
		return skuProperties;
	}

	public void setSkuProperties(String skuProperties) {
		this.skuProperties = skuProperties;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getSkuID() {
		return skuID;
	}

	public void setSkuID(String skuID) {
		this.skuID = skuID;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

}
