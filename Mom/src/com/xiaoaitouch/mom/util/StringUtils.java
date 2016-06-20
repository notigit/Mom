package com.xiaoaitouch.mom.util;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

import com.xiaoaitouch.mom.bean.CalendarDate;
import com.xiaoaitouch.mom.bean.Calendars;

/**
 * 字符串工具类
 * 
 * @author huxin
 * 
 */
public class StringUtils {
	private static final String TAG = "StringUtils";

	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;
		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();
		if (obj instanceof Map)
			return ((Map) obj).isEmpty();
		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 检查字符串的长度限制
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkLength(String str, int mini, int maxi) {
		int len = str.length();
		return mini > len || len > maxi;
	}

	/**
	 * 用户生日格式化
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @return
	 */
	public static String userBirthdayFormat(int year, int month, int dayOfMonth) {
		StringBuffer sb = new StringBuffer();
		String separator = "-";
		sb.append(year);
		sb.append(separator);
		if (month < 10) {
			sb.append(0);
			sb.append(month);
		} else {
			sb.append(month);
		}
		sb.append(separator);
		if (dayOfMonth < 10) {
			sb.append(0);
			sb.append(dayOfMonth);
		} else {
			sb.append(dayOfMonth);
		}
		return sb.toString();
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return
	 */
	public static Date strConvertDate(String str, String parten) {
		SimpleDateFormat format = new SimpleDateFormat(parten);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取当前时间的秒数
	 * 
	 * @return
	 */
	public static String getCurrentTimeSS() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 返回一个 date的小时数
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getHHOfDate(long time) {
		// str = "yyyy-MM-dd HH:mm:ss";
		String str = "HH";
		Date date = new Date(time);
		java.text.SimpleDateFormat simpleDate = new java.text.SimpleDateFormat(
				str);
		String currentDate = simpleDate.format(date);
		return currentDate;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	public static String getSysDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
				System.currentTimeMillis()));
	}

	public static String convertDate(long time) {
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return sfd.format(date);
	}

	public static String convertDate(long time, String parten) {
		SimpleDateFormat sfd = new SimpleDateFormat(parten);
		Date date = new Date(time);
		return sfd.format(date);
	}

	/**
	 * "2013年05月06日","yyyy年MM月dd日", "yyyyMMdd"
	 * 
	 * @param date
	 * @param currParten
	 * @param toParten
	 * @return
	 */
	public static String convertDate(String date, String currParten,
			String toParten) {
		String str = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(currParten);
			Date d = format.parse(date);
			SimpleDateFormat sfd = new SimpleDateFormat(toParten);
			str = sfd.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 向日期增加小时
	 * 
	 * @param hours
	 * @param date
	 * @return
	 */
	public static String addHoursToDate(int hours, Date date) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		Date temp = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		String dateTime = sdf.format(temp);
		return dateTime;
	}

	/**
	 * 检查字符串中的特殊字符（限制成数字与字母）
	 * 
	 * @return
	 */
	public static boolean hasSpecialChar(String str) {
		String regex = "[A-Za-z0-9]+$";
		return !str.matches(regex);
	}

