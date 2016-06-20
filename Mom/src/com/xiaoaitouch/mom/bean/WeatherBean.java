package com.xiaoaitouch.mom.bean;

/**
 * 天气
 * 
 * @author huxin
 * 
 */
public class WeatherBean {
	private String day_weather_desc;// 晴
	private String day_winddire_desc;// 无持续风向
	private String day_temp;// 29
	private String day_weather;
	

	private String night_temp;
	private String night_weather_desc;

	/**
	 * @return the day_weather
	 */
	public String getDay_weather() {
		return day_weather;
	}

	/**
	 * @param day_weather the day_weather to set
	 */
	public void setDay_weather(String day_weather) {
		this.day_weather = day_weather;
	}

	/**
	 * @return the day_weather_desc
	 */
	public String getDay_weather_desc() {
		return day_weather_desc;
	}

	/**
	 * @param day_weather_desc
	 *            the day_weather_desc to set
	 */
	public void setDay_weather_desc(String day_weather_desc) {
		this.day_weather_desc = day_weather_desc;
	}

	/**
	 * @return the day_winddire_desc
	 */
	public String getDay_winddire_desc() {
		return day_winddire_desc;
	}

	/**
	 * @param day_winddire_desc
	 *            the day_winddire_desc to set
	 */
	public void setDay_winddire_desc(String day_winddire_desc) {
		this.day_winddire_desc = day_winddire_desc;
	}

	/**
	 * @return the day_temp
	 */
	public String getDay_temp() {
		return day_temp;
	}

	/**
	 * @param day_temp
	 *            the day_temp to set
	 */
	public void setDay_temp(String day_temp) {
		this.day_temp = day_temp;
	}

	/**
	 * @return the night_temp
	 */
	public String getNight_temp() {
		return night_temp;
	}

	/**
	 * @param night_temp
	 *            the night_temp to set
	 */
	public void setNight_temp(String night_temp) {
		this.night_temp = night_temp;
	}

	/**
	 * @return the night_weather_desc
	 */
	public String getNight_weather_desc() {
		return night_weather_desc;
	}

	/**
	 * @param night_weather_desc
	 *            the night_weather_desc to set
	 */
	public void setNight_weather_desc(String night_weather_desc) {
		this.night_weather_desc = night_weather_desc;
	}

}
