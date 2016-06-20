package com.xiaoaitouch.mom.wheelview;

import java.io.Serializable;

public class BeaconInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String TAG = "BEACONINFO";
	
	private String uuid = "";
	private String createdate = "";
	private String low_battery = "";
	private String last_connect_date = "";
	private String desc = "";
	private String last_broken_time = "";
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getLow_battery() {
		return low_battery;
	}
	public void setLow_battery(String low_battery) {
		this.low_battery = low_battery;
	}
	public String getLast_connect_date() {
		return last_connect_date;
	}
	public void setLast_connect_date(String last_connect_date) {
		this.last_connect_date = last_connect_date;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLast_broken_time() {
		return last_broken_time;
	}
	public void setLast_broken_time(String last_broken_time) {
		this.last_broken_time = last_broken_time;
	}
	
	
}
