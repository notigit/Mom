package com.xiaoaitouch.mom.train.model;

import java.util.Date;

import com.xiaoaitouch.mom.train.Utils;

import android.os.Handler;

/**
 * 当前连续运动信息
 * 
 * @author LG
 * 
 */
public class TrainingInfo {

    /**
     * 最大连续运动时间 30分钟
     */
    public static final int MAX_CONTINUOUS_TRAINING_TIME = 1800;

    /**
     * 提示休息片刻时间 20分钟
     */
    public static final int REST_CONTINUOUS_TRAINING_TIME = 1200;

    // /**
    // * 每分钟运动不超过10步视为开始休息
    // */
    // public static final int REST_STEP = 10;

    /**
     * 休息判定时间 60秒
     */
    public static final int REST_JUDGED_TIME = 60;

    private Date mStartDate;
    private Date mEndDate;

    /**
     * 当前连续运动时间
     */
    private long mTrainTime;

    /**
     * 当前连续运动步数
     */
    private int mStepCount;
    private float mCalorie;
    private float mSpeed;
    private float mStepFrequency;
    private String mAnalyseString = "";

    /**
     * 监测并反馈运动状态
     */
    private Handler mHandler;

    public TrainingInfo() {
        initInfo();
    }

    private void initInfo() {
        clearInfo();
    }

    public void clearInfo() {
        mStartDate = new Date();
        mEndDate = new Date();
        mTrainTime = 0;
        mStepCount = 0;
        mCalorie = 0;
        mSpeed = 0;
        mStepFrequency = 0;
        mAnalyseString = "";
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public long getTrainTime() {
        return mTrainTime;
    }

    public void setTrainTime(long mTrainTime) {
        this.mTrainTime = mTrainTime;
        if (this.mTrainTime != 0) {
            setStepFrequency(mStepCount * 1.0f / this.mTrainTime);
        }
    }

    public void addTrainTime(long mTrainTime) {
        this.mTrainTime += mTrainTime;
        if (this.mTrainTime != 0) {
            setStepFrequency(mStepCount * 1.0f / this.mTrainTime);
        }
    }

    public int getStepCount() {
        return mStepCount;
    }

    public void setStepCount(int mStepCount) {
        this.mStepCount = mStepCount;
    }

    public void addStepCount(int mStepCount) {
        this.mStepCount += mStepCount;
    }

    public float getCalorie() {
        // return mCalorie;
        return Utils.comCalorie(mStepCount);
    }

    public float getDistance() {
        return Utils.comDistance(mStepCount);
    }

    public void setCalorie(float mCalorie) {
        this.mCalorie = mCalorie;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
    }

    public float getStepFrequency() {
        return mStepFrequency;
    }

    public void setStepFrequency(float mStepFrequency) {
        this.mStepFrequency = mStepFrequency;
    }

    interface OnTrainFeedback {
        public void OnTrainFeedback();
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    public void setAnalyseString(String mAnalyseString) {
        this.mAnalyseString = mAnalyseString;
    }

    public String getAnalyseString() {
        return this.mAnalyseString;
    }
}
