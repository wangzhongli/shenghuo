package com.e1858.mall.protocol.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MallOrderStatusBean implements Serializable {
	public int		status;		//	int	状态值（订单状态订单状态：1：已下单 2：已付款 3：已完成,4：已关闭 )
	public String	edescription;	//	String	描述
	public String	name;			//	String	状态名称
	public String	createTime;	//String	时间

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
