package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallManagedAddressBean;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallEditManagedAddressRequest extends AutoFillPacketRequest {

	public MallManagedAddressBean	address;

	public MallEditManagedAddressRequest() {
		super(HttpDefine.MallEditManagedAddress);
	}

	public MallManagedAddressBean getAddress() {
		return address;
	}

	public void setAddress(MallManagedAddressBean address) {
		this.address = address;
	}

}
