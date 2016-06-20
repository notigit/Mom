package com.xiaoaitouch.mom.bean;

import java.io.Serializable;

public class LocationBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String locName;// 地名
	public String province;// 省名
	public String city;// 城市
	public String district;// 区名
	public String street;// 街道
	public String streetNum;// 街道號
	public Double latitude;// 纬度
	public Double longitude;// 经度
	private String uid;

	public String time;
	public int locType;
	public float radius;

	// gps才有的
	public float speed;
	public int satellite;
	public float direction;
	// wifi才有的
	public String addStr;// 具体地址
	public int operationers;

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the locType
	 */
	public int getLocType() {
		return locType;
	}

	/**
	 * @param locType
	 *            the locType to set
	 */
	public void setLocType(int locType) {
		this.locType = locType;
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * @return the satellite
	 */
	public int getSatellite() {
		return satellite;
	}

	/**
	 * @param satellite
	 *            the satellite to set
	 */
	public void setSatellite(int satellite) {
		this.satellite = satellite;
	}

	/**
	 * @return the direction
	 */
	public float getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(float direction) {
		this.direction = direction;
	}

	/**
	 * @return the operationers
	 */
	public int getOperationers() {
		return operationers;
	}

	/**
	 * @param operationers
	 *            the operationers to set
	 */
	public void setOperationers(int operationers) {
		this.operationers = operationers;
	}

	/**
	 * @return the addStr
	 */
	public String getAddStr() {
		return addStr;
	}

	/**
	 * @param addStr
	 *            the addStr to set
	 */
	public void setAddStr(String addStr) {
		this.addStr = addStr;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public Object clone() {
		LocationBean o = null;
		try {
			// Object中的clone()识别出你要复制的是哪一个对象。
			o = (LocationBean) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return o;
	}

	/**
	 * @return the locName
	 */
	public String getLocName() {
		return locName;
	}

	/**
	 * @param locName
	 *            the locName to set
	 */
	public void setLocName(String locName) {
		this.locName = locName;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the streetNum
	 */
	public String getStreetNum() {
		return streetNum;
	}

	/**
	 * @param streetNum
	 *            the streetNum to set
	 */
	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
