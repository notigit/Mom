package com.xiaoaitouch.mom.sqlite;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper {

  private Context context;

  private static final String TAG = "sqlite"; // 调试标签

  public DatabaseOpenHelper openHelper;

  /**
   * @param context
   * @param table
   *          表名
   */
  public DBHelper(Context context) {
    // TODO Auto-generated constructor stub
    this.context = context;
    openHelper = new DatabaseOpenHelper(context);
    // db =
    // context.openOrCreateDatabase(DATABASE_NAME,Context.MODE_PRIVATE,null);
    // CreateTable();
  }

  public DBHelper(Context context, int dataVersion) {
    // TODO Auto-generated constructor stub
    this.context = context;
    openHelper = new DatabaseOpenHelper(context, dataVersion);
    // db =
    // context.openOrCreateDatabase(DATABASE_NAME,Context.MODE_PRIVATE,null);
    // CreateTable();
  }

  /**
   * 建表 列名 区分大小写？ 都有什么数据类型？ SQLite 3 TEXT 文本 NUMERIC 数值 INTEGER 整型 REAL 小数 NONE
   * 无类型 查询可否发送select ?
   */
  public boolean CreateTable(String sql) {
    SQLiteDatabase db = openHelper.getWritableDatabase();
    try {
      db.execSQL(sql);
      db.setTransactionSuccessful();
      db.close();
      Log.v(TAG, sql);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 增加数据
   * 
   * @param table
   * @param arrayKey
   *          字段 数组
   * @param arrayValues
   *          与字段相应的值数组
   * @return
   */
  public boolean insert(String table, String[] arrayKey, String[] arrayValues) {
    String key = "";
    String values = "";
    for (int i = 0; i < arrayKey.length; i++) {
      key += arrayKey[i] + ",";
      values += "'" + arrayValues[i] + "'" + ",";
    }
    String sql = "INSERT INTO " + table + "(" + key.substring(0, key.length() - 1) + ") " + "VALUES("
        + values.substring(0, values.length() - 1) + ");";

    SQLiteDatabase db = openHelper.getWritableDatabase();
    try {
      db.beginTransaction();
      db.execSQL(sql);
      db.setTransactionSuccessful();
      db.endTransaction();
      db.close();
      Log.v(TAG, sql);
      return true;
    } catch (Exception e) {
      System.out.println("Exception sql Insert>>>>>>>>>" + sql + e.getMessage());
      return false;
    }

  }

  /**
   * 替换数据
   * 
   * @param table
   * @param arrayKey
   *          字段 数组
   * @param arrayValues
   *          与字段相应的值数组
   * @return
   */
  public boolean replace(String table, String[] arrayKey, String[] arrayValues) {
    String key = "";
    String values = "";
    for (int i = 0; i < arrayKey.length; i++) {
      key += arrayKey[i] + ",";
      values += "'" + arrayValues[i] + "'" + ",";
    }
    String sql = "REPLACE INTO " + table + "(" + key.substring(0, key.length() - 1) + ") " + "VALUES("
        + values.substring(0, values.length() - 1) + ");";
    System.out.println("sql----->"+sql);
    SQLiteDatabase db = openHelper.getWritableDatabase();
    try {
      db.execSQL(sql);
      db.setTransactionSuccessful();
      db.close();
      Log.v(TAG, sql);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 更新数据
   * 
   * @param table
   *          表名
   * @param keys
   * @param values
   * @param whereKey
   *          索引键
   * @param whereValues
   *          索引值
   * @return
   */
  public boolean update(String table, String[] keys, String[] values, String whereKey, String whereValues) {
    String sql_content = "";
    for (int i = 0; i < keys.length; i++) {
      if (i == (keys.length - 1)) {
        sql_content += keys[i] + "='" + values[i] + "' ";
      } else {
        sql_content += keys[i] + "='" + values[i] + "', ";
      }
    }
    sql_content = sql_content + "WHERE " + whereKey + " = '" + whereValues + "'";
    String sql = "UPDATE " + table + " SET " + sql_content + ";";

    SQLiteDatabase db = openHelper.getWritableDatabase();
    try {
      db.execSQL(sql);
      db.setTransactionSuccessful();
      db.close();
      Log.v(TAG, sql);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 删除一项数据
   * 
   * @param table
   *          表名
   * @param id
   *          ID字段 (主键)
   * @param values
   *          对应ID值
   * @return
   */
  public boolean delete(String table, String id, String values) {
    String sql;
    if (id == null || id.equals("")) {
      sql = "DELETE FROM " + table + ";";
    } else {
      sql = "DELETE FROM " + table + " WHERE " + id + "='" + values + "';";
    }
    SQLiteDatabase db = openHelper.getWritableDatabase();
    try {
      db.execSQL(sql);
      db.setTransactionSuccessful();
      db.close();
      Log.v(TAG, sql);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 查询、获取数据
   * 
   * @param table
   *          表名
   * @param keyID
   *          查询ID 字段
   * @param valuesID
   *          查询ID 字段
   * @return
   */
  public Cursor query(String table, String keyID, String valuesID) {
    String values = "'" + valuesID + "'";
    Cursor cursor;
    SQLiteDatabase db = openHelper.getReadableDatabase();
    // 若fileId为null或""则查询所有记录
    if (valuesID == null || valuesID == null) {
      cursor = db.rawQuery("select * from " + table, null);
    } else {
      cursor = db.rawQuery("select * from " + table + " WHERE " + keyID + "= " + values + ";", null);
    }
    return cursor;
  }

  /**
   * 倒序查询、获取数据
   * 
   * @param table
   *          表名
   * @param keyID
   *          查询ID 字段
   * @param valuesID
   *          查询ID 字段
   * @return
   */
  public Cursor orderQuery(String table, String keyID, String valuesID) {
    String values = "'" + valuesID + "'";
    Cursor cursor;
    SQLiteDatabase db = openHelper.getReadableDatabase();
    // 若fileId为null或""则查询所有记录
    if (valuesID == null || valuesID == null) {
      cursor = db.rawQuery("select * from " + table + " order by ID desc", null);
    } else {
      cursor = db.rawQuery("select * from " + table + " WHERE " + keyID + "= " + values + ";", null);
    }
    return cursor;
  }

  /**
   * 倒序查询、获取数据
   * 
   * @param table
   *          表名
   * @param keyID
   *          查询ID 字段
   * @param valuesID
   *          查询ID 字段
   * @return
   */
  public Cursor orderQuery(String table, String keyID, String valuesID, String orderby, String order) {
    String values = "'" + valuesID + "'";
    Cursor cursor;
    SQLiteDatabase db = openHelper.getReadableDatabase();
    // 若fileId为null或""则查询所有记录
    if (valuesID == null || valuesID == null) {
      cursor = db.rawQuery("select * from " + table + " order by ID desc", null);
    } else {
      cursor = db.rawQuery("select * from " + table + " WHERE " + keyID + "= " + values + " order by " + orderby + " "
          + order + ";", null);
    }
    return cursor;
  }

  /**
   * cql 语句
   * 
   * @param sql
   * @return
   */
  public Cursor query(String sql) {
    Cursor cursor;
    SQLiteDatabase db = openHelper.getReadableDatabase();
    cursor = db.rawQuery(sql, null);
    return cursor;
  }

  /**
   * cql 语句
   * 
   * @param sql
   * @return
   */
  public void TruncateTable(String tabName) {

    SQLiteDatabase db = openHelper.getReadableDatabase();

    String sql = "DELETE FROM " + tabName + ";";

    db.execSQL(sql);
    revertSeq(tabName);

  }

  public void revertSeq(String tabName) {
    String sql = "update sqlite_sequence set seq=0 where name='" + tabName + "'";
    SQLiteDatabase db = openHelper.getWritableDatabase();
    db.execSQL(sql);

  }
}
