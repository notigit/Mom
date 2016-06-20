package com.xiaoaitouch.mom.train.model;

import com.xiaoaitouch.mom.train.Utils;

/**
 * 孕妇相关属性
 * 
 * @author LG
 * 
 */
public class PersonInfo {

    private int mMaxTrainingTime = 0;
    private int mMaxTrainingSlotTime = 0;
    private int mMaxTrainingStep = 0;
    private float mMaxCalorie = 0;
    public static final float TEN_PERCENT = 0.1f;
    public static final float TWENTY_PERCENT = 0.2f;

    /**** 运动量相关 ****/

    private int mStepCount;
    private float mCalorie;

    /**
     * 分段运动时间统计
     */
    private TimeTrainingSegment mTimeSlot;

    /**** 运动量相关end ****/

    /**** 运动幅度相关 ****/

    private float mSpeed;
    /**** 运动幅度相关end ****/

    private int mAge;
    private float mHeight;
    private float mWeight;
    private float mBmi;
    private float mDistance;
    private Pregnancy mPregnancy;

    /**
     * 当前症状
     */
    private Symptom mSymptom;

    private TrainingInfo mTrainingInfo;

    private static PersonInfo mInstance = null;

    private String mAnalyseString = "";

    public static PersonInfo getInstance() {
        if (mInstance == null) {
            mInstance = new PersonInfo();
        }
        return mInstance;
    }

    public PersonInfo() {
        mPregnancy = new Pregnancy();
        mSymptom = new Symptom();
        mTimeSlot = new TimeTrainingSegment();
        mTrainingInfo = new TrainingInfo();
        mPregnancy.setWeek(14);
    }

    private void comBMI() {
        if (mWeight != 0) {
            setBmi(mWeight / mHeight / mHeight);
        }
    }

    public int getStepCount() {
        return mStepCount;
    }

    public void setStepCount(int mStepCount) {
        this.mStepCount = mStepCount;
    }

    public float getCalorie() {
        // return mCalorie;
        // return m
        // 计算卡路里
        return Utils.comCalorie(mStepCount);
    }

    public void setCalorie(float mCalorie) {
        this.mCalorie = mCalorie;
    }

    public long getDayMoveTime() {
        return mTimeSlot.getTime();
    }

    public TimeTrainingSegment getTimeSlotTraining() {
        return mTimeSlot;
    }

    public void setTimeSlotTraining(TimeTrainingSegment timeSlot) {
        this.mTimeSlot = timeSlot;
    }

    public void addTimeSlotTraining(TrainingInfo trainingInfo) {
        TrainingInfo tempInfo = new TrainingInfo();
        tempInfo.setCalorie(tempInfo.getCalorie());
        tempInfo.setEndDate(trainingInfo.getEndDate());
        tempInfo.setSpeed(trainingInfo.getSpeed());
        tempInfo.setStartDate(trainingInfo.getStartDate());
        tempInfo.setStepCount(trainingInfo.getStepCount());
        tempInfo.setStepFrequency(trainingInfo.getStepFrequency());
        tempInfo.setTrainTime(trainingInfo.getTrainTime());
        tempInfo.setAnalyseString(trainingInfo.getAnalyseString());
        this.mTimeSlot.addTrainingSegment(tempInfo);
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
    }

    public float getStepFrequency() {
        return this.mTimeSlot.getStepFrequency();
    }

    public float getDistance() {
        return Utils.comDistance(mStepCount);
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int mAge) {
        this.mAge = mAge;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float mHeight) {
        this.mHeight = mHeight;
        comBMI();
    }

    public float getWeight() {
        return mWeight;
    }

    public void setWeight(float mWeight) {
        this.mWeight = mWeight;
        comBMI();
    }

    public float getBmi() {
        return mBmi;
    }

    public void setBmi(float mBmi) {
        this.mBmi = mBmi;
        selectTrainAmount();
    }

    public Pregnancy getPregnancy() {
        return mPregnancy;
    }

    public void setPregnancy(Pregnancy mPregnancy) {
        this.mPregnancy = mPregnancy;
    }

    public Symptom getSymptom() {
        return mSymptom;
    }

    public void setSymptom(Symptom mSymptom) {
        this.mSymptom = mSymptom;
    }

    public TrainingInfo getTrainingInfo() {
        return mTrainingInfo;
    }

    public void setTrainingInfo(TrainingInfo mTrainingInfo) {
        this.mTrainingInfo = mTrainingInfo;
    }

    public String getAnalyseString() {
        return mAnalyseString;
    }

    public void setAnalyseString(String mAnalyseString) {
        this.mAnalyseString = mAnalyseString;
    }

    private void selectTrainAmount() {
        // 运动量设置
        if (mBmi < 19.8) {
            mMaxTrainingSlotTime = 20 * 60;
            mMaxTrainingTime = 60 * 60;
        } else if (mBmi < 26) {
            mMaxTrainingSlotTime = 20 * 60;
            mMaxTrainingTime = 60 * 60;
        } else if (mBmi < 29) {
            mMaxTrainingSlotTime = 25 * 60;
            mMaxTrainingTime = 75 * 60;
        } else {
            mMaxTrainingSlotTime = 25 * 60;
            mMaxTrainingTime = 90 * 60;
        }
    }

    public int getMaxTrainingTime() {
        return mMaxTrainingTime;
    }

    public int getMaxTrainingSlotTime() {
        return mMaxTrainingSlotTime/60;
    }

    public void setMaxTrainingTime(int mMaxTrainingTime) {
        this.mMaxTrainingTime = mMaxTrainingTime;
    }

    public void setMaxTrainingSlotTime(int mMaxTrainingSlotTime) {
        this.mMaxTrainingSlotTime = mMaxTrainingSlotTime;
    }
}