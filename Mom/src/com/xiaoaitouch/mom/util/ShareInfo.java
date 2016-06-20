package com.xiaoaitouch.mom.util;

import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareInfo implements Serializable {
	public static final String TAG_PREFERENCES = "p_user";
	public static final String TAG_SCREEN_WIDTH = "width";
	public static final String TAG_SCREEN_HEIGHT = "height";

	public static final String TAG_BLE_MAC = "ble_mac"; // 连接设备地址

	public static void saveTagString(Context context, String key, String values) {
		SharedPreferences preferences = context.getSharedPreferences(TAG_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, values);
		editor.commit();
	}

	public static String getTagString(Context context, String tag) {
		return context.getSharedPreferences(TAG_PREFERENCES, Context.MODE_PRIVATE).getString(tag, null);
	}

	public static void saveTagInt(Context context, String key, int values) {
		SharedPreferences preferences = context.getSharedPreferences(TAG_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(key, values);
		editor.commit();
	}

	public static int getTagInt(Context context, String tag) {
		return context.getSharedPreferences(TAG_PREFERENCES, Context.MODE_PRIVATE).getInt(tag, 0);
	}
}
