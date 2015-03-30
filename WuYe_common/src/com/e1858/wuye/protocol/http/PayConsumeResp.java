package com.e1858.wuye.protocol.http;


import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class PayConsumeResp extends PacketResp
{
	@Expose
	long consumePay;// 交易ID
	@Expose
	String tradeErrCode;// 交易结果描述
	@Expose
	byte tradeFlag = 1;// 交易标记 0成功，1失败
	@Expose
	String tradeTime;// 交易时间

	public PayConsumeResp()
	{
		command = HttpDefine.PAY_CONSUME_RESP;
	}

	public long getConsumePay()
	{
		return consumePay;
	}

	public void setConsumePay(long consumePay)
	{
		this.consumePay = consumePay;
	}

	public String getTradeErrCode()
	{
		return tradeErrCode;
	}

	public void setTradeErrCode(String tradeErrCode)
	{
		this.tradeErrCode = tradeErrCode;
	}

	public byte getTradeFlag()
	{
		return tradeFlag;
	}

	public void setTradeFlag(byte tradeFlag)
	{
		this.tradeFlag = tradeFlag;
	}

	public String getTradeTime()
	{
		return tradeTime;
	}

	public void setTradeTime(String tradeTime)
	{
		this.tradeTime = tradeTime;
	}

}
