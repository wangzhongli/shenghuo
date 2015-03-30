package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallGetMainDataRequest extends AutoFillPacketRequest {

	public MallGetMainDataRequest() {
		super(HttpDefine.MallGetMainData);
	}

}
