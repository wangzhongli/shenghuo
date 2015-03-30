package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.mall.protocol.bean.MallConfirmOrderBean;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallGenerateOrderRequest extends AutoFillPacketRequest {

	public List<MallConfirmOrderBean>	confirmOrders;	//		
	public String						addressID;		//	string	MallManagedAddress.ID 这个时候服务端要设置这个地址为默认地址

	public MallGenerateOrderRequest() {
		super(HttpDefine.MallGenerateOrderNewVersion);

	}

	public List<MallConfirmOrderBean> getConfirmOrders() {
		return confirmOrders;
	}

	public void setConfirmOrders(List<MallConfirmOrderBean> confirmOrders) {
		this.confirmOrders = confirmOrders;
	}

	public String getAddressID() {
		return addressID;
	}

	public void setAddressID(String addressID) {
		this.addressID = addressID;
	}

}
