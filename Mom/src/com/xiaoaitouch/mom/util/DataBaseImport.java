package com.xiaoaitouch.mom.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DataBaseImport {

    private final int BUFFER_SIZE = 400000;

    private String dbPath;

    private Context context;

    public DataBaseImport(Context context) {
        this.context = context;
        this.dbPath = "/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + context.getPackageName() + "/databases";
        Log.i("", "位置" + this.dbPath);
    }

    public void importDateBase(String[] dbNames, int[] dbResources, int[] versions) {

        if (dbNames.length != dbResources.length || dbNames.length != versions.length) {
            throw new RuntimeException("数据库名字和资源个数不匹配");
        }

        File dbPath = new File(this.dbPath);
        if (!dbPath.exists()) {
            dbPath.mkdirs();
        }

        String dbFile = "";
        File db = null;
        InputStream is = null;
        FileOutputStream fos = null;
        SQLiteDatabase sqliteDb = null;
        boolean needImport = false;
        for (int i = 0; i < dbResources.length; i++) {

            needImport = false;
            try {
                dbFile = this.dbPath + "/" + dbNames[i];
                db = new File(dbFile);
                if (db.exists()) {
                    sqliteDb = SQLiteDatabase.openOrCreateDatabase(db, null);
                    if (sqliteDb.getVersion() < versions[i]) {
                        // 有新版本号
                        needImport = true;
                        sqliteDb.close();
                        sqliteDb = null;
                        db.delete();
                    }
                } else {
                    needImport = true;
                }
                if (needImport) {
                    db.createNewFile();
                    // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                    is = this.context.getResources().openRawResource(
                            dbResources[i]); // 欲导入的数据库
                    fos = new FileOutputStream(db);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count = 0;
                    while ((count = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, count);
                    }
                    fos.close();
                    is.close();
                    sqliteDb = SQLiteDatabase.openOrCreateDatabase(db, null);
                    sqliteDb.setVersion(versions[i]);
                }
            } catch (FileNotFoundException e) {
                Log.e("Database", "File not found");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Database", "IO exception");
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (sqliteDb != null) {
                    sqliteDb.close();
                    sqliteDb = null;
                }
            }
        }

    }

}
