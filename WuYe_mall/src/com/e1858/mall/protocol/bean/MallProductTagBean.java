package com.e1858.mall.protocol.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MallProductTagBean implements Serializable {
	public String	ID;	//	string	标签ID
	public String	name;	//	String	名字 (例如:有机)

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
