package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MallProductBean implements Serializable {
	public String							ID;			//	string	分类ID
	public String							thumbUrl;		//	String	列表展示缩略图url
	public String							name;			//	String	商品名称
	public List<MallProductTagBean>			tags;			//	MallProductTag数组	商品标签数组
	public float							price;			//	float	价格
	public float							priceBase;		//	float	价格
	public int								quantity;		//	int	总库存(必须是sku.quantity的总和)
	public int								saleCount;		//	int	总销量
	public String							introduce;		//	string	介绍说明(例如:来自澳大利亚)
	public List<String>						imageUrls;		//	String数组	商品展示图片数组
	public List<MallProductPropertyBean>	properties;	//	MallProductProperty数组	属性规格数组
	public List<MallSKUBean>				skus;			//	MallSKU数组	商品库存单位数组(若无规格分类等,此项为空)
	public String							categoryName;	//所属分类名字
	public String							categoryID;	//	string	商品类ID(必须是叶子类)
	public String							detailUrl;		//	string	商品详情web页面
	public String							shopID;
	public String							shopName;

	public float getPriceBase() {
		return priceBase;
	}

	public void setPriceBase(float priceBase) {
		this.priceBase = priceBase;
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

	public void setID(String iD) {
		ID = iD;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MallProductTagBean> getTags() {
		return tags;
	}

	public void setTags(List<MallProductTagBean> tags) {
		this.tags = tags;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public List<MallProductPropertyBean> getProperties() {
		return properties;
	}

	public void setProperties(List<MallProductPropertyBean> properties) {
		this.properties = properties;
	}

	public List<MallSKUBean> getSkus() {
		return skus;
	}

	public void setSkus(List<MallSKUBean> skus) {
		this.skus = skus;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

}
