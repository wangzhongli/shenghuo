package com.e1858.mall.protocol.bean;

import java.util.List;

public class MallProductPropertyBean {
	public String							ID;	//	string	属性ID
	public String							name;	//	string	属性名字(例如:尺寸)
	public List<MallProductPropertyValue>	values; //	MallProductPropertyValue数组	属性值列表:

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

	public List<MallProductPropertyValue> getValues() {
		return values;
	}

	public void setValues(List<MallProductPropertyValue> values) {
		this.values = values;
	}

}
