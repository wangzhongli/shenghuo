package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetMaintenances extends PacketRequest{	
	@Expose
	private int count=10;//获取条数
	@Expose
	private int offset=-1;//direction为1，表示当前屏显第一条记录的ID;direction为2，表示当前屏显最后一条记录ID
	
	public GetMaintenances(){
		command = HttpDefine.GET_MAINTENANCES;
	}


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
