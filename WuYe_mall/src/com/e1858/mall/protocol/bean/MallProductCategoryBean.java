package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MallProductCategoryBean implements Serializable {
	private String							ID;			//	string	分类ID
	private String							imageUrl;		//	String	展示图片url
	private String							name;			//	String	分类名称
	private List<MallProductCategoryBean>	children;		//	MallProductCategory数组	子分类数组
	private String							parentID;		//	string	父分类ID(没有父类,为0)
	private String							edescription;

	public String getEdescription() {
		return edescription;
	}

	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MallProductCategoryBean> getChildren() {
		return children;
	}

	public void setChildren(List<MallProductCategoryBean> children) {
		this.children = children;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

}
