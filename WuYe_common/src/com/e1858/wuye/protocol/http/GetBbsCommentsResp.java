package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetBbsCommentsResp  extends PacketResp{
	@Expose
	List<BbsComments>  bbsComments;//帖子列表
	public GetBbsCommentsResp(){
		command = HttpDefine.GET_BBS_POSTS_RESP;
		bbsComments=new ArrayList<BbsComments>();
	}
	public List<BbsComments> getBbsComments() {
		return bbsComments;
	}
	public void setBbsComments(List<BbsComments> bbsComments) {
		this.bbsComments = bbsComments;
	}
	
}
