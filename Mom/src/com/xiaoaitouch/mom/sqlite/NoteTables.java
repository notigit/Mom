package com.xiaoaitouch.mom.sqlite;


/**笔记 属性表
 * @author Administrator
 *
 */
public class NoteTables {
	public static final String TABLE_NAME = "NoteTable";				//表名
	public static final int ROW_COUNTS = 5 ;							//表列数，更新数据包，添加表数据用(不包括自增ID)
	
	
	public static final String id = "ID" ; 
	public static final String title = "TITLE" ; 						//
	public static final String beaconName = "BEACON_NAME" ;				// 绑定的beacon 名
	public static final String content = "CONTENT" ;					// 文本信息
	public static final String audio = "AUDIO" ;						// 录音
	public static final String mode = "MODE";							// 提醒方式
	public static final String dateSave = "DATE_SAVE";					// 保存日期		yyyy-mm-dd
	public static final String date1 = "DATE1";							// 日期1
	public static final String date2 = "DATE2";							// 日期2
	public static final String time1 = "Time1";							// 闹铃时间段1
	public static final String time2 = "Time2";							// 闹铃时间段2
	
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" 	//创建该表SQL语句
								+ id + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
								+ title +" TEXT ,"
								+ beaconName +" TEXT ,"								
								+ content +" TEXT ,"
								+ audio +" TEXT ,"
								+ mode +" TEXT ,"
								+ dateSave +" TEXT ,"
								+ date1 +" TEXT ,"
								+ date2 +" TEXT ,"
								+ time1 +" TEXT ,"
								+ time2 +" TEXT "
								+ ");" ; 	
	
	
	
	
}
