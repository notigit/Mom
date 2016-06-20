package com.xiaoaitouch.mom.train.model;

/**
 * 孕周期
 * 
 * @author LG
 * 
 */
public class Pregnancy {
    /**
     * 孕早期开始时间
     */
    public static final int EARLY_PREGNANCY = 1;

    /**
     * 孕中期开始时间
     */
    public static final int MID_PREGNANCY = 13;

    /**
     * 孕晚期开始时间
     */
    public static final int LATE_PREGNANCY = 28;

    /**
     * 怀孕天数
     */
    private int mDays;

    /**
     * 孕周期
     */
    private int mWeek;

    /**
     * 孕周期中第几天
     */
    private int mWeekDay;

    private String mFeedback = "";

    public int getDays() {
        return mDays;
    }

    public void setDays(int mDays) {
        this.mDays = mDays;
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int mWeek) {
        this.mWeek = mWeek;
    }

    public int getWeekDay() {
        return mWeekDay;
    }

    public void setWeekDay(int mWeekDay) {
        this.mWeekDay = mWeekDay;
    }

    public String getFeedback() {
        return mFeedback;
    }

    public void setFeedback(String mFeedback) {
        this.mFeedback = mFeedback;
    }

}
