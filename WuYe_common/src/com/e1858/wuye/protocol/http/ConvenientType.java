package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ConvenientType implements Serializable
{
	@Expose
	private int ID = -1;// 类型ID
	@Expose
	private int catalogueID = 1;// 便民类型编目，1 生活服务、2 居家服务 3 公共服务
	@Expose
	private String name = "";// 类型名称
	@Expose
	private String icon = "";// 图标

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

	public int getCatalogueID() {
		return catalogueID;
	}

	public void setCatalogueID(int catalogueID) {
		this.catalogueID = catalogueID;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


}
