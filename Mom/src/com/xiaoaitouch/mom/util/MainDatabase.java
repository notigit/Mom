package com.xiaoaitouch.mom.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.bean.CardResult;
import com.xiaoaitouch.mom.bean.MainCardDataBean;
import com.xiaoaitouch.mom.bean.MainData;
import com.xiaoaitouch.mom.bean.MainDataBean;
import com.xiaoaitouch.mom.bean.MainDataModle;
import com.xiaoaitouch.mom.dao.DaoSession;
import com.xiaoaitouch.mom.dao.HoroscopeModel;
import com.xiaoaitouch.mom.dao.HoroscopeModelDao;
import com.xiaoaitouch.mom.dao.MSymptomModel;
import com.xiaoaitouch.mom.dao.MSymptomModelDao;
import com.xiaoaitouch.mom.dao.MWeightModel;
import com.xiaoaitouch.mom.dao.MWeightModelDao;
import com.xiaoaitouch.mom.dao.MscModel;
import com.xiaoaitouch.mom.dao.MscModelDao;
import com.xiaoaitouch.mom.dao.ShareCardModel;
import com.xiaoaitouch.mom.dao.ShareCardModelDao;
import com.xiaoaitouch.mom.dao.SportCardModel;
import com.xiaoaitouch.mom.dao.SportCardModelDao;
import com.xiaoaitouch.mom.data.model.MainUserModel;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 主页数据保存和读取
 * 
 * @author huxin
 * 
 */
public class MainDatabase {
	private static MomApplication mApplication;
	private static MainDatabase mainDatabase;
	private static DaoSession mDbSession;
	// 当周的用户数据
	private MWeightModelDao mWeightModelDao;
	private MSymptomModelDao mSymptomModelDao;
	private MscModelDao mscModelDao;
	// 获取卡片本地数据库
	private SportCardModelDao mSportCardModelDao;
	private ShareCardModelDao mShareCardModelDao;
	private HoroscopeModelDao mHoroscopeModelDao;

	private MainDatabase() {
	}

	public static MainDatabase instance(Activity activity) {
		if (mainDatabase == null) {
			mainDatabase = new MainDatabase();

			mApplication = (MomApplication) activity.getApplication();
			mDbSession = mApplication.getDaoSession();

			mainDatabase.mWeightModelDao = mDbSession.getMWeightModelDao();
			mainDatabase.mSymptomModelDao = mDbSession.getMSymptomModelDao();
			mainDatabase.mscModelDao = mDbSession.getMscModelDao();

			mainDatabase.mSportCardModelDao = mDbSession.getSportCardModelDao();
			mainDatabase.mShareCardModelDao = mDbSession.getShareCardModelDao();
			mainDatabase.mHoroscopeModelDao = mDbSession.getHoroscopeModelDao();
		}

		return mainDatabase;
	}

	/**
	 * 添加用户数据
	 * 
	 * @param userId
	 * @param date
	 */
	public void mainUserSave(long userId, String date,
			MainDataBean mMainDataBean, int day) {
		String mDate = "";
		for (int i = 0; i < day; i++) {
			mDate = StringUtils.getAddDate(date.split("-"), i);
			MWeightModel mWeightModel = mWeightModelDao
					.queryBuilder()
					.where(MWeightModelDao.Properties.UserId.eq(userId),
							MWeightModelDao.Properties.Date.eq(mDate)).unique();
			mWeightModel = mMainDataBean.getMw();
			if (mWeightModel != null) {
				mWeightModelDao.insertOrReplace(mWeightModel);
			}

			List<MSymptomModel> mSymptomModel = mSymptomModelDao
					.queryBuilder()
					.where(MSymptomModelDao.Properties.UserId.eq(userId),
							MSymptomModelDao.Properties.Date.eq(mDate)).list();
			mSymptomModel = mMainDataBean.getMs();
			if (mSymptomModel != null) {
				mSymptomModelDao.insertOrReplaceInTx(mSymptomModel);
			}

			List<MscModel> mscModel = mscModelDao
					.queryBuilder()
					.where(MscModelDao.Properties.UserId.eq(userId),
							MscModelDao.Properties.Date.eq(mDate)).list();
			mscModel = mMainDataBean.getMsc();
			if (mscModel != null) {
				mscModelDao.insertOrReplaceInTx(mscModel);
			}
		}
	}

