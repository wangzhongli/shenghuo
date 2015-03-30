package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class CheckVersionResp extends PacketResp
{

	@Expose
	private String url;//新版本文件的下载地址；如果为空，表示没有新版本
	@Expose
	private String edescription;//更新说明
	
	@Expose
	private String versionName;
	
	public CheckVersionResp()
	{
		command = HttpDefine.CHECK_VERSION_RESP;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}


	public String getEdescription() {
		return edescription;
	}

	public void setEdescription(String edescription) {
		this.edescription = edescription;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	
}
