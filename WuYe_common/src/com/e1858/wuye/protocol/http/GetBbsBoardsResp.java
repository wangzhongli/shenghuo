package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class GetBbsBoardsResp  extends PacketResp{
	@Expose
	List<BbsBoard>  bbsBoards;//小区论坛板块列表
	public GetBbsBoardsResp(){
		command = HttpDefine.GET_BBS_BOARDS_RESP;
		bbsBoards=new ArrayList<BbsBoard>();
	}
	public List<BbsBoard> getBbsBoards()
	{
		return bbsBoards;
	}
	public void setBbsBoards(List<BbsBoard> bbsBoards)
	{
		this.bbsBoards = bbsBoards;
	}

}
