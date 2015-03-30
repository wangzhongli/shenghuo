package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallReceiveOrderRequest extends AutoFillPacketRequest {

	public String	ID; //

	public MallReceiveOrderRequest() {
		super(HttpDefine.MallReceiveOrder);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
