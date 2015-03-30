package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetBbsPostsByCreatorResp  extends PacketResp{
	@Expose
	List<BbsComments>  bbsPosts;//帖子列表
	public GetBbsPostsByCreatorResp(){
		command = HttpDefine.GET_BBS_POSTS_BY_CREATOR_RESP;
		bbsPosts=new ArrayList<BbsComments>();
	}
	public List<BbsComments> getBbsPosts()
	{
		return bbsPosts;
	}
	public void setBbsPosts(List<BbsComments> bbsPosts)
	{
		this.bbsPosts = bbsPosts;
	}


	
}
