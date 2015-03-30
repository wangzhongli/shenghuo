package com.e1858.wuye.protocol.http;
import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class CommissionResponse implements Serializable{
	@Expose
	private int ID=-1;//回复ID
	@Expose
	private String content="";//内容
	@Expose
	String creatorName = "";//回复人名称
	@Expose
	String createTime = "";//回复时间
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
