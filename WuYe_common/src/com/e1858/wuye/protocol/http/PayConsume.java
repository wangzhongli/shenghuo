package com.e1858.wuye.protocol.http;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class PayConsume extends PacketRequest
{
	@Expose
	long consume;// 账单ID
	@Expose
	long payInterface;// 交易接口
	@Expose
	double payAmount;// 应缴费数
	@Expose
	double tradeAmount;// 交易金额

	public PayConsume()
	{
		command = HttpDefine.PAY_CONSUME;
	}

	public long getConsume()
	{
		return consume;
	}

	public void setConsume(long consume)
	{
		this.consume = consume;
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
	
}
