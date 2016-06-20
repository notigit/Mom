package com.xiaoaitouch.mom.train.model;

import java.util.Date;

import android.util.Log;

public class NowTime extends BaseFeedbackModel {
    private Date mNowTime;
    private static final int MORNING_START_TIME = 9;
    private static final int MORNING_END_TIME = 10;
    private static final int EVENING_START_TIME = 19;
    private static final int EVENING_END_TIME = 20;
    private static final int EVENING_CAN_NOT_START_TIME = 22;
    private static final int AFTERNOON_CAN_NOT_START_TIME = 16;

    public void setNowTime(Date mNowTime) {
        this.mNowTime = mNowTime;
        Log.d("mNowTime", mNowTime.getHours() + "");
        selectK();
    }

    public Date getNowTime() {
        return mNowTime;
    }

    private void selectK() {
        if (mNowTime.getHours() >= MORNING_START_TIME && mNowTime.getHours() < MORNING_END_TIME) {
            // 早上时间
            setFeedback("早饭后走一走");
            setK(2);
        } else if (mNowTime.getHours() >= EVENING_START_TIME && mNowTime.getHours() <= EVENING_END_TIME) {
            // 晚上时间
            setFeedback("晚饭后走一走");
            setK(2);
        } else if (mNowTime.getHours() >= AFTERNOON_CAN_NOT_START_TIME && mNowTime.getHours() < EVENING_START_TIME) {
            // 16-19点
            setK(0);
            setFeedback("下班时间，不建议出行");
        } else if (mNowTime.getHours() >= EVENING_CAN_NOT_START_TIME || mNowTime.getHours() < MORNING_START_TIME) {
            // 22点以后
            setFeedback("夜深了，不建议出行");
            setK(0);
        } else {
            // 其他时间
            setFeedback("时间不错");
            setK(1);
        }
    }

}
