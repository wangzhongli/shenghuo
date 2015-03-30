package com.e1858.wuye.protocol.http;

import java.util.List;

public class PointGetRecordsResp extends PacketResp {
	List<PointRecordBean>	records;	//		积分记录

	public PointGetRecordsResp() {
		command = HttpDefine.PointGetRecords_RESP;
	}

	public List<PointRecordBean> getRecords() {
		return records;
	}

	public void setRecords(List<PointRecordBean> records) {
		this.records = records;
	}

}
