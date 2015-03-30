package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallManagedAddressBean;
import com.e1858.wuye.protocol.http.PacketResp;

@SuppressWarnings("serial")
public class MallGetManagedAddressesResponse extends PacketResp {

	private List<MallManagedAddressBean>	addresses;	//	MallProductCategory数组	分类数组

	public MallGetManagedAddressesResponse() {
		command = HttpDefine.MallGetManagedAddresses_RESP;
	}

	public List<MallManagedAddressBean> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<MallManagedAddressBean> addresses) {
		this.addresses = addresses;
	}
}
