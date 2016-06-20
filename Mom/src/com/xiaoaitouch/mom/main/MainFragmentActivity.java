package com.xiaoaitouch.mom.main;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import com.android.volley.Response.Listener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.bluetooth.le.BluetoothLeService.OnWristbandsDataListener;
import com.example.bluetooth.le.BluetoothLeUtil;
import com.example.bluetooth.le.WristbandsInfo;
import com.google.gson.reflect.TypeToken;
import com.umeng.fb.push.IFeedbackPush;
import com.xiaoaitouch.event.EventBus;
import com.xiaoaitouch.event.bean.MainEvent;
import com.xiaoaitouch.event.bean.NewFragmentDataEvent;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.PmWeatherBean;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.configs.Constant;
import com.xiaoaitouch.mom.dao.SportInfoModel;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.droid.BaseFragmentActivity;
import com.xiaoaitouch.mom.fragment.MainFragment;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.ShareInfo;
import com.xiaoaitouch.mom.util.SportInfoUtils;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.util.UserDataUtils;
import com.xiaoaitouch.mom.util.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 主页
 * 
 * @author huxin
 * 
 */
public class MainFragmentActivity extends BaseFragmentActivity {
    @Bind(R.id.main_top_left_image)
    ImageView mMainLeftImage;
    @Bind(R.id.main_title_tv)
    TextView mMainTitle;
    @Bind(R.id.main_top_right_image)
    ImageView mMainRightImage;
    @Bind(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_left_content_lay)
    View mLeftView;
    @Bind(R.id.main_viewpager)
    ViewPager mViewPager;
    // 添加模糊效果
    @Bind(R.id.main_root_rl)
    View mView;
    @Bind(R.id.main_blur_bg_rl)
    View mBlurView;
    private UserInfo mUserInfo;
    private int mYear = 2015;
    private int mMonth = 1;
    private int mDate = 1;
    private long firstTime = 0; // 记录按返回键时间
    /**
     * gps是否启动
     */
    private boolean startGPS;
    private LocationClient baiduLocationClient;
    private int mTotalWeek = 40;
    private int mCurrentWeek = 1;
    private String mDueTime;// 怀孕的日期
    private String mCurrentDate;
    private String[] mStr;
    private boolean myLocation = false;
    // ///////////
    private boolean mConnectResult = false;
    
