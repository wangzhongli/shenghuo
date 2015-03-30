package com.e1858.mall.protocol.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MallSKUBean implements Serializable {
	public String	ID;		//	string	单品ID
	public String	productID;	//	string	商品ID
	public String	properties; //	string	sku的销售属性组合字符串（颜色，大小，等等）,格式是p1:v1;p2:v2例如 1243:1215;5626:5125
	//p1指的是MallProductProperty.ID, v1指的是MallProductPropertyValue.ID
	public String	quantity;	//	int	单品库存
	public float	price;		//	float	单品价格
	public String	createTime; //	string	创建时间
	public String	modifyTime; //string	最后修改时间
	public String	imageUrl;	//	string	单品展示图片一张

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