	/**
	 * 获取date的用户数据
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public MainUserModel queryMainUserData(long userId, String date) {
		MainUserModel mainUserModel = null;
		MWeightModel mWeightModel = mWeightModelDao
				.queryBuilder()
				.where(MWeightModelDao.Properties.UserId.eq(userId),
						MWeightModelDao.Properties.Date.eq(date)).unique();
		if (mWeightModel != null) {
			mainUserModel = new MainUserModel();

			mainUserModel.mWeightModel = mWeightModel;

			mainUserModel.mSymptomModel = mSymptomModelDao
					.queryBuilder()
					.where(MSymptomModelDao.Properties.UserId.eq(userId),
							MSymptomModelDao.Properties.Date.eq(date)).list();

			mainUserModel.mscModel = mscModelDao
					.queryBuilder()
					.where(MscModelDao.Properties.UserId.eq(userId),
							MscModelDao.Properties.Date.eq(date)).list();
			return mainUserModel;
		} else {
			return null;
		}
	}

	/**
	 * 卡片信息保存
	 * 
	 * @param userId
	 * @param mMainDataBean
	 */
	public void mainCardSave(long userId, List<MainData> mainDatas) {
		if (mainDatas != null && mainDatas.size() > 0) {
			int dataSize = mainDatas.size();
			for (int i = 0; i < dataSize; i++) {
				// 运动卡片
				SportCardModel sportCardModel = mainDatas.get(i).getSi();
				if (sportCardModel != null && sportCardModel.getDate() != null) {
					SportCardModel model = new SportCardModel();
					model.setCardType(1);
					model.setDate(sportCardModel.getDate());
					model.setDueDays(sportCardModel.getDueDays());
					model.setSportMessage(sportCardModel.getSportMessage());
					model.setUserId(userId);
					model.setDescTime(mainDatas.get(i).getDescTime());
					addSportCardModel(userId, model);
				}
				// 分享卡片

				ShareCardModel shareCardModel = mainDatas.get(i).getFc();
				if (shareCardModel != null && shareCardModel.getDate() != null) {
					ShareCardModel model = new ShareCardModel();
					model.setCardType(2);
					model.setCreateTime(shareCardModel.getCreateTime());
					model.setDate(shareCardModel.getDate());
					model.setDueDays(shareCardModel.getDueDays());
					model.setFeeling(shareCardModel.getFeeling());
					model.setImg(shareCardModel.getImg());
					model.setLat(shareCardModel.getLat());
					model.setLng(shareCardModel.getLng());
					model.setLocation(shareCardModel.getLocation());
					model.setMessage(shareCardModel.getMessage());
					model.setType(shareCardModel.getType());
					model.setUserId(userId);
					model.setDescTime(mainDatas.get(i).getDescTime());
					model.setCardId(shareCardModel.getCardId());
					addShareCardModel(userId, model);
				}

				// 星座卡片
				HoroscopeModel horoscopeModel = mainDatas.get(i).getSc();
				if (horoscopeModel != null && horoscopeModel.getDate() != null) {
					HoroscopeModel model = new HoroscopeModel();
					model.setCardType(3);
					model.setDate(horoscopeModel.getDate());
					model.setDueDays(horoscopeModel.getDueDays());
					model.setHoroscope(horoscopeModel.getHoroscope());
					model.setMessage(horoscopeModel.getMessage());
					model.setStars(horoscopeModel.getStars());
					model.setUserId(userId);
					model.setDescTime(mainDatas.get(i).getDescTime());
					addHoroscopeModel(userId, model);
				}
			}
		}
	}

