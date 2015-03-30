package com.e1858.mall.entity;

import com.e1858.mall.protocol.bean.MallOrderBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class MallOrderEntity extends CacheEntityWithSpecifiedId<MallOrderBean> {

	@Override
	protected Class<MallOrderBean> getBeanClass() {
		return MallOrderBean.class;
	}

	@Override
	protected String generateID(MallOrderBean bean) {
		return bean.getID();
	}
}
