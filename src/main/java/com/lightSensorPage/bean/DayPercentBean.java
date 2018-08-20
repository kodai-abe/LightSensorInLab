package com.lightSensorPage.bean;

import java.io.Serializable;

public class DayPercentBean implements Serializable {
	private int hour;
	private double percent;

	public DayPercentBean(){
		this.hour = -1;
		this.percent = -1;
	}

	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}

}
