package com.e1858.wuye.protocol.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Maintenance implements Serializable
{
	@Expose
	private int ID = -1;// 设备报修ID
	@Expose
	private MaintenanceType type;
	@Expose
	private String phone = "";// 联系电话
	@Expose
	private String content = "";// 内容
	@Expose
	private String serviceTime = "";// 指定上门服务时间
	@Expose
	private String createTime = "";// 报修时间
	@Expose
	private int state=-1;
	@Expose
	private List<String> images = new ArrayList<String>();// 图片
	@Expose
	private List<MaintenanceResponse> responses = new ArrayList<MaintenanceResponse>();// 回复记录
	@Expose
	private String houseRoom="";
	@Expose
	private String reply="";
	@Expose
	private String handlerName="";
	@Expose
	private String handlerPhone="";
	@Expose
	private String handleTime="";
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public MaintenanceType getType() {
		return type;
	}

	public void setType(MaintenanceType type) {
		this.type = type;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getServiceTime()
	{
		return serviceTime;
	}

	public void setServiceTime(String serviceTime)
	{
		this.serviceTime = serviceTime;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public List<String> getImages()
	{
		return images;
	}

	public void setImages(List<String> images)
	{
		this.images = images;
	}

	public List<MaintenanceResponse> getResponses()
	{
		return responses;
	}

	public void setResponses(List<MaintenanceResponse> responses)
	{
		this.responses = responses;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getHouseRoom() {
		return houseRoom;
	}

	public void setHouseRoom(String houseRoom) {
		this.houseRoom = houseRoom;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getHandlerPhone() {
		return handlerPhone;
	}

	public void setHandlerPhone(String handlerPhone) {
		this.handlerPhone = handlerPhone;
	}

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}

}
