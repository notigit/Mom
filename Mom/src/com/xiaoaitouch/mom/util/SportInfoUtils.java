package com.xiaoaitouch.mom.util;

import java.util.List;

import com.xiaoaitouch.mom.DatabaseMaster;
import com.xiaoaitouch.mom.dao.DaoSession;
import com.xiaoaitouch.mom.dao.ShareCardModel;
import com.xiaoaitouch.mom.dao.ShareCardModelDao;
import com.xiaoaitouch.mom.dao.SportInfoModel;
import com.xiaoaitouch.mom.dao.SportInfoModelDao;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 记录计步数据库操作类
 * 
 * @author huxin
 * 
 */
public class SportInfoUtils {
	private static DaoSession mDbSession;
	private static SportInfoUtils mInfoUtils;
	private SportInfoModelDao mSportInfoModelDao;

	public static SportInfoUtils instance() {
		if (mInfoUtils == null) {
			mInfoUtils = new SportInfoUtils();
			mDbSession = DatabaseMaster.instance().getMainDbSession();
			mInfoUtils.mSportInfoModelDao = mDbSession.getSportInfoModelDao();
		}
		return mInfoUtils;
	}

	private SportInfoUtils() {

	}

	/**
	 * 添加和修改数据
	 * 
	 * @param userId
	 * @param date
	 * @param sInfoModel
	 */
	public void saveSportInfo(long userId, String date,
			SportInfoModel sInfoModel) {
		SportInfoModel mSportInfoModel = mSportInfoModelDao
				.queryBuilder()
				.where(SportInfoModelDao.Properties.UserId.eq(userId),
						SportInfoModelDao.Properties.Date.eq(date)).unique();
		if (mSportInfoModel != null) {
			mSportInfoModel.setMessage(sInfoModel.getMessage());
			mSportInfoModel.setSteps(sInfoModel.getSteps());
			mSportInfoModel.setTime(sInfoModel.getTime());
			mSportInfoModelDao.update(mSportInfoModel);
		} else {
			mSportInfoModelDao.insert(sInfoModel);
		}
	}

	/**
	 * 查询当天运动步数
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public SportInfoModel querySportInfo(long userId, String date) {
		SportInfoModel mSportInfoModel = mSportInfoModelDao
				.queryBuilder()
				.where(SportInfoModelDao.Properties.UserId.eq(userId),
						SportInfoModelDao.Properties.Date.eq(date)).unique();
		return mSportInfoModel;
	}

	/**
	 * 获取未提交的运动信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<SportInfoModel> queryAllSportInfo(long userId) {
		List<SportInfoModel> mSportInfoModel = mSportInfoModelDao
				.queryBuilder()
				.where(SportInfoModelDao.Properties.UserId.eq(userId)).list();
		return mSportInfoModel;
	}

	/**
	 * 删除本地当前用户当前运动数据
	 * 
	 * @param userId
	 */
	public void deleteSportInfo(long userId) {
		QueryBuilder<SportInfoModel> qb = mSportInfoModelDao.queryBuilder();
		DeleteQuery<SportInfoModel> bd = qb.where(
				SportInfoModelDao.Properties.UserId.eq(userId)).buildDelete();
		bd.executeDeleteWithoutDetachingEntities();
	}

	/**
	 * 构造SportInfoModel
	 * 
	 * @param userId
	 * @param messsage
	 * @param date
	 * @param steps
	 * @param time
	 * @return
	 */
	public static SportInfoModel getSportInfoModel(long userId,
			String messsage, String date, int steps, float time) {
		SportInfoModel sInfoModel = new SportInfoModel();
		sInfoModel.setUserId(userId);
		sInfoModel.setDate(date);
		sInfoModel.setTime(time);
		sInfoModel.setSteps(steps);
		sInfoModel.setMessage(messsage);
		return sInfoModel;
	}

}
