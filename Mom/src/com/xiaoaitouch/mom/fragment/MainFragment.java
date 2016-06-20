package com.xiaoaitouch.mom.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiaoaitouch.mom.DatabaseMaster;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.MainAdapter;
import com.xiaoaitouch.mom.bean.MainCardDataBean;
import com.xiaoaitouch.mom.bean.MainData;
import com.xiaoaitouch.mom.bean.MainDataBean;
import com.xiaoaitouch.mom.bean.MainDataModle;
import com.xiaoaitouch.mom.bean.PmWeatherBean;
import com.xiaoaitouch.mom.bean.ShareCardComparator;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.MSymptomModel;
import com.xiaoaitouch.mom.dao.MscModel;
import com.xiaoaitouch.mom.dao.ShareCardModel;
import com.xiaoaitouch.mom.dao.SportCardModel;
import com.xiaoaitouch.mom.dao.SportInfoModel;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.dao.babychange.BabyChange;
import com.xiaoaitouch.mom.dao.babychange.BabyChangeDao;
import com.xiaoaitouch.mom.data.model.MainUserModel;
import com.xiaoaitouch.mom.droid.BaseFragment;
import com.xiaoaitouch.mom.main.BigActivity;
import com.xiaoaitouch.mom.mine.MineActivity;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.pedometer.StepService;
import com.xiaoaitouch.mom.train.BeforTraining;
import com.xiaoaitouch.mom.train.ExternalFactor;
import com.xiaoaitouch.mom.train.Training;
import com.xiaoaitouch.mom.train.Training.OnTrainingLisener;
import com.xiaoaitouch.mom.util.BabyShareDialog;
import com.xiaoaitouch.mom.util.DialogUtils;
import com.xiaoaitouch.mom.util.DialogUtils.OnWeightListener;
import com.xiaoaitouch.mom.util.MainDatabase;
import com.xiaoaitouch.mom.util.SportInfoUtils;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.util.Utils;
import com.xiaoaitouch.mom.view.PullToZoomListView;
import com.xiaoaitouch.mom.view.RoundedImageView;

public class MainFragment extends BaseFragment implements OnClickListener {
    // FIXME 计步放到activity中，fragment预加载5个，其余释放
    // FIXME 计步一直在后台运行，不随程序结束而终止

