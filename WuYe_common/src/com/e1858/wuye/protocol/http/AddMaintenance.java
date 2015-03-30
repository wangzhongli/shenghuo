package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddMaintenance extends PacketRequest
{

	@Expose
	private int typeID = -1;// 报修类型Id
	@Expose
	private String content = "";// 报修内容
	@Expose
	private String phone = "";// 联系电话
	@Expose
	private String serviceTime = "";// 指定上门服务时间
	@Expose
	private List<String> images;// 图片列表

	public AddMaintenance()
	{
		command = HttpDefine.ADD_MAINTENANCE;
		images = new ArrayList<String>();
	}


	public int getTypeID() {
		return typeID;
	}


	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}


	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getServiceTime()
	{
		return serviceTime;
	}

	public void setServiceTime(String serviceTime)
	{
		this.serviceTime = serviceTime;
	}

	public List<String> getImages()
	{
		return images;
	}

	public void setImages(List<String> images)
	{
		this.images = images;
	}

}