	/**
	 * 添加运动卡片
	 * 
	 * @param userId
	 * @param sportCardModel
	 */
	public void addSportCardModel(long userId, SportCardModel sportCardModel) {

		SportCardModel mSportCardModels = mSportCardModelDao
				.queryBuilder()
				.where(SportCardModelDao.Properties.UserId.eq(userId),
						SportCardModelDao.Properties.Date.eq(sportCardModel
								.getDate())).unique();
		if (mSportCardModels == null) {
			mSportCardModelDao.insertOrReplace(sportCardModel);
		}
	}

	/**
	 * 添加分享卡片
	 * 
	 * @param userId
	 * @param shareCardModel
	 */
	public void addShareCardModel(long userId, ShareCardModel shareCardModel) {
		ShareCardModel mShareCardModels = mShareCardModelDao
				.queryBuilder()
				.where(ShareCardModelDao.Properties.UserId.eq(userId),
						ShareCardModelDao.Properties.CardId.eq(shareCardModel
								.getCardId())).unique();
		if (mShareCardModels == null) {
			mShareCardModelDao.insertOrReplace(shareCardModel);
		}
	}

	/**
	 * 添加星座卡片
	 * 
	 * @param userId
	 * @param horoscopeModel
	 */
	public void addHoroscopeModel(long userId, HoroscopeModel horoscopeModel) {
		HoroscopeModel mHoroscopeModels = mHoroscopeModelDao
				.queryBuilder()
				.where(HoroscopeModelDao.Properties.UserId.eq(userId),
						HoroscopeModelDao.Properties.Date.eq(horoscopeModel
								.getDate())).unique();
		if (mHoroscopeModels == null) {
			mHoroscopeModelDao.insertOrReplace(horoscopeModel);
		}
	}

	/**
	 * 发送成功保存数据
	 * 
	 * @param userId
	 * @param cardResult
	 */
	public void sendAddShareCardModel(long userId, CardResult cardResult) {
		ShareCardModel model = new ShareCardModel();
		model.setCardType(cardResult.getFc().getType());
		model.setCreateTime(cardResult.getFc().getCreateTime());
		model.setDate(cardResult.getFc().getDate());
		model.setDueDays(cardResult.getFc().getDueDays());
		model.setFeeling(cardResult.getFc().getFeeling());
		model.setImg(cardResult.getFc().getImg() == null ? "" : cardResult
				.getFc().getImg());
		model.setLat(cardResult.getFc().getLat());
		model.setLng(cardResult.getFc().getLng());
		model.setLocation(cardResult.getFc().getLocation() == null ? ""
				: cardResult.getFc().getLocation());
		model.setMessage(cardResult.getFc().getMessage() == null ? ""
				: cardResult.getFc().getMessage());
		model.setType(cardResult.getFc().getType());
		model.setUserId(userId);
		model.setDescTime(cardResult.getFc().getCreateTime());
		model.setCardId(cardResult.getFc().getCardId());

		addShareCardModel(userId, model);
	}

	/**
	 * 获取最新的分享卡片
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public ShareCardModel queryShareCardModel(long userId, String date) {
		List<ShareCardModel> mShareCardModels = mShareCardModelDao
				.queryBuilder()
				.where(ShareCardModelDao.Properties.UserId.eq(userId),
						ShareCardModelDao.Properties.Date.eq(date))
				.orderDesc(ShareCardModelDao.Properties.DescTime).list();
		if (mShareCardModels != null && mShareCardModels.size() > 0) {
			return mShareCardModels.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 刷新当天的运动信息
	 * 
	 * @param userId
	 * @param sportCardModel
	 */
	public void refreshSportCardModel(long userId, SportCardModel sportCardModel) {
		SportCardModel model = new SportCardModel();
		model.setCardType(1);
		model.setDate(sportCardModel.getDate());
		model.setDueDays(sportCardModel.getDueDays());
		model.setSportMessage(sportCardModel.getSportMessage());
		model.setUserId(userId);
		model.setDescTime(sportCardModel.getDescTime());
		addSportCardModel(userId, model);
	}

