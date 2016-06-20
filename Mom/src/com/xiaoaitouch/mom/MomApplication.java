package com.xiaoaitouch.mom;

import java.io.File;
import java.util.List;

import org.lasque.tusdk.core.TuSdk;

import android.app.Notification;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xiaoaitouch.mom.bean.CalendarBean.Calendars;
import com.xiaoaitouch.mom.bean.PmWeatherBean;
import com.xiaoaitouch.mom.dao.DaoSession;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.droid.BaseApplication;

public class MomApplication extends BaseApplication {
	private static final int DISK_CACHE_SIZE_BYTES = 50 * 1024 * 1024;
	private static final int MEMORY_CACHE_SIZE_BYTES = 2 * 1024 * 1024;
	private DaoSession mMainSession;
	public UserInfo mUserInfo;
	private PushAgent mPushAgent;
	private static Typeface customFont;
	public List<Calendars> mCalendarBeans = null;
	private BDLocation mBDLocation = null;
	public PmWeatherBean mPmWeatherBean;

	@Override
	public void onCreate() {
		super.onCreate();
		customFont = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-Light.ttf");
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(false);
		mMainSession = DatabaseMaster.instance().init(this).getMainDbSession();
		initUniversalImageLoader();
		initTuSDK();
		messagePush();
	}

	private void initUniversalImageLoader() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(this, Environment
				.getExternalStorageDirectory().getPath() + "/Mom/images");
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.setting_userhead)
				.showImageForEmptyUri(R.drawable.setting_userhead)
				.showImageOnFail(R.drawable.setting_userhead)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(android.graphics.Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions)
				.diskCacheSize(DISK_CACHE_SIZE_BYTES)
				.memoryCacheSize(MEMORY_CACHE_SIZE_BYTES)
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.memoryCache(new WeakMemoryCache()).threadPoolSize(3).build();

		ImageLoader tmpIL = ImageLoader.getInstance();
		tmpIL.init(config);
	}

	public void initTuSDK() {
		TuSdk.enableDebugLog(true);
		// 开发ID (请前往 http://tusdk.com 获取您的APP 开发秘钥)
		TuSdk.init(this.getApplicationContext(), "372b826094f6c11f-00-l1vyn1");
		super.onCreate();
	}

	public static Typeface getTypeface() {
		return customFont;
	}

	@Override
	protected boolean debug() {
		return BuildConfig.DEBUG;
	}

	public DaoSession getDaoSession() {
		return mMainSession;
	}

	public void setPmWeatherBean(PmWeatherBean pmWeatherBean) {
		this.mPmWeatherBean = pmWeatherBean;
	}

	public PmWeatherBean getPmWeatherBean() {
		return mPmWeatherBean;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.mUserInfo = userInfo;
	}

	public UserInfo getUserInfo() {
		return mUserInfo;
	}

	public void setCalendarBeans(List<Calendars> calendars) {
		this.mCalendarBeans = calendars;
	}

	public List<Calendars> getCalendarBeans() {
		return mCalendarBeans;
	}

	public void setLocationBean(BDLocation locationBean) {
		this.mBDLocation = locationBean;
	}

	public BDLocation getBDLocation() {
		return mBDLocation;
	}

	// @Override
	// public void onLowMemory() {
	// super.onLowMemory();
	// ImageLoader.getInstance().clearMemoryCache();
	// }

	private void messagePush() {
		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			/**
			 * 参考集成文档的1.6.3 http://dev.umeng.com/push/android/integration#1_6_3
			 * */
			@Override
			public void dealWithCustomMessage(final Context context,
					final UMessage msg) {
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// 对自定义消息的处理方式，点击或者忽略
						boolean isClickOrDismissed = true;
						if (isClickOrDismissed) {
							// 自定义消息的点击统计
							UTrack.getInstance(getApplicationContext())
									.trackMsgClick(msg);
						} else {
							// 自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext())
									.trackMsgDismissed(msg);
						}
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG)
								.show();
					}
				});
			}

			/**
			 * 参考集成文档的1.6.4 http://dev.umeng.com/push/android/integration#1_6_4
			 * */
			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
				case 1:
					NotificationCompat.Builder builder = new NotificationCompat.Builder(
							context);
					RemoteViews myNotificationView = new RemoteViews(
							context.getPackageName(),
							R.layout.notification_view);
					myNotificationView.setTextViewText(R.id.notification_title,
							msg.title);
					myNotificationView.setTextViewText(R.id.notification_text,
							msg.text);
					myNotificationView.setImageViewBitmap(
							R.id.notification_large_icon,
							getLargeIcon(context, msg));
					myNotificationView.setImageViewResource(
							R.id.notification_small_icon,
							getSmallIconId(context, msg));
					builder.setContent(myNotificationView);
					builder.setAutoCancel(true);
					Notification mNotification = builder.build();
					// 由于Android
					// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
					mNotification.contentView = myNotificationView;
					return mNotification;
				default:
					// 默认为0，若填写的builder_id并不存在，也使用默认。
					return super.getNotification(context, msg);
				}
			}
		};
		mPushAgent.setMessageHandler(messageHandler);

		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 参考集成文档的1.6.2
		 * http://dev.umeng.com/push/android/integration#1_6_2
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}
}
