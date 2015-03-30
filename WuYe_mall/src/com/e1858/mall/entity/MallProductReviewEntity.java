package com.e1858.mall.entity;

import android.text.TextUtils;

import com.e1858.mall.protocol.bean.MallProductReviewBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class MallProductReviewEntity extends CacheEntityWithSpecifiedId<MallProductReviewBean> {

	@Override
	protected Class<MallProductReviewBean> getBeanClass() {
		return MallProductReviewBean.class;
	}

	@Override
	protected String generateID(MallProductReviewBean bean) {
		if (TextUtils.isEmpty(bean.getID())) {
			return "" + bean.hashCode();
		}
		return bean.getID();
	}
}
