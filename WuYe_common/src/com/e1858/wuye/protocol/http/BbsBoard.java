package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class BbsBoard  implements Serializable
{
	@Expose
	private int ID=-1;//论坛板块ID，对应t_BbsBoard中的ID
	@Expose
	private String name="";//板块名称
	@Expose
	private String edescription="";//板块描述
	@Expose
	private int topicCount=0;//帖子数量
	@Expose
	private Boolean disable=false;

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	public String getEdescription() {
		return edescription;
	}
	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}

	public int getTopicCount() {
		return topicCount;
	}
	public void setTopicCount(int topicCount) {
		this.topicCount = topicCount;
	}
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

}