    public String mDeviceAddress = null;
    public BluetoothLeUtil mBluetoothLeUtil = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDataUtils mDataUtils = new UserDataUtils(mActivity);
        mUserInfo = mDataUtils.getUserData();
        EventBus.getDefault().register(this);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        initView();
        initBaiduLocateLocation();
        // setConnectBluetooth();
//       submitSportInfo();
    }
    private void initView() {
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH) + 1;
        mDate = now.get(Calendar.DATE);
        mCurrentDate = mYear + "-" + StringUtils.getDataValues(mMonth) + "-"
                + StringUtils.getDataValues(mDate);
        mDrawerLayout.setDrawerListener(mDrawerListener);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setViewData();
        // 绑定dialog
        // DialogUtils.bindingAlertDialog(mActivity, new OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // switch (which) {
        // case DialogUtils.BUTTON1:
        // dialog.dismiss();
        // startIntent(MipcaActivityCapture.class);
        // break;
        // case DialogUtils.BUTTON2:
        // dialog.dismiss();
        // break;
        // }
        // }
        // });
    }
    public void onEvent(MainEvent event) {
        mViewPager.removeAllViews();
        setViewData();
    }
    private void setViewData() {
        setTitleView();
        mViewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(mCurrentWeek - 1, false);
        mViewPager.setOffscreenPageLimit(mTotalWeek);
        mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }
    // 拍照当周Fragment更新
    public void onEvent(NewFragmentDataEvent event) {
        List<Fragment> mainFragments = getSupportFragmentManager()
                .getFragments();
        MainFragment mainFragment = (MainFragment) mainFragments.get(1);
        mainFragment.setRefreshLoad();
    }
    /**
     * 获取当前定位信息
     */
    private void initBaiduLocateLocation() {
        this.baiduLocationClient = new LocationClient(this);
        this.baiduLocationClient
                .registerLocationListener(new BDLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation location) {
                        if (location != null) {
                            ((MomApplication) getApplication())
                                    .setLocationBean(location);
                            if (!myLocation) {
                                String mCity = location.getCity();
                                if (mCity != null && mCity.equals("重庆市")) {
                                    mCity = "重庆";
                                }
                                myLocation = true;
                                mStr = new String[] { mCity, mCurrentDate };
                                getPmOrWeather(mStr);
                            }
                        }
                    }
                });
        LocationClientOption option = new LocationClientOption();
        // 打开gps
        option.setOpenGps(true);
        // 设置坐标类型
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setScanSpan(2000);
        this.baiduLocationClient.setLocOption(option);
    }
    /**
     * 获取今天的pm2.5和天气情况
     * 
     * @param str
     */
    private void getPmOrWeather(String[] str) {
        GsonTokenRequest<PmWeatherBean> request = new GsonTokenRequest<PmWeatherBean>(
                com.android.volley.Request.Method.POST, Configs.SERVER_URL
                        + "/user/pm25weather",
                new Listener<JsonResponse<PmWeatherBean>>() {
                    @Override
                    public void onResponse(JsonResponse<PmWeatherBean> response) {
                        switch (response.state) {
                        case Configs.UN_USE:
                            showToast("版本过低请升级新版本");
                            break;
                        case Configs.FAIL:
                            showToast(response.msg);
                            break;
                        case Configs.SUCCESS:
                            ((MomApplication) getApplication())
                                    .setPmWeatherBean(response.data);
                            break;
                        }
                    }
                }, null) {
            @Override
            public Type getType() {
                Type type = new TypeToken<JsonResponse<PmWeatherBean>>() {
                }.getType();
                return type;
            }
        };
        HttpApi.getPmOrWeather(mActivity, "/user/pm25weather", request, str);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!startGPS) {
            this.baiduLocationClient.start();
            this.startGPS = true;
        }
        
        initBleService();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        this.startGPS = false;
        this.baiduLocationClient.stop();
        
        if (mBluetoothLeUtil != null) {
            mBluetoothLeUtil.onStop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopGetMsgs();
        if (this.baiduLocationClient.isStarted()) {
            this.baiduLocationClient.stop();
        }
        this.baiduLocationClient = null;
    }
    /** 初始蓝牙
     * 
     */
    private void initBleService(){
        mDeviceAddress = ShareInfo.getTagString(this, ShareInfo.TAG_BLE_MAC);
        if (mDeviceAddress != null && mDeviceAddress.length() > 1) {
            mBluetoothLeUtil = new BluetoothLeUtil(this, mDeviceAddress);
            mBluetoothLeUtil.onStart();
            mBluetoothLeUtil.setOnWristbandsDataListener(new OnWristbandsDataListener() {
                @Override
                public void onStepData(WristbandsInfo info) {
                    // TODO Auto-generated method stub
                    showToast("走都步数" + info.getStep());
                }
                
                @Override
                public void onDisconnected(int arg0) {
                    // TODO Auto-generated method stub
                    
                }
                
                @Override
                public void onConnected(int arg0) {
                    // TODO Auto-generated method stub
                    
                }
            });
            System.out.println("ble ----------------- start");
        }
    }
    
    private void setTitleView() {
        mUserInfo = ((MomApplication) getApplication()).getUserInfo();
        if (mUserInfo != null) {
            if (!TextUtils.isEmpty(mUserInfo.getLastMensesTime())
                    && !mUserInfo.getLastMensesTime().equals("0")) {// 末次月经
                setGestationData(mUserInfo.getLastMensesTime());
            } else if (!TextUtils.isEmpty(mUserInfo.getDueTime())
                    && !mUserInfo.getDueTime().equals("0")) {// 预产期
                String[] mDueTime = StringUtils.getStringFromDate(
                        Long.valueOf(mUserInfo.getDueTime())).split("-");
                setGestationData(StringUtils.getDateFromStr(StringUtils
                        .getAddDate(mDueTime, -280)));
            }
        }
    }
    private void setGestationData(String time) {
        mDueTime = StringUtils.getStringFromDate(Long.valueOf(time));
        int number = StringUtils.getDateSpace(mDueTime, mCurrentDate) + 1;
        if (number >= 7) {
            mCurrentWeek = (number / 7) + 1;
            int days = number % 7;
            if (mCurrentWeek <= 40) {
                if (days == 0) {
                    mMainTitle.setText("第" + mCurrentWeek + "周" + " 第" + 1
                            + "天");
                } else
                    mMainTitle.setText("第" + mCurrentWeek + "周" + " 第" + days
                            + "天");
            } else {
                mMainTitle.setVisibility(View.INVISIBLE);
            }
        } else {
            mMainTitle.setText("第" + mCurrentWeek + "周" + " 第" + number + "天");
        }
    }
    @OnClick(R.id.main_top_left_image)
    public void setupMainMenuBtn() {
        if (!mDrawerLayout.isActivated()) {
            mDrawerLayout.openDrawer(mLeftView);
        }
    }
    @OnClick(R.id.main_title_tv)
    public void openCalendar() {
        Intent mIntent = new Intent(mActivity, CalendarActivity.class);
        startActivityForResult(mIntent, 1002);
        overridePendingTransition(R.anim.calendar_in, R.anim.calendar_in_out);
    }
    @OnClick(R.id.main_top_right_image)
    public void openNextActivity() {
        startActivity(new Intent(this, ShoesManageActivity.class));
    }
    @OnClick(R.id.main_user_camm_image)
    public void openMapActivity() {
        Intent intent = new Intent(mActivity, NotesActivity.class);
        intent.putExtra("date", mCurrentDate);
        intent.setAction(Constant.BROADCAST_ACTION);
        intent.putExtra("isBlur", true);
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
        startActivity(intent);
    }
    /**
     * 是否模糊界面
     * 
     * @param isBlur
     *            isBlur=true;代表模糊
     */
    public void showBlurView(boolean isBlur) {
        if (isBlur) {
            mView.setDrawingCacheEnabled(true);
            Utils.setBlurBg(mView.getDrawingCache(true), mBlurView, 8, 6,
                    getResources());
            mView.setDrawingCacheEnabled(false);
            mBlurView.setVisibility(View.VISIBLE);
        } else if (!isBlur) {
            mBlurView.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 广播监听是否需要模糊界面
     */
    private final BroadcastReceiver mBroadcastReceiverListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.getBroadcastAction().equals(action)) {
                showBlurView(intent.getBooleanExtra("isBlur", false));
            }
        }
    };
    private void setReceiver() {
        final IntentFilter f = new IntentFilter();
        f.addAction(Constant.getBroadcastAction());
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mBroadcastReceiverListener, new IntentFilter(f));
    }
    private void stopGetMsgs() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
                mBroadcastReceiverListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        setReceiver();
    }
    /**
     * 提交本地运动信息
     */
    private void submitSportInfo() {
        if (mUserInfo != null && mUserInfo.getUserId() != null) {
            List<SportInfoModel> mSportInfoModels = SportInfoUtils.instance()
                    .queryAllSportInfo(mUserInfo.getUserId());
            if (mSportInfoModels != null && mSportInfoModels.size() > 0) {
            }
        }
    }
    /**
     * 定义适配器
     * 
     */
    public class myPagerAdapter extends FragmentPagerAdapter {
        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int position) {
            int mPage = position + 1;
            return MainFragment.newInstance(mPage, mCurrentWeek,
                    StringUtils.getAddDate(mDueTime.split("-"), 7 * position),
                    mCurrentDate, mPage == mCurrentWeek, mUserInfo);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MainFragment fragment = (MainFragment) super.instantiateItem(
                    container, position);
            if (fragment != null) {
                int mPage = position + 1;
                fragment.update(mPage, mCurrentWeek, StringUtils.getAddDate(
                        mDueTime.split("-"), 7 * position), mCurrentDate,
                        mPage == mCurrentWeek, mUserInfo);
            }
            return fragment;
        }
        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return mTotalWeek;
        }
    }
    DrawerListener mDrawerListener = new DrawerListener() {
        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerLayout
                    .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);// 打开手势滑动
        }
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            // if (slideOffset > 0.6) {
            // mView.setDrawingCacheEnabled(true);
            // Utils.setBlurBg(mView.getDrawingCache(true), mBlurView, 8, 8,
            // getResources());
            // mView.setDrawingCacheEnabled(false);
            // mBlurView.setVisibility(View.VISIBLE);
            // } else {
            // mBlurView.setVisibility(View.INVISIBLE);
            // }
        }
        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
    // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageSelected(int posion) {
            int page = posion + 1;
            if (page == mCurrentWeek) {
                mMainTitle.setText("第" + page + "周" + " 第" + 1 + "天");
            } else
                mMainTitle.setText("第" + page + "周" + " 第" + 7 + "天");
        }
    }
    // 尝试关闭侧滑菜单
    private boolean showSlidingContent() {
        if (mDrawerLayout.isActivated()) {
            mDrawerLayout.closeDrawer(mLeftView);
            return true;
        }
        return false;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                showSlidingContent();
                long secondTime = System.currentTimeMillis();
                if ((secondTime - firstTime) > 1000) {
                    showToast(R.string.app_exit, Toast.LENGTH_SHORT);
                    firstTime = secondTime;
                    return false;
                } else {
                    finish();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1012) {
            String date = data.getStringExtra("date");
            int number = StringUtils.getDateSpace(mDueTime, date);
            if (number >= 7) {
                int week = number / 7;
                mViewPager.setCurrentItem(week);
            } else {
                mViewPager.setCurrentItem(0);
            }
        }
    }
}