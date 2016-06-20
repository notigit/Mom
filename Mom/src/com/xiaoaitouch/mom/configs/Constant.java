package com.xiaoaitouch.mom.configs;

public class Constant {

	public static final String BROADCAST_ACTION = "com.xiaoaitouch.mom.view.broadcastreceiver";

	public static final String WX_APP_ID = "wx277ad0d510638c8a";

	public static final String WX_APP_Secret = "8242c3cb13773b67eeef5c8ded9a4cba";

	public static final String QQ_APP_ID = "1104751617";

	public static final String QQ_APP_Secret = "R69i8glUrVpavixk";

	/**
	 * 手机号登陆
	 */
	public static final int SOCIAL_SOURCE_MOBILE = -1;
	/**
	 * 微信登录
	 */
	public static final int SOCIAL_SOURCE_WEIXIN = 0;
	/**
	 * 新浪登陆
	 */
	public static final int SOCIAL_SOURCE_SINA = 1;
	/**
	 * QQ登陆
	 */
	public static final int SOCIAL_SOURCE_QQ = 2;

	public static String getBroadcastAction() {
		return BROADCAST_ACTION;
	}
}
