package com.xiaoaitouch.mom.droid;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.xiaoaitouch.mom.net.SevenDoVolley;

public abstract class BaseApplication extends Application {
	public static BaseApplication sContext;
	public static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
		sContext = this;
		mContext = this.getApplicationContext();
		//初始化百度SDK
		SDKInitializer.initialize(this);
		SevenDoVolley.init(this);
	}

	protected abstract boolean debug();
}
