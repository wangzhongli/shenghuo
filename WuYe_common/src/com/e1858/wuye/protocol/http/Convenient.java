package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Convenient implements Serializable
{
	@Expose
	private int ID = -1;// ID，对应t_CommunityConvenient中的ID
	@Expose
	private String name = "";// 名称
	@Expose
	private String icon = "";// 图标
	@Expose
	private String address = "";// 地址
	@Expose
	private String phone = "";// 电话
	@Expose
	private String serviceTime = "";// 服务时间
	@Expose
	private String edescription = "";// 描述
	@Expose
	private List<String> images = new ArrayList<String>();// 图片列表
	
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

	

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
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

	

	public String getEdescription() {
		return edescription;
	}

	public void setEdescription(String edescription) {
		this.edescription = edescription;
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
