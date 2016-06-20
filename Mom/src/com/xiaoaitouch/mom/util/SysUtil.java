package com.xiaoaitouch.mom.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class SysUtil {
	private static DisplayMetrics metrics = null;

	private static DisplayMetrics getDisplayMetrice() {
		if (null == metrics) {
			metrics = new DisplayMetrics();
			return metrics;
		} else {
			return metrics;
		}
	}

	/***
	 * 获取手机屏幕宽度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getPhoneWidth(Activity activity) {
		DisplayMetrics metrics = getDisplayMetrice();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int phoneWidth = metrics.widthPixels;
		return phoneWidth;
	}

	/***
	 * 获取手机屏幕高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getPhoneHeight(Activity activity) {
		DisplayMetrics metrics = getDisplayMetrice();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int phoneHeight = metrics.heightPixels;
		return phoneHeight;
	}

	/***
	 * 获取状态栏高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getStatusBarHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		if (statusBarHeight == 0) {
			statusBarHeight = 50;
		}
		return statusBarHeight;
	}

	/***
	 * bigmap转drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);

		return drawable;
	}
}
