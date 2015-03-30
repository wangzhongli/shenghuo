package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class BannerBean implements Serializable {
	@Expose
	private String	name		= "";
	@Expose
	private String	url			= "";
	@Expose
	private String	imageUrl	= "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
