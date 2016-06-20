package com.xiaoaitouch.mom.droid;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.io.FileUtils;

import com.xiaoaitouch.mom.util.Logger;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;


public class CrashHandler implements UncaughtExceptionHandler {
    private final Application mApplication;
    private Handler mUIHandler;
    private Thread mUiThread;

    public CrashHandler(Application app) {
        mApplication = app;
        mUIHandler = new Handler();
        mUiThread = Thread.currentThread();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        writeCrashInfoToFile(e);

        if (Thread.currentThread() != mUiThread) {
            mUIHandler.post(new Runnable() {

                @Override
                public void run() {
                    mApplication.onTerminate();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            });
        } else {
            mApplication.onTerminate();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    private void writeCrashInfoToFile(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        Throwable cause = t.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        String crashInfo = sw.toString();
        Logger.e(crashInfo);
        pw.close();

        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = mApplication.getApplicationContext().getExternalCacheDir();
                if (file != null) {
                    file = FileUtils.getFile(file, "crash");
                    if (file.exists() || file.isDirectory()) {
                        file.delete();
                    }
                    file.createNewFile();
                    FileUtils.writeStringToFile(file, crashInfo, "utf-8");
                }
            }
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
    }

}
