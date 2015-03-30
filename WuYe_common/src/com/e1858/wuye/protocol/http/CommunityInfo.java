package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class CommunityInfo implements Serializable {
	@Expose
	private String				phone			= "";							//物业电话，对应t_Community中的telePhone
	@Expose
	private String				name			= "";							//小区名称，对应t_Community中的name
	@Expose
	List<String>				images			= new ArrayList<String>();		//首页小区轮播图片，对应t_CommunityImage的imageUrl
	@Expose
	private List<Integer>		shortcuts		= new ArrayList<Integer>();	//首页快捷功能
	@Expose
	private List<BannerBean>	advertisements	= new ArrayList<BannerBean>();
	@Expose
	private String				mobile			= "";

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<Integer> getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(List<Integer> shortcuts) {
		this.shortcuts = shortcuts;
	}

	public List<BannerBean> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<BannerBean> advertisements) {
		this.advertisements = advertisements;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
