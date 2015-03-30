package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class UploadJson implements Serializable
{
	@Expose
	private int ret = 0;
	@Expose
	private String url;
	@Expose
	private String error;
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	

}
