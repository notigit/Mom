package com.xiaoaitouch.mom.net.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.bean.MainParams;
import com.xiaoaitouch.mom.bean.SendCardParams;
import com.xiaoaitouch.mom.bean.SocialLogin;
import com.xiaoaitouch.mom.bean.UserMessage;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.net.SevenDoVolley;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.request.StringRequest;
import com.xiaoaitouch.mom.util.Encode;
import com.xiaoaitouch.mom.util.Utils;

public class HttpApi {
	/**
	 * 天气和pm2.5接口
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getPmOrWeather(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		String[] str = (String[]) object;
		mPostParams.put("areaName", str[0]);
		mPostParams.put("date", str[1]);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 删除卡片
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void deleteCard(Activity activity, String method,
			StringRequest request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 发送卡片(没有图片的)
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void sendCard(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		SendCardParams mainParams = (SendCardParams) object;
		mPostParams.put("date", mainParams.getDate());
		mPostParams.put("message", mainParams.getMessage());
		mPostParams.put("lat", String.valueOf(mainParams.getLat()));
		mPostParams.put("lng", String.valueOf(mainParams.getLng()));
		mPostParams.put("feeling", String.valueOf(mainParams.getFeeling()));
		mPostParams.put("location", mainParams.getLocation());
		mPostParams.put("type", String.valueOf(mainParams.getType()));
		mPostParams.put("createTime", String.valueOf(mainParams.getCreateTime()));
		getRequest(method, mPostParams, request);
	}

	/**
	 * 获取主页数据
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getMainData(Activity activity, String method,
			GsonTokenRequest<T> request, String object) {
		Map<String, String> mPostParams = getParams(activity);
		mPostParams.put("date", object);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 当前日历数据
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param string
	 */
	public static <T> void getCalendar(Activity activity, String method,
			GsonTokenRequest<T> request, String string) {
		Map<String, String> mPostParams = getParams(activity);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 修改昵称
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param string
	 */
	public static <T> void getUpdateNickName(Activity activity, String method,
			GsonTokenRequest<T> request, String string) {
		Map<String, String> mPostParams = getParams(activity);
		mPostParams.put("neckname", string);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 忘记密码
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getUserForgetPwd(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		String[] str = (String[]) object;
		mPostParams.put("phone", str[0]);
		mPostParams.put("pwd", str[1]);
		mPostParams.put("code", str[2]);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 登录
	 * 
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getUserLogin(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		String[] str = (String[]) object;
		mPostParams.put("userName", str[0]);
		mPostParams.put("pwd", str[1]);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 注册
	 * 
	 * @param activity
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getRegister(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		String[] str = (String[]) object;
		mPostParams.put("phone", str[0]);
		mPostParams.put("pwd", str[1]);
		mPostParams.put("code", str[2]);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 发送验证码
	 * 
	 * @param method
	 * @param request
	 * @param phone
	 */
	public static <T> void sendCode(Activity activity, String method,
			StringRequest request, String phone) {
		Map<String, String> mPostParams = getParams(activity);
		mPostParams.put("phone", phone);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 第三方登录
	 * 
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getUserSocialLogin(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		SocialLogin sLogin = (SocialLogin) object;
		mPostParams.put("socailUnid", sLogin.getSocailUnid());
		mPostParams.put("userLoginName", sLogin.getUserLoginName());
		mPostParams.put("userName", sLogin.getUserName());
		mPostParams.put("head_pic", sLogin.getHead_pic());
		mPostParams.put("source", String.valueOf(sLogin.getSource()));
		getRequest(method, mPostParams, request);
	}

	/**
	 * 修改预产期信息
	 * 
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getUpdateDueInfo(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		String[] str = (String[]) object;
		mPostParams.put("dueTime", str[0]);
		mPostParams.put("lastMensesTime", str[1]);
		mPostParams.put("mensesCircle", str[2]);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 修改个人信息
	 * 
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getUpdateUserInfo(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		String[] str = (String[]) object;
		mPostParams.put("age", str[0]);
		mPostParams.put("height", str[1]);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 获取主页信息
	 * 
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getHomeData(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		mPostParams.put("lastMensesTime", (String) object);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 基础信息绑定
	 * 
	 * @param method
	 * @param request
	 * @param object
	 */
	public static <T> void getSubmitInfoParams(Activity activity,
			String method, GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		if (object != null) {
			UserMessage userInfo = (UserMessage) object;
			mPostParams.put("age", userInfo.getAge());
			mPostParams.put("height", userInfo.getHeight());
			mPostParams.put("neckname", userInfo.getNickName());
			mPostParams.put("dueTime", userInfo.getDueTime());
			mPostParams.put("weight", userInfo.getWeight());
			mPostParams.put("mensesCircle", String.valueOf(userInfo.getDays()));
			mPostParams.put("lastMensesTime", userInfo.getmLastPeriod());
		}
		getRequest(method, mPostParams, request);
	}

	/**
	 * 获取个人信息
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public static <T> void getUserInfo(Activity activity, String method,
			GsonTokenRequest<T> request, Object object) {
		Map<String, String> mPostParams = getParams(activity);
		getRequest(method, mPostParams, request);
	}

	/**
	 * 带参数请求公共方法
	 * 
	 * @param method
	 * @param map
	 * @param request
	 */
	private static <T> void getRequest(String method, Map<String, String> map,
			GsonTokenRequest<T> request) {
		Map<String, String> mPostParams = new HashMap<String, String>();
		RequestQueue requestQueue = SevenDoVolley.getRequestQueue();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if ((String) entry.getValue() != null) {
				request.addPostParam((String) entry.getKey(),
						(String) entry.getValue());
				mPostParams.put((String) entry.getKey(),
						(String) entry.getValue());
			}

		}
		request.addPostParam("key", getKeyValues(method, mPostParams));
		requestQueue.add(request);
	}

	/**
	 * 带参数请求公共方法
	 * 
	 * @param method
	 * @param map
	 * @param request
	 */
	private static void getRequest(String method, Map<String, String> map,
			StringRequest request) {
		Map<String, String> mPostParams = new HashMap<String, String>();
		RequestQueue requestQueue = SevenDoVolley.getRequestQueue();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if ((String) entry.getValue() != null) {
				request.addPostParam((String) entry.getKey(),
						(String) entry.getValue());
				mPostParams.put((String) entry.getKey(),
						(String) entry.getValue());
			}

		}
		request.addPostParam("key", getKeyValues(method, mPostParams));
		requestQueue.add(request);
	}

	private static Map<String, String> getParams(Activity activity) {
		Map<String, String> mPostParams = new HashMap<String, String>();
		MomApplication application = (MomApplication) activity.getApplication();
		UserInfo userInfo = application.getUserInfo();
		if (userInfo != null && !TextUtils.isEmpty(userInfo.getAccount())
				&& !TextUtils.isEmpty(userInfo.getPwd())) {
			mPostParams.put("userName", userInfo.getAccount());
			mPostParams.put("pwd", userInfo.getPwd());
		} else {
			mPostParams.put("uniqueness", Utils.createUniqueness());
		}
		mPostParams
				.put("timestemp", String.valueOf(System.currentTimeMillis()));
		return mPostParams;
	}

	/**
	 * 参数名排序得到key值
	 * 
	 * @param method
	 * @param map
	 * @return
	 */
	private static String getKeyValues(String method, Map<String, String> map) {
		String params = Configs.SERVER_URL + method + "?";
		String valueParams = setArraySort(map);
		params += valueParams.substring(1, valueParams.length());
		params += "&appId=com.xiaoaitouch.mom&com=xiaoaitouch";
		return Encode.getMd5Value(Encode.getMd5Value(params)).toString()
				.toUpperCase();// 字母大写
	}

	/**
	 * 请求参数
	 * 
	 * @param method
	 * @param map
	 * @return
	 */
	public static String getValues(String method, Map<String, String> map) {
		String params = Configs.SERVER_URL + method + "?";
		String valueParams = setArraySort(map);
		params += valueParams.substring(1, valueParams.length());
		params += "&appId=com.xiaoaitouch.mom&com=xiaoaitouch";
		return params;
	}

	/**
	 * 对参数名字进行排序
	 * 
	 * @param map
	 * @return
	 */
	private static String setArraySort(Map<String, String> map) {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		String keys[] = new String[map.size()];
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			keys[i] = "&" + key + "=" + map.get(key);
			i++;
		}
		Arrays.sort(keys);
		for (String s : keys) {
			sb.append(s);
		}
		return sb.toString();
	}

}
