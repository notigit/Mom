package com.xiaoaitouch.mom.bean;

/**
 * pm2.5
 * 
 * @author huxin
 * 
 */
public class PmBean {
	private int aqi;
	private String pm2_5;
	private String area;

	/**
	 * @return the aqi
	 */
	public int getAqi() {
		return aqi;
	}

	/**
	 * @param aqi
	 *            the aqi to set
	 */
	public void setAqi(int aqi) {
		this.aqi = aqi;
	}

	/**
	 * @return the pm2_5
	 */
	public String getPm2_5() {
		return pm2_5;
	}

	/**
	 * @param pm2_5
	 *            the pm2_5 to set
	 */
	public void setPm2_5(String pm2_5) {
		this.pm2_5 = pm2_5;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

}
