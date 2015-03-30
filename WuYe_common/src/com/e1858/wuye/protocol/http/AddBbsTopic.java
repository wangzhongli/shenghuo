package com.e1858.wuye.protocol.http;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class AddBbsTopic extends PacketRequest
{
	@Expose
	private int boardID = -1;// 板块ID
	@Expose
	private String title = "";// 话题标题
	@Expose
	private String content = "";// 话题内容
	@Expose
	private List<String> images = new ArrayList<String>();// 帖子图片列表

	public AddBbsTopic()
	{
		command = HttpDefine.ADD_BBS_TOPIC;
	}

	public int getBoardID() {
		return boardID;
	}

	public void setBoardID(int boardID) {
		this.boardID = boardID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

}
