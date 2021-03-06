package com.xiaoaitouch.mom.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.xiaoaitouch.mom.dao.MscModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MSC_MODEL.
*/
public class MscModelDao extends AbstractDao<MscModel, Long> {

    public static final String TABLENAME = "MSC_MODEL";

    /**
     * Properties of entity MscModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property UserId = new Property(1, Long.class, "userId", false, "USER_ID");
        public final static Property IsOk = new Property(2, Integer.class, "isOk", false, "IS_OK");
        public final static Property Message = new Property(3, String.class, "message", false, "MESSAGE");
        public final static Property Date = new Property(4, String.class, "date", false, "DATE");
    };


    public MscModelDao(DaoConfig config) {
        super(config);
    }
    
    public MscModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MSC_MODEL' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'USER_ID' INTEGER," + // 1: userId
                "'IS_OK' INTEGER," + // 2: isOk
                "'MESSAGE' TEXT," + // 3: message
                "'DATE' TEXT);"); // 4: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MSC_MODEL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MscModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }
 
        Integer isOk = entity.getIsOk();
        if (isOk != null) {
            stmt.bindLong(3, isOk);
        }
 
        String message = entity.getMessage();
        if (message != null) {
            stmt.bindString(4, message);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(5, date);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MscModel readEntity(Cursor cursor, int offset) {
        MscModel entity = new MscModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // isOk
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // message
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // date
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MscModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setIsOk(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setMessage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MscModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MscModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
