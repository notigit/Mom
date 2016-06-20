package com.xiaoaitouch.mom.train.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xiaoaitouch.mom.train.Utils;

/**
 * 分时间段统计运动
 * 
 * @author LG
 * 
 */
public class TimeTrainingSegment {

    private List<TrainingInfo> mTrainingInfoList;
    private long mTime = 0;
    private int mStepCount = 0;
    private int mLastTrainingHour = 0;
    private int mLastTrainingMin = 0;
    private String mTrainTimeFeedback = "";
    private String mTrainTimeSpacingFeedback = "";

    /**
     * 运动时间间隔30分钟
     */
    public static int MIN_TRAINING_TIME_SPACING = 30;

    public TimeTrainingSegment() {
        init();
    }

    private void init() {
        mTrainingInfoList = new ArrayList<TrainingInfo>();
    }

    public void addTrainingSegment(TrainingInfo trainingInfo) {
        mTrainingInfoList.add(0, trainingInfo);
        // mTime += trainingInfo.getTrainTime();
        // mStepCount += trainingInfo.getStepCount();
        setLastTrainingHour(trainingInfo.getEndDate().getHours());
        setLastTrainingMin(trainingInfo.getEndDate().getMinutes());
    }

    public void addTime(long time) {
        mTime += time;
    }

    public void addStep(int step) {
        mStepCount += step;
    }

    private void setLastTrainingHour(int mLastTrainingHour) {
        this.mLastTrainingHour = mLastTrainingHour;
    }

    private void setLastTrainingMin(int mLastTrainingMin) {
        this.mLastTrainingMin = mLastTrainingMin;
    }

    public int getLastTrainingHour() {
        return this.mLastTrainingHour;
    }

    public int getLastTrainingMin() {
        return this.mLastTrainingMin;
    }

    public boolean removeTrainingSegment(TrainingInfo trainingSegment) {
        if (trainingSegment == null) {
            return false;
        }
        mTrainingInfoList.remove(trainingSegment);
        return true;
    }

    public void clearTrainingSegmentList() {
        mTrainingInfoList.clear();
    }

    public List<TrainingInfo> getTrainingInfoList() {
        return mTrainingInfoList;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long mTime) {
        this.mTime = mTime;
    }

    public long getStepCount() {
        return mStepCount;
    }

    public void setStepCount(int mStepCount) {
        this.mStepCount = mStepCount;
    }

    public float getStepFrequency() {
        if (mTime == 0) {
            return 0;
        }
        return mStepCount * 1.0f / mTime;
    }

    public float getCalorie() {
        return Utils.comCalorie(mStepCount);
    }

    public float getDistance() {
        return Utils.comDistance(mStepCount);
    }

    public class TimeTraining {
        private Date mStartDate;
        private Date mEndDate;
        private int mStepCount;
        private float mCalorie;
        private float mSpeed;
        private float mStepFrequency;

        public TimeTraining() {
            // TODO 自动生成的构造函数存根
        }

        public TimeTraining(Date mStartDate, Date mEndDate, int mStepCount, float mCalorie, float mSpeet,
                float mStepFrequency) {
            super();
            this.mStartDate = mStartDate;
            this.mEndDate = mEndDate;
            this.mStepCount = mStepCount;
            this.mCalorie = mCalorie;
            this.mSpeed = mSpeet;
            this.mStepFrequency = mStepFrequency;
        }

        public Date getStartDate() {
            return mStartDate;
        }

        public void setStartDate(Date mStartDate) {
            this.mStartDate = mStartDate;
        }

        public Date getEndDate() {
            return mEndDate;
        }

        public void setEndDate(Date mEndDate) {
            this.mEndDate = mEndDate;
        }

        public int getStepCount() {
            return mStepCount;
        }

        public void setStepCount(int mStepCount) {
            this.mStepCount = mStepCount;
        }

        public float getCalorie() {
            return Utils.comCalorie(mStepCount);
            // return mCalorie;
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

    }

    public String getTrainTimeFeedback() {
        return mTrainTimeFeedback;
    }

    public String getTrainTimeSpacingFeedback() {
        return mTrainTimeSpacingFeedback;
    }

    public void setTrainTimeFeedback(String mTrainTimeFeedback) {
        this.mTrainTimeFeedback = mTrainTimeFeedback;
    }

    public void setTrainTimeSpacingFeedback(String mTrainTimeSpacingFeedback) {
        this.mTrainTimeSpacingFeedback = mTrainTimeSpacingFeedback;
    }

}
