package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class GetCommunityByKeyRequest extends PacketRequest {
	@Expose
	private String	city		= "";	//城市
	@Expose
	private String	keyWords	= "";	//小区关键字搜索

	public GetCommunityByKeyRequest() {
		command = HttpDefine.GETCOMMUNITYBYKEY;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

}
