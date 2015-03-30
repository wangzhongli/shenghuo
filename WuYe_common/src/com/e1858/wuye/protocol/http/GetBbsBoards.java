package com.e1858.wuye.protocol.http;

@SuppressWarnings("serial")
public class GetBbsBoards extends PacketRequest{	
	
	public GetBbsBoards(){
		command = HttpDefine.GET_BBS_BOARDS;
	}
	
}
