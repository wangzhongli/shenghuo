package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class KangelOrderStatus implements Serializable {

	@Expose
	private int status=1;//订单状态订单状态：1：待接单 2：收件中 3：执行中 4：已签收 5:已取消 6：取消中 7:提交失败
	@Expose
	private String edescription="";
	@Expose
	private String name="待接单";
	@Expose
	private String createTime="";
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getEdescription() {
		return edescription;
	}
	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
