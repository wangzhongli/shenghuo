package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class KangelGetOrders extends PacketRequest {

	@Expose
	private int offset;
	@Expose
	private int count;
	public KangelGetOrders(){
		command=HttpDefine.KANGEL_GET_ORDERS;
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
