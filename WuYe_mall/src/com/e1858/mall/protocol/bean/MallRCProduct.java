package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MallRCProduct implements Serializable {
	private String			recProductID;	//所推荐的商品ID
	private String			thumbUrl;		//列表展示缩略图url
	private String			productName;	//商品名称
	private List<String>	imageUrls;		//商品展示图片数组
	private String			categoryName;	//所属分类名字
	private String			detailUrl;		//商品详情web页面

	public MallRCProduct() {
		super();
	}

	public String getRecProductID() {
		return recProductID;
	}

	public void setRecProductID(String recProductID) {
		this.recProductID = recProductID;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getID() {
		return recProductID;
	}

}
