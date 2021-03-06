package com.xiaoaitouch.mom.dao.babychange;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.xiaoaitouch.mom.dao.babychange.BabyChange;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BABY_CHANGE.
*/
public class BabyChangeDao extends AbstractDao<BabyChange, Long> {

    public static final String TABLENAME = "BABY_CHANGE";

    /**
     * Properties of entity BabyChange.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Week = new Property(1, String.class, "week", false, "WEEK");
        public final static Property Height = new Property(2, String.class, "height", false, "HEIGHT");
        public final static Property Weight = new Property(3, String.class, "weight", false, "WEIGHT");
        public final static Property Tips = new Property(4, String.class, "tips", false, "TIPS");
    };


    public BabyChangeDao(DaoConfig config) {
        super(config);
    }
    
    public BabyChangeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BABY_CHANGE' (" + //
                "'id' INTEGER PRIMARY KEY ," + // 0: id
                "'WEEK' TEXT," + // 1: week
                "'HEIGHT' TEXT," + // 2: height
                "'WEIGHT' TEXT," + // 3: weight
                "'TIPS' TEXT);"); // 4: tips
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BABY_CHANGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BabyChange entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String week = entity.getWeek();
        if (week != null) {
            stmt.bindString(2, week);
        }
 
        String height = entity.getHeight();
        if (height != null) {
            stmt.bindString(3, height);
        }
 
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(4, weight);
        }
 
        String tips = entity.getTips();
        if (tips != null) {
            stmt.bindString(5, tips);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BabyChange readEntity(Cursor cursor, int offset) {
        BabyChange entity = new BabyChange( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // week
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // height
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // weight
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // tips
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BabyChange entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setWeek(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHeight(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWeight(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTips(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(BabyChange entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(BabyChange entity) {
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
