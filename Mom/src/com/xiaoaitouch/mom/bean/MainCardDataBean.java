package com.xiaoaitouch.mom.bean;

import java.util.List;

import com.xiaoaitouch.mom.dao.HoroscopeModel;
import com.xiaoaitouch.mom.dao.ShareCardModel;
import com.xiaoaitouch.mom.dao.SportCardModel;

public class MainCardDataBean {
	private List<ShareCardModel> fc;// 分享卡片
	private HoroscopeModel sc;// 星座卡片
	private SportCardModel si;// 运动卡片

	/**
	 * @return the fc
	 */
	public List<ShareCardModel> getFc() {
		return fc;
	}

	/**
	 * @param fc
	 *            the fc to set
	 */
	public void setFc(List<ShareCardModel> fc) {
		this.fc = fc;
	}

	/**
	 * @return the sc
	 */
	public HoroscopeModel getSc() {
		return sc;
	}

	/**
	 * @param sc
	 *            the sc to set
	 */
	public void setSc(HoroscopeModel sc) {
		this.sc = sc;
	}

	/**
	 * @return the si
	 */
	public SportCardModel getSi() {
		return si;
	}

	/**
	 * @param si
	 *            the si to set
	 */
	public void setSi(SportCardModel si) {
		this.si = si;
	}

}
