package com.e1858.object;

import java.io.Serializable;
@SuppressWarnings("serial")
public class CityBean implements Serializable
{
	private String cityName; 
	private String nameSort;
	public String getCityName()
	{
		return cityName;
	}
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	public String getNameSort()
	{
		return nameSort;
	}
	public void setNameSort(String nameSort)
	{
		this.nameSort = nameSort;
	}

	
}
