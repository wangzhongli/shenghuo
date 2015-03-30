package com.e1858.mall.entity;

import com.e1858.mall.protocol.bean.MallManagedAddressBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class MallManagedAddressEntity extends CacheEntityWithSpecifiedId<MallManagedAddressBean> {

	@Override
	protected Class<MallManagedAddressBean> getBeanClass() {
		return MallManagedAddressBean.class;
	}

	@Override
	protected String generateID(MallManagedAddressBean bean) {
		return bean.getID();
	}
}