	public static boolean checkSpecialChar(String str) {
		// String regex =
		// "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\r\\n]";
		// str.split("\\s");
		// String regex = "[\\r\\n\\s]";
		// Pattern p = Pattern.compile(regex);
		// Matcher m = p.matcher(str);
		// return m.find();
		String regex = "[\\S]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (!m.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否属于手机号码
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isPhoneNum(String num) {
		Pattern p = Pattern.compile("^[1][0-9]{10}$");
		Matcher m = p.matcher(num);
		return m.matches();
	}

	/**
	 * 11位手机号码 3-4位区号，7-8位直播号码，1－4位分机号
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isPhoneNumOrTel(String num) {
		boolean flag = false;
		Pattern p1 = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p2 = Pattern.compile("(^(\\d{3,4}-)?\\d{7,8})$");
		// Pattern p3 = Pattern.compile("^\\d{11}$");
		Matcher m1 = p1.matcher(num);
		Matcher m2 = p2.matcher(num);
		// Matcher m3 = p3.matcher(num);
		String[] str = { "110", "120", "114", "10086", "10010" };
		if (m1.matches()) {
			flag = true;
		} else {
			if (m2.matches()) {
				flag = true;
			} else {
				if (Arrays.toString(str).contains(num)) {
					flag = true;
				} else {
					if (num.matches("[0-9]+")) {
						flag = true;
						if (num.length() > 18) {
							flag = false;
						}
					} else {
						flag = false;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 去掉+86
	 * 
	 * @param prefix
	 * @param number
	 * @return
	 */
	public static String trimSmsNumber(String prefix, String number) {
		String s = number;
		if (prefix.length() > 0 && number.startsWith(prefix)) {
			s = number.substring(prefix.length());
		}
		return s;
	}

	/**
	 * String 转换成 Integer
	 * 
	 * @param str
	 * @return
	 */
	public static int String2Int(String str) {
		int num = 0;
		try {
			num = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return num;
	}

	/**
	 * String 转换成 Double
	 * 
	 * @param str
	 * @return
	 */
	public static double String2Double(String str) {
		double num = 0.0;
		try {
			num = Double.parseDouble(str);
		} catch (Exception e) {
		}
		return num;
	}

	/**
	 * String 转换成 Long
	 * 
	 * @param str
	 * @return
	 */
	public static long String2Long(String str) {
		long num = 0;
		try {
			num = Long.parseLong(str);
		} catch (Exception e) {
		}
		return num;
	}

	/**
	 * 格式化时间，把分钟转换成小时
	 * 
	 * @param minute
	 * @return
	 */
	public static String formatMinutes(long minute) {
		long h = minute / 60;
		long m = minute % 60;
		String fh = "";
		String fm = "";
		if (h != 0)
			fh = h + "小时";
		if (m != 0)
			fm = m + "分钟";
		if (isEmpty(fh) || isEmpty(fm))
			return fh + fm;
		else
			return fh + "，" + fm;
	}

	/**
	 * 格式化时间，把分钟转换成小时
	 * 
	 * @param minute
	 * @return
	 */
	public static String minute2Hour(long minute) {
		long hour = minute / 60;
		return String.valueOf(hour);
	}

	/**
	 * Kb转Mb
	 * 
	 * @param bblt
	 * @return
	 */
	public static String kbConvertM(String bblt) {

		if (bblt != null && !bblt.equals("")) {
			int m = 0;
			try {
				int blt = Integer.parseInt(bblt);
				m = blt / 1024;
				// int k = blt % 1024;
				// return m + "Mb" + k + "kb";
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}
			return m + "MB";
		} else {
			return bblt;
		}

	}

	/**
	 * 详单 计算流量大小
	 * 
	 * @param kbValue
	 * @return
	 */
	public static String getMBAndKB(String kbValue) {
		try {
			DecimalFormat df = new DecimalFormat("0.##");
			int kb = Integer.parseInt(kbValue);
			double k1 = kb / 1024.0;
			int k2 = kb % 1024;
			if (k1 >= 1) {
				return df.format(k1) + "MB";
			} else if (k2 != 0) {
				return k2 + "KB";
			} else {
				return "0KB";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 计算百分比
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static int getPercent(int x, int y) {
		double mx = x * 1.0;
		double my = y * 1.0;
		try {
			String temp = NumberFormat.getPercentInstance().format(mx / my);
			if (temp.contains("%")) {
				temp = temp.replace("%", "");
			}
			return Integer.parseInt(temp);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 换算单位
	 * 
	 * @param m
	 * @return
	 */
	public static String computeFt(int m) {
		if (m > 1000) {
			return (m / 1024) + "GB";
		} else {
			return m + "MB";
		}
	}

	/**
	 * 得到当月剩余天数
	 * 
	 * @param time
	 * @return
	 */
	public static int getDaysRemainNum(long c) {
		int max = 0;
		int curr = Calendar.getInstance().getTime().getDate();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		String time = format.format(new Date(c));
		try {
			Date date = format.parse(time);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (max - curr) + 1;
	}

	/**
	 * 计算某年每月有多少天
	 * 
	 * @param date
	 * @return
	 */
	public static int getMaxNumOfMonth(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
		try {
			Date d = format.parse(date);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(d);
			return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 遍历某年某月的每一天+某月
	 * 
	 * @param date
	 * @return
	 */
	public static List<String> getDaysDateOfMonth(String date) {
		int maxDays = getMaxNumOfMonth(date);
		List<String> days = new ArrayList<String>();
		if (maxDays != 0) {
			for (int i = 1; i <= maxDays; i++) {
				String dayTemp = null;
				if (String.valueOf(i).length() == 1) {
					dayTemp = "0" + String.valueOf(i);
				} else {
					dayTemp = i + "";
				}
				days.add(date + dayTemp);
			}
			Collections.reverse(days);
			days.add(0, date);
		}
		return days;
	}

	/**
	 * 返回如2012-12-31 22:00:00数据
	 * 
	 * @return
	 */
	public static String getLastDMY() {
		return now4Timestamp("yyyy-MM-", null)
				+ Calendar.getInstance()
						.getActualMaximum(Calendar.DAY_OF_MONTH) + " 22:00:00";
	}

	/**
	 * 得到日均值
	 * 
	 * @param x
	 * @param y
	 * @return
	 */

	public static String getAverageDailyVal(int x, int y) {
		String aa = "";
		try {
			double mx = x * 1.0;
			double my = y * 1.0;
			DecimalFormat df = new DecimalFormat("#0.00");
			aa = df.format(mx / my);
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}
		return aa;
	}

	/**
	 * 格式化数据，保留两位小数
	 * 
	 * @param obj
	 * @return
	 */
	public static String formatNumber(Object obj) {
		if (obj instanceof String) {
			if (!obj.equals("")) {
				try {
					obj = Integer.parseInt((String) obj);
				} catch (Exception e) {
					try {
						obj = Double.parseDouble((String) obj);
					} catch (Exception e2) {
						Log.e(TAG, e2.getLocalizedMessage(), e);
					}

				}
			}
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		String aa = "";
		try {
			aa = df.format(obj);
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}

		return aa;
	}

	/**
	 * 格式化时间 2012-5-2
	 * 
	 * @param str
	 * @return
	 */
	public static String FormatDate(String str, String parten) {
		SimpleDateFormat format = new SimpleDateFormat(parten);
		Date date = null;
		String aa = "";
		try {
			date = format.parse(str);
			aa = format.format(date);
		} catch (ParseException e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
			return null;
		}
		return aa;
	}

	// 获取配置文件的软件识别码
	public static String getIdentifier(Context context) {
		Properties prop = loadConfig(context, "default.properties");
		String identifier = (String) prop.get("identifier");
		return identifier;
	}

	// 获取当前手机的时间
	public static String now4Timestamp(String str, java.sql.Timestamp times) {
		if ("".equals(str))
			str = "yyyy-MM-dd HH:mm:ss";
		java.text.SimpleDateFormat date = new java.text.SimpleDateFormat(str);
		String currentDate = date.format(times != null ? times : new Date(
				System.currentTimeMillis()));
		return currentDate;
	}

	/**
	 * 根据一个指定的millsec获取一个指定格式的日期
	 * 
	 * @param str
	 * @param millSec
	 * @return
	 */
	public static String now4Timestamp(String str, long millSec) {
		if ("".equals(str)) {
			str = "yyyyMM";
		}
		SimpleDateFormat data = new SimpleDateFormat(str);
		String currentDate = data.format(millSec);
		return currentDate;
	}

	public static String now4Timestamp2(String str, java.sql.Timestamp times) {
		if ("".equals(str))
			str = "yyyy年MM月dd日 E ";
		java.text.SimpleDateFormat date = new java.text.SimpleDateFormat(str);
		String currentDate = date.format(times != null ? times : new Date(
				System.currentTimeMillis()));
		return currentDate;
	}

	// 读取配置文件

	public static Properties loadConfig(Context context, String fileName) {

		Properties properties = new Properties();

		try {

			InputStream s = context.getResources().getAssets().open(fileName);

			properties.load(s);

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}

		return properties;

	}

	// 获取mac地址
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		if (mac == null) {
			mac = "";
		}
		return mac;
	}

	// 获取服务启动的时间
	public static long getRunningServicesInfo(Context context) {
		// StringBuffer serviceInfo = new StringBuffer();
		final ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = activityManager
				.getRunningServices(100);
		String packageName = context.getPackageName();
		Iterator<RunningServiceInfo> l = services.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = l.next();

			if (si.process.equals(packageName)) {
				Log.d(TAG, si.service.toString());
				// if (si.service.toString().equals(
				// "ComponentInfo{" + packageName
				// + "/org.androidpn.client.NotificationService}")) {
				Log.d("TAG", (SystemClock.elapsedRealtime() - si.activeSince)
						/ 1000 + "");
				return (SystemClock.elapsedRealtime() - si.activeSince) / 1000;
				// }

			}
		}

		return 0;
	}

	// 判断程序是在前台还是 后台运行
	public static boolean isTopActivity(Activity activity) {

		String packageName = "com.born.mobile.wlanhelper";

		ActivityManager activityManager = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);

		if (tasksInfo.size() > 0) {

			Log.d(TAG, "---------------包名-----------"
					+ tasksInfo.get(0).topActivity.getPackageName());

			// 应用程序位于堆栈的顶层

			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {

				return true;

			}

		}

		return false;

	}

	/**
	 * 由 秒 转换 成 0分0秒
	 * 
	 * @param seconds
	 * @return
	 */
	public static String calculateTime(int seconds) {
		int min = seconds / 60;// 分钟
		int sec = seconds % 60;// 秒数
		if (min == 0) {
			return sec + "秒";
		} else {
			return min + "分" + sec + "秒";
		}
	}

	/**
	 * 获取星期几
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int getWeekDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		return calendar.get(Calendar.DAY_OF_WEEK);// 星期表示1-7，是从星期日开始，
	}

	public static final int daysBetween(Date early, Date late) {

		java.util.Calendar calst = java.util.Calendar.getInstance();
		java.util.Calendar caled = java.util.Calendar.getInstance();
		calst.setTime(early);
		caled.setTime(late);
		// 设置时间为0时
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
				.getTime().getTime() / 1000)) / 3600 / 24;

		return days;
	}

	/**
	 * 得到day过后的日期
	 * 
	 * @param dueCalendars
	 * @param day
	 * @return
	 */
	public static CalendarDate getDateAfter(Calendars dueCalendars, int day) {
		CalendarDate cDate = new CalendarDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 格式化日期
		Calendar now = Calendar.getInstance();
		now.set(dueCalendars.getYear(), dueCalendars.getMonth() - 1,
				dueCalendars.getDay());
		now.add(Calendar.DAY_OF_YEAR, day);
		String str[] = sdf.format(now.getTime()).split("-");
		cDate.setYear(Integer.valueOf(str[0]));
		cDate.setMonth(Integer.valueOf(str[1]));
		cDate.setDay(Integer.valueOf(str[2]));
		return cDate;
	}

	/**
	 * 比较两日期大小
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/***
	 * 获取当前时间的时间戳
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(date);
	}

	/**
	 * 将指定的日期转换成时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateFromStr(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(time);
			return String.valueOf(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 把时间戳转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getStringFromDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(time);
	}

	/**
	 * 得到多少天过后的日期
	 * 
	 * @param str
	 * @param days
	 * @return
	 */
	public static String getAddDate(String[] str, int days) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.valueOf(str[0]), Integer.valueOf(str[1]) - 1,
				Integer.valueOf(str[2]));
		cal.add(Calendar.DAY_OF_YEAR, days);
		long date = cal.getTimeInMillis();
		return format.format(new Date(date));
	}

	/**
	 * 计算两日期相差的天数
	 * 
	 * @param dueTime
	 * @param currentTime
	 * @return
	 */
	public static int getDateSpace(String dueTime, String currentTime) {
		Date earlydate = new Date();
		Date latedate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			earlydate = df.parse(dueTime);
			latedate = df.parse(currentTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return daysBetween(earlydate, latedate);
	}

	public static String getDataValues(int values) {
		String result = "";
		if (values <= 9) {
			result = "0" + values;
		} else {
			result = String.valueOf(values);
		}
		return result;
	}

	/**
	 * 根据年月获取当月的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int chooseTime(int year, int month) {
		int date = 1;
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month))) {
			date = 31;
		} else if (list_little.contains(String.valueOf(month))) {
			date = 30;
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				date = 29;
			} else {
				date = 28;
			}
		}
		return date;
	}
}
