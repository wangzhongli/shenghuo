package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MallRefundRecordBean implements Serializable {
	public int			type;			//	int	1.退货2.换货
	public int			status;		//	int	退换货状态
	public String		statusName;	//	string	退换货状态名称
	public String		reason;		//	string	买家退换原因
	public String		edescription;	//	String	买家退换描述
	public String		answer;		//	String	商家回复
	public String		createTime;	//	String	时间
	public List<String>	imageUrls;		//	String数组	图片urls

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEdescription() {
		return edescription;
	}

	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

}
