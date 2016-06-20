package com.xiaoaitouch.mom.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Validation {
	/**
	 * 判断是否是手机号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		return str.matches("^0?1[34586]{1}[0-9]{9}");
	}

	/**
	 * 验证是否是电话.
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isPhoneNumber(String paramString) {
		if (isMobile(paramString)) {
			return true;
		} else if (paramString.matches("^0{1,2}[1-9][0-9]{1,2}[0-9]{7,8}")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为null 和 ""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 判断字符串是否为null 和 ""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullTrim(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return boolean
	 */
	public static boolean checkSDCard(Context context) {
		boolean isSDCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!isSDCardExist && context != null) {
			Toast.makeText(context, "没有找到SD卡", 1).show();
		}
		return isSDCardExist;
	}
}
