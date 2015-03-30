package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

import com.e1858.wuye.protocol.http.BannerBean;

/**
 * 日期: 2015年2月2日 下午6:10:06
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class MallRecommendedCategoryBean implements Serializable {
	MallProductCategoryBean	productCategory;	//	分类
	int						layout;			//	int	1按着左边一个大图右边一个小图的样子排列
	List<BannerBean>		banners;			//	Banner数组	

	public MallProductCategoryBean getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(MallProductCategoryBean productCategory) {
		this.productCategory = productCategory;
	}

	public int getLayout() {
		return layout;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public List<BannerBean> getBanners() {
		return banners;
	}

	public void setBanners(List<BannerBean> banners) {
		this.banners = banners;
	}
}
