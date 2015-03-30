package com.e1858.mall.entity;

import com.e1858.mall.protocol.bean.MallCartShopBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class MallCartShopEntity extends CacheEntityWithSpecifiedId<MallCartShopBean> {

	@Override
	protected Class<MallCartShopBean> getBeanClass() {
		return MallCartShopBean.class;
	}

	@Override
	protected String generateID(MallCartShopBean bean) {
		return bean.getID();
	}

}
