package com.e1858.mall.protocol.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MallProductPropertyValue implements Serializable {
	public String	ID;	//	
	public String	name;	//	

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
