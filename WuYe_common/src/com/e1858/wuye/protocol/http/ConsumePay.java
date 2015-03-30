package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class ConsumePay implements Serializable
{
	@Expose
	long consumePay;// 交易ID
	@Expose
	long payInterface;// 交易接口
	@Expose
	double payAmount;// 应缴费数
	@Expose
	double tradeAmount;// 交易金额
	@Expose
	String tradeSequence;// 交易结果描述
	@Expose
	String tradeErrCode;// 交易结果描述
	@Expose
	byte tradeFlag;// 交易标记 0成功，1失败
	@Expose
	String tradeTime;// 交易时间

	public long getConsumePay()
	{
		return consumePay;
	}

	public void setConsumePay(long consumePay)
	{
		this.consumePay = consumePay;
	}

	public long getPayInterface()
	{
		return payInterface;
	}

	public void setPayInterface(long payInterface)
	{
		this.payInterface = payInterface;
	}

	public double getPayAmount()
	{
		return payAmount;
	}

	public void setPayAmount(double payAmount)
	{
		this.payAmount = payAmount;
	}

	public double getTradeAmount()
	{
		return tradeAmount;
	}

	public void setTradeAmount(double tradeAmount)
	{
		this.tradeAmount = tradeAmount;
	}

	public String getTradeSequence()
	{
		return tradeSequence;
	}

	public void setTradeSequence(String tradeSequence)
	{
		this.tradeSequence = tradeSequence;
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
