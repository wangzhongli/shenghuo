package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.mall.protocol.bean.MallRecommendedCategoryBean;
import com.e1858.wuye.protocol.http.BannerBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetMainDataResponse extends PacketResp {

	private List<BannerBean>					topBanners;			//	
	private List<MallRecommendedCategoryBean>	recommendedCategories;	//	首页推荐商品[添加]
	private List<MallProductCategoryBean>		moreCategories;		//	MallProductCategory数组	首页更多商品[添加]

	private String								originTagID;

	public String getOriginTagID() {
		return originTagID;
	}

	public void setOriginTagID(String originTagID) {
		this.originTagID = originTagID;
	}

	public MallGetMainDataResponse() {
		command = HttpDefine.MallGetMainData_RESP;
	}

	public List<BannerBean> getTopBanners() {
		return topBanners;
	}

	public void setTopBanners(List<BannerBean> topBanners) {
		this.topBanners = topBanners;
	}

	public List<MallRecommendedCategoryBean> getRecommendedCategories() {
		return recommendedCategories;
	}

	public void setRecommendedCategories(List<MallRecommendedCategoryBean> recommendedCategories) {
		this.recommendedCategories = recommendedCategories;
	}

	public List<MallProductCategoryBean> getMoreCategories() {
		return moreCategories;
	}

	public void setMoreCategories(List<MallProductCategoryBean> moreCategories) {
		this.moreCategories = moreCategories;
	}

}
