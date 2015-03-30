package com.e1858.wuye.protocol.http;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
@SuppressWarnings("serial")
public class Consume implements Serializable
{
	@Expose
	Long consume;// 账单ID
	@Expose
	String payNumber;// 户号
	@Expose
	double postAmount;// 上期表数
	@Expose
	double currentAmount;// 本期表数
	@Expose
	double amount;// 本期用量
	@Expose
	double price;// 单价
	@Expose
	double payAmount;// 金额
	@Expose
	String consumeTime;// 录入时间
	@Expose
	int year;// 账单年份
	@Expose
	int month;// 账单月份
	@Expose
	ConsumePay consumePay;

	public Long getConsume()
	{
		return consume;
	}

	public void setConsume(Long consume)
	{
		this.consume = consume;
	}

	public String getPayNumber()
	{
		return payNumber;
	}

	public void setPayNumber(String payNumber)
	{
		this.payNumber = payNumber;
	}

	public double getPostAmount()
	{
		return postAmount;
	}

	public void setPostAmount(double postAmount)
	{
		this.postAmount = postAmount;
	}

	public double getCurrentAmount()
	{
		return currentAmount;
	}

	public void setCurrentAmount(double currentAmount)
	{
		this.currentAmount = currentAmount;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public double getPayAmount()
	{
		return payAmount;
	}

	public void setPayAmount(double payAmount)
	{
		this.payAmount = payAmount;
	}

	public String getConsumeTime()
	{
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime)
	{
		this.consumeTime = consumeTime;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public int getMonth()
	{
		return month;
	}

	public void setMonth(int month)
	{
		this.month = month;
	}

	public ConsumePay getConsumePay()
	{
		return consumePay;
	}

	public void setConsumePay(ConsumePay consumePay)
	{
		this.consumePay = consumePay;
	}

}
