package com.e1858.mall.protocol.bean;

import java.io.Serializable;

import android.text.TextUtils;

@SuppressWarnings("serial")
public class MallManagedAddressBean implements Serializable {
	public String	ID;		//	string	地址
	public String	name;		//	string	姓名
	public String	phone;		//	string	电话
	public String	province;	//	string	省
	public String	city;		//	string	市
	public String	district;	//	string	区县
	public String	address;	//	string	详细地址
	public String	zipCode;	//	string	邮编

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String provinceCityDistrict() {
		StringBuilder cityBuilder = new StringBuilder();
		if (!TextUtils.isEmpty(getProvince())) {
			cityBuilder.append(getProvince());
		}
		if (!TextUtils.isEmpty(getCity())) {
			cityBuilder.append(getCity());
		}
		if (!TextUtils.isEmpty(getDistrict())) {
			cityBuilder.append(getDistrict());
		}
		return cityBuilder.toString();
	}
}
