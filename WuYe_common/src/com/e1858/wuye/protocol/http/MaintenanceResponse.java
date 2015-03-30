package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class MaintenanceResponse implements Serializable{
	@Expose
	private long response=-1;//回复ID
	@Expose 
	private String maintenanceMan="";//维修人
	@Expose 
	private String phone="";//维修人电话
	@Expose
	private String content="";//内容
	@Expose
	String creator = "";//回复人名称
	@Expose
	String createTime = "";//回复时间
	
	
	public long getResponse()
	{
		return response;
	}
	public void setResponse(long response)
	{
		this.response = response;
	}
	public String getMaintenanceMan()
	{
		return maintenanceMan;
	}
	public void setMaintenanceMan(String maintenanceMan)
	{
		this.maintenanceMan = maintenanceMan;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getCreator()
	{
		return creator;
	}
	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	
	
}
