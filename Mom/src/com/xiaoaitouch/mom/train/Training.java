package com.xiaoaitouch.mom.train;

import java.util.Date;

import com.xiaoaitouch.mom.train.model.Constraint;
import com.xiaoaitouch.mom.train.model.PersonInfo;

public class Training {
    /**
     * 查询间隔单位ms
     */
    public static final long CHECK_TIME = 6000;

    /**
     * 运动状态改变，需要采集信息的次数
     */
    private final int CHECK_TRAINING_TIMES = 5;
    private final float perSec = (float) (CHECK_TIME / 1000);

    // 检查段最小步数
    private final int minStep = 4;

    private PersonInfo mPersonInfo;

    private boolean isTraining;
    private int trainingFlag = 0;
    private int restFlag = 0;

    private OnTrainingLisener onTrainingLisener = null;
    private OnOverSpeedLisener onOverSpeedLisener = null;
    private OnOverTimeLisener onOverTimeLisener = null;
    private float mSpeed = 0;

    private int mTempTime = 0;
    private int mTempStep = 0;

    private int mTrainingTime = 0;

    private boolean mCouldTrain = false;

    private String mSpeedFeedback = "";

    public void setCouldTrain(boolean mCouldTrain) {
        this.mCouldTrain = mCouldTrain;
    }

    // private int trainTime = 0;

    public Training(OnTrainingLisener onTrainingLisener) {
        mPersonInfo = PersonInfo.getInstance();
        this.onTrainingLisener = onTrainingLisener;
    }

    private void startTraining() {
        isTraining = true;
        mPersonInfo.getTrainingInfo().setStartDate(new Date());
        mPersonInfo.getTrainingInfo().setTrainTime(0);
        restFlag = 0;
    }

    private void endTraining() {
        if (mTrainingTime >= mPersonInfo.getMaxTrainingSlotTime() * 60 * 0.7 || !mCouldTrain) {
            isTraining = false;
            mPersonInfo.getTrainingInfo().setEndDate(new Date());
            AfterTraining.analyseTrainingInfo(mPersonInfo.getTrainingInfo());
            mPersonInfo.addTimeSlotTraining(mPersonInfo.getTrainingInfo());
            mPersonInfo.getTrainingInfo().clearInfo();
            restFlag = 0;
            AfterTraining.analysePersonInfo(mPersonInfo);
            mTrainingTime = 0;
        }
        // mPersonInfo.getTrainingInfo().setTrainTime(0);
    }

    public void onStep(int step) {
        restFlag = 0;
        int addStep = 0;
        int addTime = 0;
        if (mTempStep != 0) {
            addStep = mTempStep;
            addTime = mTempTime;
            mTempStep = 0;
            mTempTime = 0;
        } else {
            addStep = step - mPersonInfo.getStepCount();
            addTime = (int) perSec;
        }
        mPersonInfo.getTrainingInfo().addStepCount(addStep);
        mPersonInfo.getTrainingInfo().addTrainTime((long) addTime);
        mPersonInfo.getTimeSlotTraining().addStep(addStep);
        mPersonInfo.getTimeSlotTraining().addTime((long) addTime);
        mTrainingTime += addTime;
    }

    public void onRest(int step) {
        // if (restFlag <= 1) {
        // mPersonInfo.getTrainingInfo().addStepCount(step -
        // mPersonInfo.getStepCount());
        // mPersonInfo.getTrainingInfo().addTrainTime((long) perSec);
        // mPersonInfo.getTimeSlotTraining().addStep(step -
        // mPersonInfo.getStepCount());
        // mPersonInfo.getTimeSlotTraining().addTime((long) perSec);
        // }
        restFlag++;
        // 休息时间超过40s判定运动结束
        if (restFlag >= 8) {
            endTraining();
        }
    }

