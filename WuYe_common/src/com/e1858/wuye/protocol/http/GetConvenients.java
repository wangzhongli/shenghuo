package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetConvenients extends PacketRequest
{
	@Expose
	private int count=0;
	@Expose
	private int offset=0;
	@Expose
	private int typeID = -1;// 便民项目类型ID

	public GetConvenients()
	{
		command = HttpDefine.GET_CONVENIENTS;
	}
	public int getTypeID() {
		return typeID;
	}
	public void setTypeID(int typeID) {
		this.typeID = typeID;
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