	/**
	 * 查询这周数据
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public MainDataModle queryBeforeMainCardData(long userId, String date,
			int day) {
		MainDataModle mainDataModle = new MainDataModle();
		List<MainData> mainDatasList = new ArrayList<MainData>();
		List<HoroscopeModel> mHoroscopeModelsList = new ArrayList<HoroscopeModel>();
		mainDatasList.clear();
		mHoroscopeModelsList.clear();

		mainDataModle.setMainUserModel(queryMainUserData(userId,
				StringUtils.getAddDate(date.split("-"), day - 1)));
		String mDate = "";
		for (int i = 0; i < day; i++) {
			mDate = StringUtils.getAddDate(date.split("-"), (day - 1 - i));
			// 运动卡片
			SportCardModel mSportCardModel = mSportCardModelDao
					.queryBuilder()
					.where(SportCardModelDao.Properties.UserId.eq(userId),
							SportCardModelDao.Properties.Date.eq(mDate))
					.unique();
			if (mSportCardModel != null) {
				MainData mainData = new MainData();
				mainData.setSi(mSportCardModel);
				mainData.setType(1);
				mainData.setIndex(i);
				mainData.setDescTime(mSportCardModel.getDescTime());
				mainDatasList.add(mainData);
			}
			// 分享卡片
			List<ShareCardModel> mShareCardModels = mShareCardModelDao
					.queryBuilder()
					.where(ShareCardModelDao.Properties.UserId.eq(userId),
							ShareCardModelDao.Properties.Date.eq(mDate))
					.orderDesc(ShareCardModelDao.Properties.DescTime).list();
			if (mShareCardModels != null && mShareCardModels.size() > 0) {
				for (int j = 0; j < mShareCardModels.size(); j++) {
					ShareCardModel sCardModel = mShareCardModels.get(j);
					if (sCardModel != null) {
						MainData mainDataShare = new MainData();
						mainDataShare.setFc(sCardModel);
						mainDataShare.setType(2);
						mainDataShare.setIndex(i);
						mainDataShare.setDescTime(sCardModel.getDescTime());
						mainDatasList.add(mainDataShare);
					}
				}
			}
			// 星座卡片
			HoroscopeModel mHoroscopeModel = mHoroscopeModelDao
					.queryBuilder()
					.where(HoroscopeModelDao.Properties.UserId.eq(userId),
							HoroscopeModelDao.Properties.Date.eq(mDate))
					.unique();
			if (mHoroscopeModel != null) {
				MainData mainHspData = new MainData();
				mainHspData.setSc(mHoroscopeModel);
				mainHspData.setType(3);
				mainHspData.setIndex(i);
				mainHspData.setDescTime(mHoroscopeModel.getDescTime());
				mainDatasList.add(mainHspData);
				mHoroscopeModelsList.add(mHoroscopeModel);
			}
		}
		mainDataModle.setSc(mHoroscopeModelsList);
		mainDataModle.setMainDatas(mainDatasList);
		return mainDataModle;
	}

	/**
	 * 删除分享卡片
	 * 
	 * @param userId
	 * @param createTime
	 */
	public void deleteMainShareCard(long userId, long createTime) {
		QueryBuilder<ShareCardModel> qb = mShareCardModelDao.queryBuilder();
		DeleteQuery<ShareCardModel> bd = qb.where(
				ShareCardModelDao.Properties.UserId.eq(userId),
				ShareCardModelDao.Properties.CreateTime.eq(createTime))
				.buildDelete();
		bd.executeDeleteWithoutDetachingEntities();
	}

	public void deleteAllData() {
		mWeightModelDao.deleteAll();
		mSymptomModelDao.deleteAll();
	}
}
