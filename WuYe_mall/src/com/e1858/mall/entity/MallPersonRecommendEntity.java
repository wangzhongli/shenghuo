package com.e1858.mall.entity;

import com.e1858.mall.protocol.bean.MallRecommend;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class MallPersonRecommendEntity extends CacheEntityWithSpecifiedId<MallRecommend> {

	@Override
	protected Class<MallRecommend> getBeanClass() {
		return MallRecommend.class;
	}

	@Override
	protected String generateID(MallRecommend bean) {
		return bean.getID();
	}

}
