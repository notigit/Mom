package com.xiaoaitouch.mom.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.xiaoaitouch.mom.dao.SymptomsDao;
import com.xiaoaitouch.mom.dao.UserInfoDao;
import com.xiaoaitouch.mom.dao.SportInfoModelDao;
import com.xiaoaitouch.mom.dao.SportCardModelDao;
import com.xiaoaitouch.mom.dao.HoroscopeModelDao;
import com.xiaoaitouch.mom.dao.ShareCardModelDao;
import com.xiaoaitouch.mom.dao.MWeightModelDao;
import com.xiaoaitouch.mom.dao.MSymptomModelDao;
import com.xiaoaitouch.mom.dao.MscModelDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1000): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1000;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        SymptomsDao.createTable(db, ifNotExists);
        UserInfoDao.createTable(db, ifNotExists);
        SportInfoModelDao.createTable(db, ifNotExists);
        SportCardModelDao.createTable(db, ifNotExists);
        HoroscopeModelDao.createTable(db, ifNotExists);
        ShareCardModelDao.createTable(db, ifNotExists);
        MWeightModelDao.createTable(db, ifNotExists);
        MSymptomModelDao.createTable(db, ifNotExists);
        MscModelDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        SymptomsDao.dropTable(db, ifExists);
        UserInfoDao.dropTable(db, ifExists);
        SportInfoModelDao.dropTable(db, ifExists);
        SportCardModelDao.dropTable(db, ifExists);
        HoroscopeModelDao.dropTable(db, ifExists);
        ShareCardModelDao.dropTable(db, ifExists);
        MWeightModelDao.dropTable(db, ifExists);
        MSymptomModelDao.dropTable(db, ifExists);
        MscModelDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(SymptomsDao.class);
        registerDaoClass(UserInfoDao.class);
        registerDaoClass(SportInfoModelDao.class);
        registerDaoClass(SportCardModelDao.class);
        registerDaoClass(HoroscopeModelDao.class);
        registerDaoClass(ShareCardModelDao.class);
        registerDaoClass(MWeightModelDao.class);
        registerDaoClass(MSymptomModelDao.class);
        registerDaoClass(MscModelDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}