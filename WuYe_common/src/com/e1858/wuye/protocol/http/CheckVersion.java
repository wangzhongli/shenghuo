package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class CheckVersion extends PacketRequest
{
	@Expose
	private String versionCode;
	
	@Expose
	private String mobileOS;

	public CheckVersion()
	{
		command = HttpDefine.CHECK_VERSION;
	}

	public String getMobileOS()
	{
		return mobileOS;
	}

	public void setMobileOS(String mobileOS)
	{
		this.mobileOS = mobileOS;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}


}
