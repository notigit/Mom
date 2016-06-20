package com.xiaoaitouch.mom;

import com.xiaoaitouch.mom.dao.babychange.BabyChangeDao;
import com.xiaoaitouch.mom.util.DataBaseImport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseMaster {
	private static final String DB_NAME = "com.xiaoaitouch.db";
	private static DatabaseMaster sMaster;

	private com.xiaoaitouch.mom.dao.DaoMaster mMainMaster;
	private com.xiaoaitouch.mom.dao.DaoSession mMainDbSession;
	private com.xiaoaitouch.mom.dao.babychange.DaoSession mBabyChangeSession;

	public static final int BABY_DB_VERSION = 1;
	public static final String BABY_DB_NAME = "baby_change";

	private DatabaseMaster() {

	}

	public static DatabaseMaster instance() {
		if (sMaster == null) {
			sMaster = new DatabaseMaster();
		}

		return sMaster;
	}

	public DatabaseMaster init(Context context) {
		migrateDb(context);
		initMainDb(context);
		initBabyChangeDb(context);
		return this;
	}

	private void migrateDb(Context context) {
		String[] dbNames = { BABY_DB_NAME };
		int[] dbResources = { R.raw.baby_change };
		int[] versions = { BABY_DB_VERSION };
		new DataBaseImport(context).importDateBase(dbNames, dbResources,
				versions);
	}

	private void initMainDb(Context context) {
		com.xiaoaitouch.mom.dao.DaoMaster.DevOpenHelper helper = null;
		if (BuildConfig.DEBUG) {
			helper = new com.xiaoaitouch.mom.dao.DaoMaster.DevOpenHelper(
					context, DB_NAME, null) {
				@Override
				public void onCreate(SQLiteDatabase db) {
					super.onCreate(db);
				}
			};
		} else {
			helper = new com.xiaoaitouch.mom.dao.DaoMaster.DevOpenHelper(
					context, DB_NAME, null) {
				@Override
				public void onCreate(SQLiteDatabase db) {
					super.onCreate(db);
				}

				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion,
						int newVersion) {

				}
			};
		}

		mMainMaster = new com.xiaoaitouch.mom.dao.DaoMaster(
				helper.getWritableDatabase());
		mMainDbSession = mMainMaster.newSession();
	}

	private void initBabyChangeDb(Context context) {
		com.xiaoaitouch.mom.dao.babychange.DaoMaster.OpenHelper deptHelper = new com.xiaoaitouch.mom.dao.babychange.DaoMaster.OpenHelper(
				context, "baby_change", null) {

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {

			}
		};
		com.xiaoaitouch.mom.dao.babychange.DaoMaster deptMaster = new com.xiaoaitouch.mom.dao.babychange.DaoMaster(
				deptHelper.getWritableDatabase());
		mBabyChangeSession = deptMaster.newSession();
	}

	public BabyChangeDao getBabyChangeDao() {
		return mBabyChangeSession.getBabyChangeDao();
	}

	public com.xiaoaitouch.mom.dao.DaoSession getMainDbSession() {
		return mMainDbSession;
	}
}
