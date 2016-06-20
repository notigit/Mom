package com.xiaoaitouch.mom.train;

import java.util.Date;

import com.xiaoaitouch.mom.train.model.PersonInfo;
import com.xiaoaitouch.mom.train.model.Pregnancy;
import com.xiaoaitouch.mom.train.model.TimeTrainingSegment;

public class InternalFactor {
    // TODO 加入反馈信息
    private PersonInfo mPersonInfo;

    public InternalFactor() {
        init();
    }

    private void init() {
        mPersonInfo = PersonInfo.getInstance();
    }

    public PersonInfo getPersonInfo() {
        return mPersonInfo;
    }

    public void setPersonInfo(PersonInfo mPersonInfo) {
        this.mPersonInfo = mPersonInfo;
    }

    public boolean isPeriodicityCouldTrain() {
        // 孕周期过早则不推荐运动
        if (mPersonInfo.getPregnancy().getWeek() <= Pregnancy.MID_PREGNANCY) {
            mPersonInfo.getPregnancy().setFeedback("当前孕周期不建议运动");
            return false;
        }
        mPersonInfo.getPregnancy().setFeedback("当前孕周期可以运动");
        return true;
    }

    public boolean isTrainingTimeCouldTrain() {
        if (mPersonInfo.getTimeSlotTraining().getTime() >= mPersonInfo.getMaxTrainingTime()
                * (1 - PersonInfo.TEN_PERCENT)) {
            // 运动量超过额定的90%就不推荐出去运动了
            mPersonInfo.getTimeSlotTraining().setTrainTimeFeedback("运动时间足够，不建议运动");
            return false;
        }
        mPersonInfo.getTimeSlotTraining().setTrainTimeFeedback("运动时间不足，建议运动");
        return true;
    }

    public boolean isTrainingTimeSpacingCouldTrain() {
        // 与上次运动时间间隔超出30分钟
        int hour = mPersonInfo.getTimeSlotTraining().getLastTrainingHour();
        int min = mPersonInfo.getTimeSlotTraining().getLastTrainingMin();
        Date date = new Date();
        int nowHour = date.getHours();
        int nowMin = date.getMinutes();
        int subMin = (nowHour - hour) * 60 + nowMin - min;
        // if (subMin < TimeTrainingSegment.MIN_TRAINING_TIME_SPACING) {
        if (subMin < 1) {
            mPersonInfo.getTimeSlotTraining().setTrainTimeSpacingFeedback("运动间隔不足1min，不建议运动");
            return false;
        }
        mPersonInfo.getTimeSlotTraining().setTrainTimeSpacingFeedback("运动间隔超过1min，建议运动");
        return true;
    }

    public boolean isSymptomCouldTrain() {
        if (mPersonInfo.getSymptom().getSymptomCount() > 0) {
            // 有症状则不推荐
            mPersonInfo.getSymptom().setFeedback("有症状，推荐就医，不建议运动。");
            return false;
        }
        mPersonInfo.getSymptom().setFeedback("无症状，建议运动。");
        return true;
    }

    public boolean isInternalFactor() {
        boolean isPeriodicity = isPeriodicityCouldTrain();
        boolean isSymptom = isSymptomCouldTrain();
        boolean isTrainingTime = isTrainingTimeCouldTrain();
        boolean isTrainingTimeSpacing = isTrainingTimeSpacingCouldTrain();
        return (isPeriodicity && isSymptom && isTrainingTime && isTrainingTimeSpacing);
        // if (isPeriodicityCouldTrain() && isSymptomCouldTrain() &&
        // isTrainingTimeCouldTrain()
        // && isTrainingTimeSpacingCouldTrain()) {
        // return true;
        // }
        // return false;
    }

    public String getPeriodicityFeedback() {
        return mPersonInfo.getPregnancy().getFeedback();
    }

    public String getSymptomFeedback() {
        return mPersonInfo.getSymptom().getFeedback();
    }

    public String getTrainingTimeFeedback() {
        return mPersonInfo.getTimeSlotTraining().getTrainTimeFeedback();
    }

    public String getTrainingTimeSpacingFeedback() {
        return mPersonInfo.getTimeSlotTraining().getTrainTimeSpacingFeedback();
    }
}
