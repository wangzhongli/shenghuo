package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public abstract class PacketRequest extends Packet
{
	@Expose
	private String key="";
	@Expose
	private String token="";
	@Expose
	private int communityID=0;
	
	public PacketRequest()
	{
		command = 0;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getToken() {
		return token;
	}
	public int getCommunityID() {
		return communityID;
	}
	public void setCommunityID(int communityID) {
		this.communityID = communityID;
	}
	public void setToken(String token) {
		this.token = token;
	}

	

}