    public static final String QUERY_WEEK_DATA = "week";
    public static final String QUERY_DATE_DATA = "date";
    public static final String QUERY_USER_DATA = "userInfo";
    public static final String QUERY_CURRENT_WEEK = "currentWeek";
    public static final String QUERY_IS_CURRENT_WEEK = "isCurrentWeek";
    public static final String QUERY_IS_CURRENT_DAY = "isCurrentDay";
    @Bind(R.id.main_listview)
    PullToZoomListView mListView;
    private MainAdapter mainAdapter;
    private View mHeadView;
    private View mTopHorizontalView;
    private View mTopVerticalView;
    private TextView mUserNickNameTv;
    private RoundedImageView mRoundedImageView;
    private TextView mBabyLengthTv;
    private TextView mBabyWeightTv;
    private TextView mBabyDueTv;
    private TextView mBabyOtherDueTv;
    private TextView mBabyDetailsTv;
    private ImageView mBabyImage;
    private ImageView mBabyImage1;
    private TextView mUserWeightTv;
    private TextView mUserSymptomsTv;
    private TextView mUserExaminationTv;
    private boolean isLoadData; // 是否可以开始加载数据
    private List<MainData> mainDatasList = new ArrayList<MainData>();
    private MainDataBean mMainDataBean;
    // 本地数据库婴儿信息查询
    private BabyChangeDao mBabyChangeDao;
    private BabyChange mBabyChange;
    private int mBeforWeek = 1;
    private int mWeek = 1;
    private String mDate = "";
    private int resID = 0;
    private boolean mIsCurrentWeek = false;// 是否是当前周
    private UserInfo mUserInfo;
    private MainUserModel mainUserModel;
    private MainDataModle mainDataModle;
    private String mCurrentDate;// 当前时间
    private int mCurrentWeekDay = 1;// 当周天数
    private MainDatabase mainDatabase;
    private DisplayImageOptions defaultOptions;
    private ApplicationInfo appInfo;
    private String dueDayStr;
    private int mDueDay = 0;
    private int mListIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isLoadData = true;
        appInfo = getActivity().getApplicationInfo();
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        // 运动
        if (mIsCurrentWeek) {
            training = new Training(onTrainingLisener);
            mBeforTraining = new BeforTraining();
            mBeforTraining.couldTraining();
        }
    }

    /**
     * 创建Fragment实例并传递参数
     * 
     * @param str
     * @return
     */
    public static MainFragment newInstance(int week, int currentWeek, String date, String currentDate,
            Boolean isCurrentWeek, UserInfo userInfo) {
        final MainFragment fragment = new MainFragment();
        final Bundle args = new Bundle();
        args.putInt(QUERY_WEEK_DATA, week);
        args.putInt(QUERY_CURRENT_WEEK, currentWeek);
        args.putString(QUERY_DATE_DATA, date);
        args.putString(QUERY_IS_CURRENT_DAY, currentDate);
        args.putBoolean(QUERY_IS_CURRENT_WEEK, isCurrentWeek);
        args.putSerializable(QUERY_USER_DATA, userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    public void update(int week, int currentWeek, String date, String currentDate, Boolean isCurrentWeek,
            UserInfo userInfo) {
        this.mUserInfo = userInfo;
        this.mBeforWeek = currentWeek;
        this.mWeek = week;
        this.mDate = date;
        this.mIsCurrentWeek = isCurrentWeek;
        this.mCurrentDate = currentDate;
        if (!mIsCurrentWeek) {
            mCurrentWeekDay = 7;
        } else {
            mCurrentWeekDay = StringUtils.getDateSpace(mDate, mCurrentDate) + 1;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            this.mBeforWeek = args.getInt(QUERY_CURRENT_WEEK, 1);
            this.mWeek = args.getInt(QUERY_WEEK_DATA, 1);
            this.mDate = args.getString(QUERY_DATE_DATA);
            this.mIsCurrentWeek = args.getBoolean(QUERY_IS_CURRENT_WEEK, false);
            this.mCurrentDate = args.getString(QUERY_IS_CURRENT_DAY);
            this.mUserInfo = (UserInfo) args.getSerializable(QUERY_USER_DATA);
        }
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.drawable.setting_userhead).showImageForEmptyUri(R.drawable.setting_userhead)
                .showImageOnFail(R.drawable.setting_userhead).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(android.graphics.Bitmap.Config.RGB_565).build();
    }

    public void onResume() {
        super.onResume();
        if (mainAdapter != null) {
            mainAdapter.notifyDataSetChanged();
        }
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            showData();
        }
        if (mIsCurrentWeek) {
            if (!mIsRunning) {
                startStepService();
                bindStepService();
            } else if (mIsRunning) {
                bindStepService();
            }
            if (!flag) {
                flag = true;
                new Thread(runnable).start();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 每次切换fragment时调用的方法
        if (isVisibleToUser) {
            showData();
        }
    }

    /**
     * 初始化数据
     */
    private void showData() {
        if (isLoadData) {
            initTopViewData();
            isLoadData = false;// 加载数据完成
            // 加载各种数据
            if (mListView != null) {
                mListView.getHeaderContainer().addView(mHeadView);
                mListView.setHeaderView();
                mListView.setmHeaderHeight(Utils.dip2px(getActivity(), 166));
                mainAdapter = new MainAdapter(getActivity(), mUserInfo);
                mListView.setAdapter(mainAdapter);
            }
            // 小于当周之后的数据
            if (mWeek <= mBeforWeek) {
                mainDatabase = MainDatabase.instance(getActivity());
                mainDataModle = mainDatabase.queryBeforeMainCardData(mUserInfo.getUserId(), mDate, mCurrentWeekDay);
                if (mainDataModle != null && mainDataModle.getMainUserModel() != null
                        && mainDataModle.getMainDatas() != null) {
                    mainUserModel = mainDataModle.getMainUserModel();
                    mUserWeightTv.setText(mainUserModel.mWeightModel.getWeight() + "KG");
                    mainDatasList = mainDataModle.getMainDatas();
                    mainAdapter.setData(mainDatasList, mIsCurrentWeek);
                }
                if (mIsCurrentWeek) {
                    SportInfoModel sModel = querySportInfo();
                    if (sModel != null) {
                        mTime = sModel.getTime();
                    }
                    loadData(mDate);
                } else {
                    if (mainDataModle == null || mainDataModle.getSc() == null
                            || mainDataModle.getMainUserModel() == null || mainDataModle.getSc().size() < 7) {
                        loadData(mDate);
                    }
                }
            } else {
                mUserWeightTv.setText("--");
                mUserSymptomsTv.setText("--");
                mUserExaminationTv.setText("--");
            }
        }
    }

    /**
     * 发布心情卡片页面刷新
     */
    public void setRefreshLoad() {
        if (mUserInfo.getUserId() != null) {
            ShareCardModel shareCardModel = MainDatabase.instance(getActivity()).queryShareCardModel(
                    mUserInfo.getUserId(), mCurrentDate);
            if (shareCardModel != null) {
                MainData mainDataShare = new MainData();
                mainDataShare.setFc(shareCardModel);
                mainDataShare.setType(2);
                mainDataShare.setIndex(0);
                mainDatasList.add(0, mainDataShare);
                mListIndex++;
                if (mainAdapter != null) {
                    mainAdapter.setData(mainDatasList, mIsCurrentWeek);
                }
            }
        }
    }

    /**
     * 查询本地婴儿数据信息
     */
    private void initTopViewData() {
        mBabyChangeDao = DatabaseMaster.instance().getBabyChangeDao();
        mBabyChange = mBabyChangeDao.queryBuilder().where(BabyChangeDao.Properties.Week.eq(String.valueOf(mWeek)))
                .unique();
        resID = getResources().getIdentifier("week" + mWeek, "drawable", appInfo.packageName);
        mBabyImage.setImageResource(resID);
        mBabyImage1.setImageResource(resID);
        if (mBabyChange != null) {
            mBabyLengthTv.setText(mBabyChange.getHeight());
            mBabyWeightTv.setText(mBabyChange.getWeight());
            mBabyDetailsTv.setText(mBabyChange.getTips().replace("=", "\n"));
        }
        if (mUserInfo != null) {
            ImageLoader.getInstance().displayImage(mUserInfo.getHeadPic(), mRoundedImageView, defaultOptions);
            mUserNickNameTv.setText(mUserInfo.getNickName());
        }
        setDueDay(mCurrentWeekDay);
    }

    /**
     * 距离预产期还有多少天
     * 
     * @param day
     */
    private void setDueDay(int day) {
        String weekEndDay = StringUtils.getAddDate(mDate.split("-"), day);
        int dueDay = 0;
        if (mUserInfo != null) {
            if (!TextUtils.isEmpty(mUserInfo.getLastMensesTime()) && !mUserInfo.getLastMensesTime().equals("0")) {// 末次月经
                dueDay = StringUtils.getDateSpace(weekEndDay, StringUtils.getAddDate(
                        StringUtils.getStringFromDate(Long.valueOf(mUserInfo.getLastMensesTime())).split("-"), 280));
            } else if (!TextUtils.isEmpty(mUserInfo.getDueTime()) && !mUserInfo.getDueTime().equals("0")) {// 预产期
                String mDueTime = StringUtils.getStringFromDate(Long.valueOf(mUserInfo.getDueTime()));
                dueDay = StringUtils.getDateSpace(weekEndDay, mDueTime);
            }
        }
        this.dueDayStr = "距离预产期" + dueDay + "天";
        mBabyDueTv.setText(dueDayStr);
        mBabyOtherDueTv.setText(dueDayStr);
    }

    @Override
    public void onPause() {
        // TODO 自动生成的方法存根
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
        if (mIsCurrentWeek) {
            flag = false;
            if (mIsRunning) {
                unbindStepService();
                stopStepService();
            }
        }
    }

    /**
     * 获取这周的数据
     * 
     * @param date
     */
    private void loadData(String date) {
        mBlockDialog.show();
        GsonTokenRequest<MainDataBean> request = new GsonTokenRequest<MainDataBean>(
                com.android.volley.Request.Method.POST, Configs.SERVER_URL + "/user/week/data",
                new Listener<JsonResponse<MainDataBean>>() {
                    @Override
                    public void onResponse(JsonResponse<MainDataBean> response) {
                        mBlockDialog.cancel();
                        switch (response.state) {
                        case Configs.UN_USE:
                            showToast("版本过低请升级新版本");
                            break;
                        case Configs.FAIL:
                            showToast(response.msg);
                            break;
                        case Configs.SUCCESS:
                            initViewData(response.data);
                            break;
                        }
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        mBlockDialog.cancel();
                        showToast("网络数据加载失败");
                    }
                }) {
            @Override
            public Type getType() {
                Type type = new TypeToken<JsonResponse<MainDataBean>>() {
                }.getType();
                return type;
            }
        };
        HttpApi.getMainData(getActivity(), "/user/week/data", request, date);
    }

    private void initView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.main_top_include, null);
        mTopHorizontalView = mHeadView.findViewById(R.id.main_top_horizontal_include);
        mTopVerticalView = mHeadView.findViewById(R.id.main_top_vertical_include);
        mUserNickNameTv = (TextView) mHeadView.findViewById(R.id.main_user_nickname_tv);
        mRoundedImageView = (RoundedImageView) mHeadView.findViewById(R.id.main_user_head_image);
        mBabyImage = (ImageView) mHeadView.findViewById(R.id.main_baby_image);
        // mBabyImage.setOnClickListener(this);
        mBabyImage1 = (ImageView) mHeadView.findViewById(R.id.other_baby_icon);
        // mBabyImage1.setOnClickListener(this);
        mHeadView.findViewById(R.id.other_share_image).setOnClickListener(this);
        mHeadView.findViewById(R.id.main_user_memorabilia_tv).setOnClickListener(this);
        // 宝贝的信息
        mBabyLengthTv = (TextView) mHeadView.findViewById(R.id.main_babay_length_tv);
        mBabyWeightTv = (TextView) mHeadView.findViewById(R.id.main_baby_wight_tv);
        mBabyDueTv = (TextView) mHeadView.findViewById(R.id.main_baby_due_tv);
        mBabyDetailsTv = (TextView) mHeadView.findViewById(R.id.other_baby_details_tv);
        mBabyOtherDueTv = (TextView) mHeadView.findViewById(R.id.other_due_day_tv);
        mUserWeightTv = (TextView) mHeadView.findViewById(R.id.main_user_weight_tv);
        mUserWeightTv.setOnClickListener(this);
        mUserSymptomsTv = (TextView) mHeadView.findViewById(R.id.main_user_symptoms_tv);
        mUserSymptomsTv.setOnClickListener(this);
        mUserExaminationTv = (TextView) mHeadView.findViewById(R.id.main_user_examination_tv);
        mUserExaminationTv.setOnClickListener(this);
        mRoundedImageView.setOnClickListener(this);
    }

    /**
     * 封装数据
     * 
     * @param mDataBean
     */
    private void initViewData(MainDataBean mDataBean) {
        mainDatasList.clear();
        this.mMainDataBean = mDataBean;
        int dataSize = 0;
        if (mDataBean != null && mDataBean.getData() != null) {
            List<MainCardDataBean> data = mDataBean.getData();
            if (data.get(0).getSc() != null) {
                mDueDay = data.get(0).getSc().getDueDays();
            }
            dataSize = data.size();
            for (int i = 0; i < dataSize; i++) {
                // 运动卡片
                if (data.get(i).getSi() != null && data.get(i).getSi().getDate() != null) {
                    MainData mainData = new MainData();
                    mainData.setSi(data.get(i).getSi());
                    mainData.setType(1);
                    mainData.setIndex(i);
                    mainData.setDescTime(System.currentTimeMillis());
                    mainDatasList.add(mainData);
                }
                // 分享卡片
                List<ShareCardModel> mShareCardBeansList = data.get(i).getFc();
                if (mShareCardBeansList != null && mShareCardBeansList.size() > 0) {
                    Collections.sort(mShareCardBeansList, new ShareCardComparator());
                    int shareSize = mShareCardBeansList.size();
                    for (int j = 0; j < shareSize; j++) {
                        MainData mainDataShare = new MainData();
                        mainDataShare.setFc(data.get(i).getFc().get(j));
                        mainDataShare.setType(2);
                        mainDataShare.setIndex(i);
                        mainDataShare.setDescTime(System.currentTimeMillis());
                        mainDatasList.add(mainDataShare);
                    }
                }
                // 星座卡片
                if (data.get(i).getSc() != null && data.get(i).getSc().getDate() != null) {
                    MainData mainHspData = new MainData();
                    mainHspData.setSc(data.get(i).getSc());
                    mainHspData.setType(3);
                    mainHspData.setIndex(i);
                    mainHspData.setDescTime(System.currentTimeMillis());
                    mainDatasList.add(mainHspData);
                }
            }
            if (mIsCurrentWeek) {
                setDueDay(dataSize);
            }
        }
        if (mIsCurrentWeek) {
            SportCardModel sportCardModel = new SportCardModel();
            sportCardModel.setCardType(1);
            sportCardModel.setDate(mCurrentDate);
            sportCardModel.setSportMessage("运动卡片");
            sportCardModel.setUserId(mUserInfo.getUserId());
            sportCardModel.setDueDays(mDueDay);

            MainData mainData = new MainData();
            mainData.setSi(sportCardModel);
            mainData.setType(1);
            mainData.setIndex(0);
            mainData.setDescTime(System.currentTimeMillis());
            mainDatasList.add(0, mainData);
        }
        if (mainAdapter != null) {
            mainAdapter.setData(mainDatasList, mIsCurrentWeek);
        }
        if (mDataBean != null && mDataBean.getMw() != null) {
            mUserWeightTv.setText(mDataBean.getMw().getWeight() + "KG");
        }
        // 保存用户top信息
        mainDatabase.mainUserSave(mUserInfo.getUserId(), mDate, mDataBean, mCurrentWeekDay);
        // 保存卡片信息
        mainDatabase.mainCardSave(mUserInfo.getUserId(), mainDatasList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.main_baby_image:
            showBabyDetails();
            break;
        case R.id.other_baby_icon:
            showBabyDetails();
            break;
        case R.id.other_share_image:
            BabyShareDialog mBabyShareDialog = new BabyShareDialog(getActivity());
            mBabyShareDialog.babyShareDialog(dueDayStr, mBabyChange.getTips() == null ? "" : mBabyChange.getTips(),
                    resID);
            break;
        case R.id.main_user_head_image:
            startIntent(MineActivity.class);
            break;
        case R.id.main_user_memorabilia_tv:
            startIntent(BigActivity.class);
            break;
        case R.id.main_user_weight_tv:
            if (mIsCurrentWeek) {
                DialogUtils.showMainWeightDialog(getActivity(), 30, 121, 6, new OnWeightListener() {
                    String content;

                    @Override
                    public void onWeight(String weight) {
                        content = weight;
                    }

                    @Override
                    public void isSure(boolean isFlage) {
                        if (isFlage) {
                            mUserWeightTv.setText(content);
                        }
                    }
                });
            }
            break;
        case R.id.main_user_symptoms_tv:
            if (mIsCurrentWeek) {
                if (mMainDataBean != null && mMainDataBean.getMs() != null) {
                    mSymptomDialog(mMainDataBean.getMs());
                }
            } else {
                if (mainUserModel != null) {
                    mSymptomDialog(mainUserModel.mSymptomModel);
                }
            }
            break;
        case R.id.main_user_examination_tv:
            if (mIsCurrentWeek) {
                if (mMainDataBean != null && mMainDataBean.getMsc() != null) {
                    mScDialog(mMainDataBean.getMsc());
                }
            } else {
                if (mainUserModel != null) {
                    mScDialog(mainUserModel.mscModel);
                }
            }
            break;
        default:
            break;
        }
    }

    /**
     * 症状dialog
     */
    private void mSymptomDialog(List<MSymptomModel> mSymptomBeans) {
        DialogUtils.showMainSymptomDialog(getActivity(), mSymptomBeans, mIsCurrentWeek);
    }

    /**
     * 自查dialog
     */
    private void mScDialog(List<MscModel> mscBeans) {
        Map<Integer, MscModel> map = new HashMap<Integer, MscModel>();
        for (int i = 0; i < mscBeans.size(); i++) {
            map.put(i, mscBeans.get(i));
        }
        DialogUtils.showMainSelfDialog(getActivity(), map, mIsCurrentWeek);
    }

    public void showBabyDetails() {
        if (mTopHorizontalView.getVisibility() == View.VISIBLE) {
            mTopHorizontalView.setVisibility(View.GONE);
            mTopVerticalView.setVisibility(View.VISIBLE);
        } else {
            mTopHorizontalView.setVisibility(View.VISIBLE);
            mTopVerticalView.setVisibility(View.GONE);
        }
    }

    int i = 0;

    /**
     * 更新运动信息
     * 
     * @param sportInfo
     * @param isflage
     */
    private void updateSportInfo(String sportInfo, boolean isflage) {
        if (mainAdapter != null) {
            // String time = StringUtils.getSysDate().substring(10, 16);
            if (mIsCurrentWeek) {
                SportCardModel sportCardModel = new SportCardModel();
                sportCardModel.setCardType(1);
                sportCardModel.setDate(mCurrentDate);
                sportCardModel.setSportMessage(sportInfo);
                sportCardModel.setUserId(mUserInfo.getUserId());
                sportCardModel.setDueDays(mDueDay);

                MainData mainData = new MainData();
                mainData.setSi(sportCardModel);
                mainData.setType(1);
                mainData.setIndex(0);
                mainData.setDescTime(System.currentTimeMillis());
                if (isflage && mListIndex > 0) {
                    mainDatasList.remove(mListIndex);
                    mListIndex = 0;
                    mainDatasList.add(mListIndex, mainData);
                } else {
                    mainDatasList.set(mListIndex, mainData);
                }
                mainAdapter.setData(mainDatasList, mIsCurrentWeek);
            }
        }
        i++;
    }

    /* 运动 */
    private BeforTraining mBeforTraining;
    /**
     * True, when service is running.
     */
    private boolean mIsRunning = false;
    private Training training;
    private String trainingString = "";
    private int mStepValue = 0;
    private boolean isStartStep = false;
    private float mTime = 0.0f;
    private float mStartTime = 0.0f;
    private float mTallTime = 0.0f;
    Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
            case STEPS_MSG:
                mStepValue = (int) msg.arg1;
                break;
            case TRAIN_MSG: {
                mStartTime = mBeforTraining.getInternalFactor().getPersonInfo().getTrainingInfo().getTrainTime() / 60.0f;
                isStartStep = true;
                mTallTime = mTime + mStartTime;
                updateSportInfo(getStyleString(true, mTallTime), true);
            }
                break;
            case REST_MSG: {
                if (isStartStep) {
                    saveSportInfo();
                    SportInfoModel sModel = querySportInfo();
                    if (sModel != null) {
                        mTime = sModel.getTime();
                    }
                    isStartStep = false;
                }
                updateSportInfo(getStyleString(false, mTime), false);
            }
                break;
            default:
                super.handleMessage(msg);
            }
        };
    };

    private void saveSportInfo() {
        SportInfoModel mSportInfoModel = SportInfoUtils.getSportInfoModel(mUserInfo.getUserId(),
                getStyleString(false, mTallTime), mCurrentDate, mStepValue, mTallTime);

        SportInfoUtils.instance().saveSportInfo(mUserInfo.getUserId(), mCurrentDate, mSportInfoModel);
    }

    private SportInfoModel querySportInfo() {
        SportInfoModel mSportInfoModel = SportInfoUtils.instance().querySportInfo(mUserInfo.getUserId(), mCurrentDate);
        if (mSportInfoModel != null) {
            return mSportInfoModel;
        } else {
            return null;
        }
    }

    private String getStyleString(boolean isflage, float time) {
        String string = "";
        String weather = mBeforTraining.getExternalFactor().getWeather().getFeedback();
        String temp = mBeforTraining.getExternalFactor().getTemperature().getMinTemperature() + "℃";
        String aqi = mBeforTraining.getExternalFactor().getAirQuality().getFeedback() + "<br/>";
        String runTime = "";
        if (isflage) {
            runTime = "已经走了<font color = \"#ff7999\">" + time + "</font>分钟,";
        } else {
            runTime = "总共走了<font color = \"#ff7999\">" + time + "</font>分钟,";
        }
        String wholeStep = "共计<font color = \"#ff7999\">"
                + mBeforTraining.getInternalFactor().getPersonInfo().getStepCount() + "</font>步了哦。<br/>";
        String suggestTime = "";
        String speed = "";
        if (!training.getSpeedFeedback().equals("")) {
            speed = training.getSpeedFeedback() + "<br/>";
        }
        if (mBeforTraining.couldTraining()) {
            suggestTime = "建议步行<font color = \"#ff7999\">"
                    + mBeforTraining.getInternalFactor().getPersonInfo().getMaxTrainingSlotTime() + "</font>分钟。";
        }
        string = weather + temp + aqi + runTime + wholeStep + speed + suggestTime;
        return string;
    }

    private static final int STEPS_MSG = 1;
    private static final int PACE_MSG = 2;
    private static final int DISTANCE_MSG = 3;
    private static final int SPEED_MSG = 4;
    private static final int CALORIES_MSG = 5;
    private static final int TRAIN_MSG = 6;
    private static final int REST_MSG = 7;
    // TODO: unite all into 1 type of message
    private com.xiaoaitouch.mom.pedometer.StepService.ICallback mCallback = new com.xiaoaitouch.mom.pedometer.StepService.ICallback() {
        public void stepsChanged(int value) {
            handler.sendMessage(handler.obtainMessage(STEPS_MSG, value, 0));
        }

        public void paceChanged(int value) {
            // handler.sendMessage(handler.obtainMessage(PACE_MSG, value, 0));
        }

        public void distanceChanged(float value) {
            // handler.sendMessage(handler.obtainMessage(DISTANCE_MSG, (int)
            // (value * 1000), 0));
        }

        public void speedChanged(float value) {
            // handler.sendMessage(handler.obtainMessage(SPEED_MSG, (int) (value
            // * 1000), 0));
        }

        public void caloriesChanged(float value) {
            // handler.sendMessage(handler.obtainMessage(CALORIES_MSG, (int)
            // (value), 0));
        }
    };
    OnTrainingLisener onTrainingLisener = new OnTrainingLisener() {
        @Override
        public void onTraining(boolean isTraining, float speed, String content) {
            // TODO 自动生成的方法存根
            trainingString = content;
            if (isTraining) {
                handler.sendEmptyMessage(TRAIN_MSG);
            } else {
                handler.sendEmptyMessage(REST_MSG);
            }
        }
    };
    private StepService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((StepService.StepBinder) service).getService();
            mService.registerCallback(mCallback);
            mService.reloadSettings();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private void startStepService() {
        if (!mIsRunning) {
            mIsRunning = true;
            getActivity().startService(new Intent(getActivity(), StepService.class));
        }
    }

    private void bindStepService() {
        getActivity().bindService(new Intent(getActivity(), StepService.class), mConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        getActivity().unbindService(mConnection);
    }

    private void stopStepService() {
        if (mService != null) {
            getActivity().stopService(new Intent(getActivity(), StepService.class));
        }
        mIsRunning = false;
    }

    boolean flag = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PmWeatherBean pmWeatherBean = ((MomApplication) getActivity().getApplication()).getPmWeatherBean();
                if (pmWeatherBean != null) {
                    ExternalFactor mExternalFactor = mBeforTraining.getExternalFactor();
                    if (pmWeatherBean.getWeather() != null) {
                        mExternalFactor.getWeather().setWeather(
                                pmWeatherBean.getWeather().getDay_weather() == null ? "" : pmWeatherBean.getWeather()
                                        .getDay_weather());
                        if (pmWeatherBean.getWeather().getNight_temp() == null) {
                            mExternalFactor.getTemperature().setMinTemperature(0);
                        } else
                            mExternalFactor.getTemperature().setMinTemperature(
                                    Integer.valueOf(pmWeatherBean.getWeather().getNight_temp()));
                    }
                    // /
                    // mExternalFactor.getAirQuality().setAqi(pmWeatherBean.getPm2_5().getAqi());
                }
                training.setCouldTrain(mBeforTraining.couldTraining());
                training.trainingLisener(mStepValue);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}