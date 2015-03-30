package com.e1858.wuye.protocol.http;

public class PointGetRecordsRequest extends AutoFillPacketRequest {

	int	offset;
	int	count;

	public PointGetRecordsRequest() {
		super(HttpDefine.PointGetRecords);
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
