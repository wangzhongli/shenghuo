package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

public class GetFeeBills extends PacketRequest {

	public GetFeeBills(){
		command=HttpDefine.GET_FEEBILLS;
	}
	@Expose
	private int offset;
	@Expose
	private int count;
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
