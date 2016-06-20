package com.xiaoaitouch.mom.util;

import java.util.UUID;

import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.droid.BaseApplication;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Utils {
	private static Toast sToast = null;

	private static String channel;

	public static void showToast(CharSequence text, int duration) {
		if (sToast == null) {
			sToast = Toast.makeText(BaseApplication.sContext, text, duration);
		} else {
			sToast.setText(text);
		}

		sToast.show();
	}

	public static void showToast(int resId, int duration) {
		showToast(BaseApplication.sContext.getString(resId), duration);
	}

	public static int getScreenWidth(Context context) {
		int width = 0;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		return width;
	}

	public static int getScreenHeight(Context context) {
		int height = 0;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		height = dm.heightPixels;
		return height;
	}

	private static PackageInfo getPackageInfo() {
		PackageManager packageManager = BaseApplication.sContext
				.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(
					BaseApplication.sContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return packInfo;
	}

	public static String getVersionName() {

		PackageInfo packageInfo = getPackageInfo();
		if (packageInfo != null) {
			return packageInfo.versionName;
		} else {
			return "";
		}

	}

	public static int getVersionCode() {
		PackageInfo packageInfo = getPackageInfo();

		if (packageInfo != null) {
			return packageInfo.versionCode;
		} else {
			return 0;
		}
	}

	public static String getImei() {
		TelephonyManager tm = (TelephonyManager) BaseApplication.sContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();

		return imei;
	}

	public static String getPhoneType() {
		String phoneType = Build.MODEL;

		return phoneType = phoneType.replace(" ", "-");
	}

	public static String getSecureId() {
		return Secure.ANDROID_ID;
	}

	public static String getOsVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getMacAddr() {
		WifiManager wifi = (WifiManager) BaseApplication.sContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();

		return info.getMacAddress();
	}

	public static boolean checkPhoneNumber(String paramString) {
		if (paramString.matches("^0?1[34586]{1}[0-9]{9}")) {
			return true;
		} else if (paramString.matches("^0{1}[0-9]{2,3}[0-9]{7,8}")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getChannel() {
		if (TextUtils.isEmpty(channel)) {
			channel = getAppMetaData("UMENG_CHANNEL");
		}
		return channel;
	}

	public static String getAppID() {
		return "com.xiaoaitouch.mom";
	}

	/**
	 * 获取application中指定的meta-data
	 * 
	 * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
	 */
	private static String getAppMetaData(String key) {
		if (TextUtils.isEmpty(key)) {
			return null;
		}
		String resultData = null;
		try {
			PackageManager packageManager = BaseApplication.sContext
					.getPackageManager();
			if (packageManager != null) {
				ApplicationInfo applicationInfo = packageManager
						.getApplicationInfo(
								BaseApplication.sContext.getPackageName(),
								PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						resultData = applicationInfo.metaData.getString(key);
					}
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return resultData;
	}

	public static void playNotificationSound() {
		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(BaseApplication.sContext,
				notification);
		r.play();
	}

	
	private static final String UNIQUENESS = "uniqueness";

	public static float dp2px(Resources resources, float dp) {
		final float scale = resources.getDisplayMetrics().density;
		return dp * scale + 0.5f;
	}

	public static float sp2px(Resources resources, float sp) {
		final float scale = resources.getDisplayMetrics().scaledDensity;
		return sp * scale;
	}
	
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  

	/**
	 * 虚化
	 * 
	 * @param bitmap
	 *            原图
	 * @param view
	 *            需要设置背景的view
	 * @param scaleFactor
	 *            图片缩放倍数(缩放越大，占资源越小)
	 * @param radius
	 *            模糊半径(半径越大，占资源越大，模糊效果越好)
	 * @param res
	 * @return drawable
	 */
	public static Drawable blur(Bitmap bitmap, View view, float scaleFactor,
			int radius, Resources res) {
		// float radius = 50;
		view.setDrawingCacheEnabled(true);
		Bitmap overlay = Bitmap.createBitmap(
				(int) (view.getMeasuredWidth() / scaleFactor),
				(int) (view.getMeasuredHeight() / scaleFactor),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		overlay = net.qiujuer.genius.blur.StackBlur.blurNatively(overlay,
				radius, true);
		// overlay = FastBlur.doBlur(overlay, (int) radius, true);
		// overlay.recycle();
		return new BitmapDrawable(res, overlay);
	}

	/**
	 * 虚化
	 * 
	 * @param bitmap
	 *            原图
	 * @param view
	 *            需要设置背景的view
	 * @param scaleFactor
	 *            图片缩放倍数(缩放越大，占资源越小)
	 * @param radius
	 *            模糊半径(半径越大，占资源越大，模糊效果越好)
	 * @param res
	 * @return drawable
	 */
	@SuppressWarnings("deprecation")
	public static void setBlurBg(Bitmap bitmap, View view, float scaleFactor,
			int radius, Resources res) {
		view.setBackgroundDrawable(blur(bitmap, view, scaleFactor, radius, res));
	}

	public static String createUniqueness() {
		String uniqueness = SharedPreferencesUtil.getString(
				MomApplication.sContext, UNIQUENESS, "");
		if (!TextUtils.isEmpty(uniqueness)) {
			return uniqueness;
		}
		uniqueness = createUniquenessByPhone();
		SharedPreferencesUtil.putString(MomApplication.sContext, UNIQUENESS,
				uniqueness);

		return uniqueness;
	}

	private static String createUniquenessByPhone() {

		TelephonyManager tm = (TelephonyManager) MomApplication.sContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String lineNumber = tm.getLine1Number();
		if (lineNumber != null && !"".equals(lineNumber)) {
			lineNumber = lineNumber.replace("+86", "").replace("-", "");
			if (Validation.isMobile(lineNumber)) {
				return "tel:" + lineNumber;
			}
		}
		String imsi = tm.getSubscriberId();
		if (imsi != null && imsi.length() > 4) {
			return "imsi:" + imsi;
		}
		int type = tm.getPhoneType();
		String diviceId = tm.getDeviceId();
		if (diviceId != null && diviceId.length() > 4) {
			if (type == TelephonyManager.PHONE_TYPE_GSM) {
				return "imei:" + diviceId;
			} else if (type == TelephonyManager.PHONE_TYPE_CDMA) {
				return "meid:" + diviceId;
			}
		}

		WifiManager wifi = (WifiManager) MomApplication.sContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		if (mac != null && mac.length() > 4) {
			return "mac:" + mac;
		}

		UUID uuid = UUID.randomUUID();
		return "uuid:" + uuid.toString();

	}
}
