package com.xiaoaitouch.mom.util;

import java.util.List;

import android.app.Activity;

import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.bean.MineInfo;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.dao.UserInfoDao;

/**
 * 本地数据库操作类
 * 
 * @author huxin
 * 
 */
public class UserDataUtils {
	private MomApplication mApplication;
	private UserInfoDao mUserInfoDao;

	public UserDataUtils(Activity activity) {
		mApplication = (MomApplication) activity.getApplication();
		mUserInfoDao = mApplication.getDaoSession().getUserInfoDao();
	}

	/**
	 * 得到当前用户信息
	 * 
	 * @return
	 */
	public UserInfo getUserData() {
		UserInfo uInfo = null;
		List<UserInfo> mInfos = mUserInfoDao.loadAll();
		if (mInfos != null && mInfos.size() > 0) {
			uInfo = mInfos.get(0);
			mApplication.setUserInfo(uInfo);
		} else {
			uInfo = null;
		}
		return uInfo;
	}

	/**
	 * 本地用户信息保存
	 * 
	 * @param mineInfo
	 */
	public void saveData(MineInfo mineInfo) {
		if (mineInfo != null) {
			mUserInfoDao.deleteAll();
			UserInfo uInfo = new UserInfo();
			uInfo.setNickName(mineInfo.getUserInfo().getNeckname() == null ? ""
					: mineInfo.getUserInfo().getNeckname());
			uInfo.setAccount(mineInfo.getUserInfo().getUserName() == null ? ""
					: mineInfo.getUserInfo().getUserName());
			uInfo.setPwd(mineInfo.getUserInfo().getPwd() == null ? ""
					: mineInfo.getUserInfo().getPwd());
			uInfo.setAge(String.valueOf(mineInfo.getUserInfo().getAge()));
			uInfo.setCreateTime(String.valueOf(mineInfo.getUserInfo()
					.getCreateTime()));
			uInfo.setDueTime(String
					.valueOf(mineInfo.getUserInfo().getDueTime()));
			uInfo.setHeight(String.valueOf(mineInfo.getUserInfo().getHeight()));
			uInfo.setLastMensesTime(String.valueOf(mineInfo.getUserInfo()
					.getLastMensesTime()));
			uInfo.setMensesCircle(mineInfo.getUserInfo().getMensesCircle());
			uInfo.setUserId(Long.valueOf(String.valueOf(mineInfo.getUserInfo()
					.getUserId())));
			uInfo.setWeight(mineInfo.getUserInfo().getWeight());
			uInfo.setHeadPic(mineInfo.getUserInfo().getHeadPic() == null ? ""
					: mineInfo.getUserInfo().getHeadPic());
			uInfo.setUniqueness(mineInfo.getUserInfo().getUniqueness());

			mApplication.setUserInfo(uInfo);
			mUserInfoDao.insertOrReplace(uInfo);
		}
	}

	/**
	 * 修改头像地址
	 * 
	 * @param headUrl
	 */
	public void updateHead(String headUrl) {
		UserInfo uInfo = null;
		mUserInfoDao.deleteAll();
		if (mApplication.getUserInfo() == null) {
			uInfo = new UserInfo();
		} else {
			uInfo = mApplication.getUserInfo();
		}
		uInfo.setHeadPic(headUrl);
		mApplication.setUserInfo(uInfo);
		mUserInfoDao.insertOrReplace(uInfo);
	}

	/**
	 * 退出登录
	 */
	public void outLogin() {
		mUserInfoDao.deleteAll();
		UserInfo uInfo = mApplication.getUserInfo();
		uInfo.setAccount("");
		uInfo.setPwd("");
		mApplication.setUserInfo(uInfo);
		mUserInfoDao.insertOrReplace(uInfo);
	}
}
