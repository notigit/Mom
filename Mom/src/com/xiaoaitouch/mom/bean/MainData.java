package com.xiaoaitouch.mom.bean;

import com.xiaoaitouch.mom.dao.HoroscopeModel;
import com.xiaoaitouch.mom.dao.ShareCardModel;
import com.xiaoaitouch.mom.dao.SportCardModel;

public class MainData {
	private int type;// 选项卡分类
	private SportCardModel si;// 运动卡片
	private ShareCardModel fc;// 分享卡片
	private HoroscopeModel sc;// 星座卡片
	private int index;
	private long descTime;

	/**
	 * @return the descTime
	 */
	public long getDescTime() {
		return descTime;
	}

	/**
	 * @param descTime
	 *            the descTime to set
	 */
	public void setDescTime(long descTime) {
		this.descTime = descTime;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
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

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the fc
	 */
	public ShareCardModel getFc() {
		return fc;
	}

	/**
	 * @param fc
	 *            the fc to set
	 */
	public void setFc(ShareCardModel fc) {
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

}
