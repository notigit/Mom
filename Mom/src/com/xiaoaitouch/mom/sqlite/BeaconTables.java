package com.xiaoaitouch.mom.sqlite;

import java.util.ArrayList;

import com.xiaoaitouch.mom.wheelview.BeaconInfo;

import android.content.Context;
import android.database.Cursor;

/**Beacon 属性表
 * @author Administrator
 *
 */
public class BeaconTables {
	public static final String TABLE_NAME = "BeaconTable";				//表名
	public static final int ROW_COUNTS = 5 ;							//表列数，更新数据包，添加表数据用(不包括自增ID)
	
	
	public static final String id = "ID" ; 
	public static final String uuid = "UUID" ; 							// 
	public static final String mac = "MAC";								//
	public static final String name = "NAME" ; 	
	public static final String power = "POWER";							// 
	
	public static final String low_battery = "LOW_BATTERY" ; 			//	
	public static final String createdate = "CREATEDATE";				//
	public static final String last_connect_date = "LAST_CONNECT_DATE";	//
	public static final String desc = "DESC" ; 							//
	public static final String last_broken_time = "LAST_BROKEN_TIME";	//
	
	
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" 	//创建该表SQL语句
								+ id + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
								+ uuid +" TEXT ,"
								+ mac +" TEXT ,"
								+ name +" TEXT ,"
								+ low_battery +" TEXT ,"
								+ createdate +" TEXT ,"
								+ last_connect_date +" TEXT ,"
								+ desc +" TEXT ,"
								+ last_broken_time +" TEXT ,"
								+ power + " INTEGER"
								+ ");" ; 	
	
	
	/** 获取设备信息
	 * @param context
	 * @return
	 */
	public static ArrayList<BeaconInfo> getBeaconList(Context context){
		DBHelper dbHelper = new DBHelper(context);
		ArrayList<BeaconInfo> arrayList = new ArrayList<BeaconInfo>();
		
		Cursor cursor = dbHelper.query(TABLE_NAME, null, null);
		while (cursor.moveToNext()) {
			BeaconInfo info = new BeaconInfo();
			info.setUuid(cursor.getString(cursor.getColumnIndex(BeaconTables.uuid)));
			info.setDesc(cursor.getString(cursor.getColumnIndex(BeaconTables.desc)));
			arrayList.add(info);
		}
		cursor.close();
		dbHelper.openHelper.close();
		return arrayList;
	}
	
	/** 添加设备信息
	 * @param context
	 * @param info
	 * @return
	 */
	public static boolean addBeacons(Context context , BeaconInfo info){
		DBHelper dbHelper = new DBHelper(context);
		String[] arrayKey = {uuid , low_battery , createdate , last_connect_date , desc , last_broken_time};
		String[] arrayValues = {info.getUuid() , info.getLow_battery() , info.getCreatedate() 
				, info.getLast_connect_date() , info.getDesc() , info.getLast_broken_time()};
		return dbHelper.insert(TABLE_NAME, arrayKey, arrayValues);
	}
	
	/** 删除设备信息
	 * @param context
	 * @param info
	 * @return
	 */
	public static boolean delBeacons(Context context , BeaconInfo info){
		DBHelper dbHelper = new DBHelper(context);
		return dbHelper.delete(TABLE_NAME, uuid, info.getUuid());
	}
	
}
