package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallDeleteManagedAddressRequest extends AutoFillPacketRequest {

	public String	ID; //

	public MallDeleteManagedAddressRequest() {
		super(HttpDefine.MallDeleteManagedAddress);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
