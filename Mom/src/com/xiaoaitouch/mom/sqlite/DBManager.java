package com.xiaoaitouch.mom.sqlite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/** 本地SQL
 * 将raw中得数据库文件写入到data数据库中
 * @author sy
 *
 */
public class DBManager
{
	private final int BUFFER_SIZE = 1024 * 1024 * 2;
	private static final String PACKAGE_NAME = "com.afd.crt.app";
	public static final String DB_NAME = "crt.db";
	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() 
											+ "/" + PACKAGE_NAME ; // 存放路径
	
	public static final String DOWNLOAD_DATA = "data.db"	;	   //下载的数据库文件名
	
	private Context mContext;
	private SQLiteDatabase database;

	public DBManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 被调用方法
	 */
	public void openDateBase()
	{
//		this.database = this.openDateBase(DB_PATH + "/" + DB_NAME);
	}
	
	/**打开程序本地
	 * 
	 */
	public void openDateLocal(){
		this.database = this.openDateBaseLocal(DB_PATH + "/" + DB_NAME);
	}

	/**
	 * 打开数据库
	 * 
	 * @param dbFile
	 * @return SQLiteDatabase
	 * @author sy
	 */
	private SQLiteDatabase openDateBase(String dbFile , String path)
	{
		File file = new File(dbFile);
		if (file.exists()) {
			file.delete();
		}
		
		if (!file.exists())
		{
			// // 打开raw中得数据库文件，获得stream流
			InputStream stream = null;
			try {
				stream = new FileInputStream(new File(path));
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try
			{
				
				// 将获取到的stream 流写入道data中
				FileOutputStream outputStream = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = stream.read(buffer)) > 0)
				{
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
				stream.close();
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
				
				return db;
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return database;
	}
	
	/**读取本地数据库文件
	 * @param dbFile
	 * @return
	 */
	private SQLiteDatabase openDateBaseLocal(String dbFile)
	{
		File file = new File(dbFile);
		if (!file.exists())
		{
			// // 打开raw中得数据库文件，获得stream流
			InputStream stream = null ;
//			InputStream stream = this.mContext.getResources().openRawResource(R.raw.data);
			try
			{
				// 将获取到的stream 流写入道data中
				FileOutputStream outputStream = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = stream.read(buffer)) > 0)
				{
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
				stream.close();
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
				return db;
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return database;
	}

	public void closeDatabase()
	{
		if (database != null && database.isOpen())
		{
			this.database.close();
		}
	}
}
