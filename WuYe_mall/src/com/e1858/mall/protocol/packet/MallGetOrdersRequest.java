package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallGetOrdersRequest extends AutoFillPacketRequest {

	public int	offset;
	public int	count;
	public int	status; //	int	参见MallOrderStatus表格,另外0代表全部

	public MallGetOrdersRequest() {
		super(HttpDefine.MallGetOrders);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
