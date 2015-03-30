package com.e1858.entity;

import com.e1858.wuye.protocol.http.PointRecordBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class PointRecordEntity extends CacheEntityWithSpecifiedId<PointRecordBean> {

	@Override
	protected Class<PointRecordBean> getBeanClass() {
		return PointRecordBean.class;
	}

	@Override
	protected String generateID(PointRecordBean bean) {
		return bean.getID();
	}

}
