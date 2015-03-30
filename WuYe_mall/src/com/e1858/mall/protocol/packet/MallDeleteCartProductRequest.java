package com.e1858.mall.protocol.packet;

import java.util.List;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallDeleteCartProductRequest extends AutoFillPacketRequest {

	public List<String>	IDs;	//	string	MallCartProduct.ID

	public MallDeleteCartProductRequest() {
		super(HttpDefine.MallDeleteCartProduct);
	}

	public List<String> getIDs() {
		return IDs;
	}

	public void setIDs(List<String> iDs) {
		IDs = iDs;
	}

}
