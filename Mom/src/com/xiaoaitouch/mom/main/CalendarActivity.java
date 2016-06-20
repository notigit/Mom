package com.xiaoaitouch.mom.main;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Response.Listener;
import com.google.gson.reflect.TypeToken;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.CalendarAdapter;
import com.xiaoaitouch.mom.bean.CalendarBean;
import com.xiaoaitouch.mom.bean.CalendarBean.Calendars;
import com.xiaoaitouch.mom.bean.DayDate;
import com.xiaoaitouch.mom.bean.NewCalendar;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.SharedPreferencesUtil;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.util.Utils;

/**
 * 日历
 * 
 * @author huyxin
 * 
 */
public class CalendarActivity extends Activity {
    @Bind(R.id.calendar_listview)
    ListView mListView;

    private CalendarAdapter mAdapter;
    private UserInfo mUserInfo;
    private int weekDay = 1;
    private List<NewCalendar> mCalendars = new ArrayList<NewCalendar>();
    private String[] startTime;
    private String[] nextStartTime;
    private String currentDate;
    private String dueStr;
    private String mSelectDate;
    private List<Calendars> mCalendarBeans = null;
    private MomApplication application;
    protected Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MomApplication) getApplication();
        mUserInfo = application.getUserInfo();
        mCalendarBeans = application.getCalendarBeans();
        mActivity = this;
        setContentView(R.layout.calendar_activity);
        ButterKnife.bind(this);
        initData();
        if (mCalendarBeans == null) {
            getCalendarData();
        } else {
            mAdapter.setCalendars(mCalendars, mCalendarBeans);
        }
    }

    private void initData() {
        Calendar now = Calendar.getInstance();
        currentDate = now.get(Calendar.YEAR) + "-" + StringUtils.getDataValues(now.get(Calendar.MONTH) + 1) + "-"
                + StringUtils.getDataValues(now.get(Calendar.DATE));

        String[] dueTime = getDueDate();
        mSelectDate = SharedPreferencesUtil.getString(mActivity, "user_select_date", currentDate);
        dueStr = dueTime[0] + "-" + StringUtils.getDataValues(Integer.valueOf(dueTime[1])) + "-"
                + StringUtils.getDataValues(Integer.valueOf(dueTime[2]));
        if (dueTime != null) {// 获取怀孕的日期是星期几
            weekDay = StringUtils.getWeekDate(Integer.valueOf(dueTime[0]), Integer.valueOf(dueTime[1]),
                    Integer.valueOf(dueTime[2])) - 1;
            if (weekDay != 7) {
                startTime = StringUtils.getAddDate(dueTime, -weekDay).split("-");
            } else {
                startTime = dueTime;
            }
        }
        structureTime();
        mAdapter = new CalendarAdapter(CalendarActivity.this);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 数据提交
     */
    private void getCalendarData() {
        GsonTokenRequest<CalendarBean> request = new GsonTokenRequest<CalendarBean>(
                com.android.volley.Request.Method.POST, Configs.SERVER_URL + "/user/mom/data",
                new Listener<JsonResponse<CalendarBean>>() {

                    @Override
                    public void onResponse(JsonResponse<CalendarBean> response) {
                        switch (response.state) {
                        case Configs.UN_USE:
                            showToast("版本过低请升级新版本");
                            break;
                        case Configs.FAIL:
                            showToast(response.msg);
                            break;
                        case Configs.SUCCESS:
                            if (response.data != null) {
                                mCalendarBeans = response.data.getCalendar();
                                mAdapter.setCalendars(mCalendars, mCalendarBeans);
                                application.setCalendarBeans(mCalendarBeans);
                            }
                            break;
                        }
                    }

                }, null) {

            @Override
            public Type getType() {
                Type type = new TypeToken<JsonResponse<CalendarBean>>() {
                }.getType();

                return type;
            }
        };
        HttpApi.getCalendar(mActivity, "/user/mom/data", request, null);
    }

    /**
     * 获得每一周的时间段
     * 
     * @param startTime
     * @return
     */
    private String getTimeSlot(String[] startTime) {
        String str = "";
        String[] endTime = StringUtils.getAddDate(startTime, 6).split("-");
        str = startTime[1] + "月" + startTime[2] + "-" + endTime[1] + "月" + endTime[2] + "号";
        nextStartTime = StringUtils.getAddDate(endTime, 1).split("-");
        return str;
    }

    /**
     * 构造40周数据
     */
    private void structureTime() {
        NewCalendar calendar = new NewCalendar();
        calendar.setWeek(1);
        calendar.setDueDate(dueStr);
        calendar.setCurrentDate(currentDate);
        calendar.setWeekEndDate(startTime);
        calendar.setTimeSlot(getTimeSlot(startTime));
        calendar.setSelectDate(mSelectDate);
        List<DayDate> mDayDates = new ArrayList<DayDate>();
        for (int i = 0; i < 7; i++) {
            DayDate dayDate = new DayDate();
            dayDate.setDayDate(StringUtils.getAddDate(startTime, i));
            mDayDates.add(dayDate);
        }
        calendar.setmDayDates(mDayDates);
        mCalendars.add(calendar);

        for (int i = 2; i <= 40; i++) {
            List<DayDate> list = new ArrayList<DayDate>();
            NewCalendar mCalendar = new NewCalendar();
            for (int j = 0; j < 7; j++) {
                DayDate dayDate = new DayDate();
                dayDate.setDayDate(StringUtils.getAddDate(nextStartTime, j));
                list.add(dayDate);
            }
            mCalendar.setSelectDate(mSelectDate);
            mCalendar.setCurrentDate(currentDate);
            mCalendar.setDueDate(dueStr);
            mCalendar.setWeekEndDate(nextStartTime);
            mCalendar.setWeek(i);
            mCalendar.setmDayDates(list);
            mCalendar.setTimeSlot(getTimeSlot(nextStartTime));
            mCalendars.add(mCalendar);
        }
    }

    /**
     * 获取怀孕的日期
     * 
     * @return
     */
    private String[] getDueDate() {
        String[] dueTime = null;
        if (mUserInfo != null) {
            if (!TextUtils.isEmpty(mUserInfo.getLastMensesTime()) && !mUserInfo.getLastMensesTime().equals("0")) {// 末次月经
                dueTime = StringUtils.getStringFromDate(Long.valueOf(mUserInfo.getLastMensesTime())).split("-");
            } else {
                if (!TextUtils.isEmpty(mUserInfo.getDueTime()) && !mUserInfo.getDueTime().equals("0")) {// 预产期
                    String[] mDueTime = StringUtils.getStringFromDate(Long.valueOf(mUserInfo.getDueTime())).split("-");
                    dueTime = StringUtils.getAddDate(mDueTime, -280).split("-");
                }
            }
        }
        return dueTime;
    }

    @OnClick(R.id.activity_left_tv)
    public void onBack() {
        finish();
        overridePendingTransition(R.anim.calendar_in_out, R.anim.calendar_miss);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            overridePendingTransition(R.anim.calendar_in_out, R.anim.calendar_miss);
            onBack();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void showToast(CharSequence text) {
        Utils.showToast(text, Toast.LENGTH_SHORT);
    }
}
