package com.xiaoaitouch.mom.bean;

import java.util.List;

import com.xiaoaitouch.mom.dao.MSymptomModel;
import com.xiaoaitouch.mom.dao.MWeightModel;
import com.xiaoaitouch.mom.dao.MscModel;

/**
 * 主界面实体类
 * 
 * @author huxin
 * 
 */
public class MainDataBean {
	private MWeightModel mw;// 妈妈体重
	private List<MSymptomModel> ms;// 妈妈症状
	private List<MscModel> msc;// 妈妈自查
	private List<MainCardDataBean> data;// 卡片组合

	/**
	 * @return the mw
	 */
	public MWeightModel getMw() {
		return mw;
	}

	/**
	 * @param mw
	 *            the mw to set
	 */
	public void setMw(MWeightModel mw) {
		this.mw = mw;
	}

	/**
	 * @return the data
	 */
	public List<MainCardDataBean> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<MainCardDataBean> data) {
		this.data = data;
	}

	/**
	 * @return the ms
	 */
	public List<MSymptomModel> getMs() {
		return ms;
	}

	/**
	 * @param ms
	 *            the ms to set
	 */
	public void setMs(List<MSymptomModel> ms) {
		this.ms = ms;
	}

	/**
	 * @return the msc
	 */
	public List<MscModel> getMsc() {
		return msc;
	}

	/**
	 * @param msc the msc to set
	 */
	public void setMsc(List<MscModel> msc) {
		this.msc = msc;
	}



}
