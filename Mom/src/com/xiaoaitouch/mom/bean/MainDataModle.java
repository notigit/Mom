package com.xiaoaitouch.mom.bean;

import java.util.List;

import com.xiaoaitouch.mom.dao.HoroscopeModel;
import com.xiaoaitouch.mom.data.model.MainUserModel;

public class MainDataModle {
	private MainUserModel mainUserModel;
	private List<MainData> mainDatas;
	private List<HoroscopeModel> sc;// 星座卡片
	

	/**
	 * @return the sc
	 */
	public List<HoroscopeModel> getSc() {
		return sc;
	}

	/**
	 * @param sc the sc to set
	 */
	public void setSc(List<HoroscopeModel> sc) {
		this.sc = sc;
	}

	/**
	 * @return the mainUserModel
	 */
	public MainUserModel getMainUserModel() {
		return mainUserModel;
	}

	/**
	 * @param mainUserModel
	 *            the mainUserModel to set
	 */
	public void setMainUserModel(MainUserModel mainUserModel) {
		this.mainUserModel = mainUserModel;
	}

	/**
	 * @return the mainDatas
	 */
	public List<MainData> getMainDatas() {
		return mainDatas;
	}

	/**
	 * @param mainDatas
	 *            the mainDatas to set
	 */
	public void setMainDatas(List<MainData> mainDatas) {
		this.mainDatas = mainDatas;
	}

}
