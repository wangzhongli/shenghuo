package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class GetCommunityByCityNewRequest extends PacketRequest {
	@Expose
	private String	city	= "";	//地区，对应t_Community中的city
	@Expose
	private int		offset;
	@Expose
	private int		count;

	public GetCommunityByCityNewRequest() {
		command = HttpDefine.GETCOMMUNITYBYCITYNEW;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