    /**
     * 隔5s查询一次
     * 
     * @param step
     */
    public void trainingLisener(int step) {
        /* 测试信息 */
        String content = "";
        content = " 总步数:" + mPersonInfo.getStepCount() + " 活跃时间" + mPersonInfo.getDayMoveTime() + " 最新分段活跃时间:"
                + mPersonInfo.getTrainingInfo().getTrainTime() + " 平均活跃步频:" + mPersonInfo.getStepFrequency() + " 分段"
                + mPersonInfo.getTimeSlotTraining().getTrainingInfoList().size() + "\n";
        content += "总反馈:" + mPersonInfo.getAnalyseString();
        if (mPersonInfo.getTimeSlotTraining().getTrainingInfoList().size() > 0) {
            content += " 最新反馈:" + mPersonInfo.getTimeSlotTraining().getTrainingInfoList().get(0).getAnalyseString()
                    + "\n";
        }
        content += "检查时间段步数:" + (step - mPersonInfo.getStepCount());
        /* 测试信息end */

        System.out.println("aaaaaaaa" + (step - mPersonInfo.getStepCount()));
        if (checkTraning(step)) {
            if (trainingFlag >= CHECK_TRAINING_TIMES && isTraining == false) {
                startTraining();
            }
            onStep(step);
            content += "计步中";
        }

        mSpeedFeedback = "";
        checkSpeed(step);

        if (trainingFlag <= 0 && isTraining == true) {
            onRest(step);
            content += "休息中";
        }

        /* 测试信息 */
        content += " trainingFlag:" + trainingFlag + " 实时步率" + (step - mPersonInfo.getStepCount()) / perSec + "步/s"
                + " 当前活跃步数:" + mPersonInfo.getTrainingInfo().getStepCount() + " 活跃总步数:"
                + mPersonInfo.getTimeSlotTraining().getStepCount();

        content += "\n" + " 总路程:" + mPersonInfo.getDistance() + " 总卡路里:" + mPersonInfo.getCalorie() + " 总活跃路程:"
                + mPersonInfo.getTimeSlotTraining().getDistance() + " 总活跃卡路里:"
                + mPersonInfo.getTimeSlotTraining().getCalorie() + " 当前活跃路程"
                + mPersonInfo.getTrainingInfo().getDistance() + " 当前活跃卡路里:"
                + mPersonInfo.getTrainingInfo().getCalorie();
        /* 测试信息end */
        mPersonInfo.setStepCount(step);
        if (onTrainingLisener != null) {
            onTrainingLisener.onTraining(isTraining, mSpeed, content);
        }
    }

    public void checkSpeed(int step) {
        mSpeed = (step - mPersonInfo.getStepCount()) / perSec;
        // if (onOverSpeedLisener != null) {
        if (mSpeed > 1.6) {
            mSpeedFeedback = "运动速度偏快，请放慢速度。";
            // onOverSpeedLisener.onOverSpeed(mSpeed,
            // Constraint.OVER_LARGE_LEVEL);
        } else if (mSpeed > 1.4) {
            mSpeedFeedback = "运动速度有点快，请放慢速度。";
            // onOverSpeedLisener.onOverSpeed(mSpeed,
            // Constraint.OVER_MID_LEVEL);
        } else if (mSpeed > 1.3) {
            mSpeedFeedback = "运动速度有点快，请放慢速度。";
            // onOverSpeedLisener.onOverSpeed(mSpeed,
            // Constraint.OVER_LITLE_LEVEL);
        }
        // }
    }

    public String getSpeedFeedback() {
        return mSpeedFeedback;
    }

    public void checkTime() {
        long time = mTrainingTime;
        if (onOverTimeLisener != null) {
            if (time > 30 * 60/* *60 */) {
                onOverTimeLisener.onOverTime(time, Constraint.OVER_LARGE_LEVEL);
            } else if (time > 25 * 60) {
                onOverTimeLisener.onOverTime(time, Constraint.OVER_MID_LEVEL);
            } else if (time > 20 * 60) {
                onOverTimeLisener.onOverTime(time, Constraint.OVER_LITLE_LEVEL);
            }
        }
    }

    /**
     * 判断是否正常运动
     * 
     * @param step
     * @return
     */
    public boolean checkTraning(int step) {
        if (step - mPersonInfo.getStepCount() >= minStep) {
            if (trainingFlag < CHECK_TRAINING_TIMES) {
                mTempStep += step - mPersonInfo.getStepCount();
                mTempTime += perSec;
                trainingFlag++;
            }
        } else {
            mTempStep = 0;
            mTempTime = 0;
            if (trainingFlag > 0) {
                trainingFlag--;
            }
        }
        if (trainingFlag >= CHECK_TRAINING_TIMES || (trainingFlag >= CHECK_TRAINING_TIMES - 1 && isTraining == true)) {
            return true;
        }
        return false;
    }

    public int getTrainingTime() {
        return mTrainingTime;
    }

    public void setOnTrainingLisener(OnTrainingLisener onTrainingLisener) {
        this.onTrainingLisener = onTrainingLisener;
    }

    public void setOnOverSpeedLisener(OnOverSpeedLisener onOverSpeedLisener) {
        this.onOverSpeedLisener = onOverSpeedLisener;
    }

    public void setOnOverTimeLisener(OnOverTimeLisener onOverTimeLisener) {
        this.onOverTimeLisener = onOverTimeLisener;
    }

    public interface OnTrainingLisener {
        public void onTraining(boolean isTraining, float speed, String content);
    }

    public interface OnOverSpeedLisener {
        public void onOverSpeed(float speed, int overLevel);
    }

    public interface OnOverTimeLisener {
        public void onOverTime(long time, int overLevel);
    }

}