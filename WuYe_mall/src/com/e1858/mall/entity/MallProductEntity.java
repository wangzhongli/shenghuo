package com.e1858.mall.entity;

import com.e1858.mall.protocol.bean.MallProductBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class MallProductEntity extends CacheEntityWithSpecifiedId<MallProductBean> {

	@Override
	protected Class<MallProductBean> getBeanClass() {
		return MallProductBean.class;
	}

	@Override
	protected String generateID(MallProductBean bean) {
		return bean.getID();
	}
}
