package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public abstract class PacketResp extends Packet
{
	@Expose
	private int		ret	= 0;//执行状态，0表示成功，其他表示失败
	@Expose
	private String	error	= "";//失败描述
	
	public PacketResp()
	{
		command=0;
	}


	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean isSuccess() {
		return ret == HttpDefine.RESPONSE_SUCCESS;
	}

}
