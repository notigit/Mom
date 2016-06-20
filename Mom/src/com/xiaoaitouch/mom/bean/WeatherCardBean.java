package com.xiaoaitouch.mom.bean;

/**
 * 天气卡片
 * 
 * @author huxin
 * 
 */
public class WeatherCardBean {
	private String areaid;
	private String day_temp;// 白天温度
	private String day_weather_desc;// 晴
	private String night_temp;// 夜晚温度
	private String night_winddire_desc;// 晴

	/**
	 * @return the areaid
	 */
	public String getAreaid() {
		return areaid;
	}

	/**
	 * @param areaid
	 *            the areaid to set
	 */
	public void setAreaid(String areaid) {
		this.areaid = areaid;
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
	 * @return the night_winddire_desc
	 */
	public String getNight_winddire_desc() {
		return night_winddire_desc;
	}

	/**
	 * @param night_winddire_desc
	 *            the night_winddire_desc to set
	 */
	public void setNight_winddire_desc(String night_winddire_desc) {
		this.night_winddire_desc = night_winddire_desc;
	}

}
