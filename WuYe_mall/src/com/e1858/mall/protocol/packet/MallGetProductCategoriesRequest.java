package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallGetProductCategoriesRequest extends AutoFillPacketRequest {

	private String	categoryID; //	String 	MallProductCategory.ID 获取大类是null

	public MallGetProductCategoriesRequest() {
		super(HttpDefine.MallGetProductCategories);
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

}
