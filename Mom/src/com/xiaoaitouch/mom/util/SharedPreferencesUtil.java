package com.xiaoaitouch.mom.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences util class
 * 
 * @author huxin
 * 
 */
public class SharedPreferencesUtil {

	private static SharedPreferences sharedPreferences = null;

	public static void putString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(Context context, String key,
			String defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		return sharedPreferences.getString(key, defaultValue);
	}

	public static void putBoolean(Context context, String key, Boolean value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static Boolean getBoolean(Context context, String key,
			Boolean defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static void putLong(Context context, String key, long value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		return sharedPreferences.getLong(key, defaultValue);
	}

	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static void putInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static synchronized SharedPreferences getSharedPreferences(
			Context context) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences("yfx",
					Context.MODE_APPEND);
		}
		return sharedPreferences;
	}

}
