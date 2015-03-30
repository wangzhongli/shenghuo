package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallGetProductsRequest extends AutoFillPacketRequest {
	public int		offset;
	public int		count;
	public String	keyword;
	public String	categoryID;
	public String	orderby;	//String	排序方式和名字组合,例如	price|asc	price|desc	saleCount|asc	saleCount[优化修改添加字段]|desc

	public String	tagID;

	public String getTagID() {
		return tagID;
	}

	public void setTagID(String tagID) {
		this.tagID = tagID;
	}

	public MallGetProductsRequest() {
		super(HttpDefine.MallGetProducts);
	}

	public int getOffset() {
		return offset;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

}
