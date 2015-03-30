package com.e1858.wuye.protocol.http;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PointRecordBean implements Serializable {
	public String	ID;		//	String	ID
	public String	createTime; //	String	产生时间
	public String	title;		//	String	积分来源:例如:推荐评价,话题被赞等,用来显示用的
	public String	content;	//	string	具体内容
	public int		points;	//	int	获得(消费)的积分,可以是正数或者负数

	public String getID() {
		return ID;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
