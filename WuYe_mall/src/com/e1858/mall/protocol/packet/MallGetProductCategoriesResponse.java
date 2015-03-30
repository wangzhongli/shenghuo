package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetProductCategoriesResponse extends PacketResp {

	private List<MallProductCategoryBean>	categories; //	MallProductCategory数组	分类数组

	public MallGetProductCategoriesResponse() {
		command = HttpDefine.MallGetProductCategories_RESP;
	}

	public List<MallProductCategoryBean> getCategories() {
		return categories;
	}

	public void setCategories(List<MallProductCategoryBean> categories) {
		this.categories = categories;
	}

}
