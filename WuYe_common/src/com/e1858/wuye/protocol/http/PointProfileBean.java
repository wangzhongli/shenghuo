package com.e1858.wuye.protocol.http;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PointProfileBean implements Serializable {
	public String	levelName;			//	String	等级
	public String	levelIconUrl;		//	String	等级图标链接
	public float	levelProgress;		//	float	[0,1]
	public int		points;			//	int	现有积分
	public String	maxLevelName;		//	string	最大等级
	public String	maxLevelIconUrl;	//	string	最大等级图标链接
	public float	exchangeRate;

	public float getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelIconUrl() {
		return levelIconUrl;
	}

	public void setLevelIconUrl(String levelIconUrl) {
		this.levelIconUrl = levelIconUrl;
	}

	public float getLevelProgress() {
		return levelProgress;
	}

	public void setLevelProgress(float levelProgress) {
		this.levelProgress = levelProgress;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getMaxLevelName() {
		return maxLevelName;
	}

	public void setMaxLevelName(String maxLevelName) {
		this.maxLevelName = maxLevelName;
	}

	public String getMaxLevelIconUrl() {
		return maxLevelIconUrl;
	}

	public void setMaxLevelIconUrl(String maxLevelIconUrl) {
		this.maxLevelIconUrl = maxLevelIconUrl;
	}

}
