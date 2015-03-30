package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetBbsPostsByCreator extends PacketRequest
{
	@Expose
	private int creatorID = -1;// 创建者ID
	@Expose
	private int count = 10;// 获取条数
	@Expose
	private long offset = -1;// 偏移量

	public GetBbsPostsByCreator()
	{
		command = HttpDefine.GET_BBS_POSTS_BY_CREATOR;
	}

	public int getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}


}
