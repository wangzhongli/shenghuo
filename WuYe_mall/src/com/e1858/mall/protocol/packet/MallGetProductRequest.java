package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallGetProductRequest extends AutoFillPacketRequest {
	public String	ID;

	public MallGetProductRequest() {
		super(HttpDefine.MallGetProductDetail);
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

}
