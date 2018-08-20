package com.lightSensorPage.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class LightSensorBean implements Serializable {
	private long id;
	private int lightSensorValue;
	private Timestamp inserttime;

	public LightSensorBean(){
		this.id = -1L;
		this.lightSensorValue = -1;
		this.inserttime = null;
	}

	public LightSensorBean(long id, int lightSensorValue, Timestamp inserttime){
		this.id = id;
		this.lightSensorValue = lightSensorValue;
		this.inserttime = inserttime;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLightSensorValue() {
		return lightSensorValue;
	}
	public void setLightSensorValue(int lightSensorValue) {
		this.lightSensorValue = lightSensorValue;
	}
	public Timestamp getInserttime() {
		return inserttime;
	}
	public void setInserttime(Timestamp inserttime) {
		this.inserttime = inserttime;
	}

}
