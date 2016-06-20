package com.xiaoaitouch.mom.bean;

/**
 * 星座卡片
 * 
 * @author huxin
 * 
 */
public class HspCardBean {
	private long id;
	private String message;
	private int stars;
	private long userId;
	private int horoscope;
	private String date;
	private int dueDays;

	/**
	 * @return the dueDays
	 */
	public int getDueDays() {
		return dueDays;
	}

	/**
	 * @param dueDays
	 *            the dueDays to set
	 */
	public void setDueDays(int dueDays) {
		this.dueDays = dueDays;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the horoscope
	 */
	public int getHoroscope() {
		return horoscope;
	}

	/**
	 * @param horoscope
	 *            the horoscope to set
	 */
	public void setHoroscope(int horoscope) {
		this.horoscope = horoscope;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the stars
	 */
	public int getStars() {
		return stars;
	}

	/**
	 * @param stars
	 *            the stars to set
	 */
	public void setStars(int stars) {
		this.stars = stars;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

}
