package com.xiaoaitouch.mom.train;

import com.xiaoaitouch.mom.train.model.Constraint;
import com.xiaoaitouch.mom.train.model.PersonInfo;
import com.xiaoaitouch.mom.train.model.TrainingInfo;


/**
 * 运动分析
 * 
 * @author LG
 * 
 */
public class AfterTraining {
    // analyse
    public static void analyseTrainingInfo(TrainingInfo trainingInfo) {
        String analyseString = "";
        switch (checkSpeed(trainingInfo.getStepFrequency())) {
        case Constraint.OVER_LARGE_LEVEL:
            analyseString += "运动速度过快" + trainingInfo.getStepFrequency() + " " + trainingInfo.getTrainTime();
            break;

        case Constraint.OVER_MID_LEVEL:
            analyseString += "运动速度过快" + trainingInfo.getStepFrequency() + " " + trainingInfo.getTrainTime();
            break;

        case Constraint.OVER_LITLE_LEVEL:
            analyseString += "运动速度略快" + trainingInfo.getStepFrequency() + " " + trainingInfo.getTrainTime();
            break;

        default:
            break;
        }

        switch (checkTime(trainingInfo.getTrainTime())) {
        case Constraint.OVER_LARGE_LEVEL:
            analyseString += ",运动时间太久";
            break;

        case Constraint.OVER_MID_LEVEL:
            analyseString += ",运动时间很长";
            break;

        case Constraint.OVER_LITLE_LEVEL:
            analyseString += ",运动时间略长";
            break;

        default:
            break;
        }
        if (!analyseString.equals("")) {
            analyseString += "。";
        }
        trainingInfo.setAnalyseString(analyseString);
    }

    public static void analysePersonInfo(PersonInfo personInfo) {
        String analyseString = "";
        switch (checkSpeed(personInfo.getStepFrequency())) {
        case Constraint.OVER_LARGE_LEVEL:
            analyseString += "运动速度过快" + personInfo.getStepFrequency() + " " + personInfo.getDayMoveTime();
            break;

        case Constraint.OVER_MID_LEVEL:
            analyseString += "运动速度过快" + personInfo.getStepFrequency() + " " + personInfo.getDayMoveTime();
            break;

        case Constraint.OVER_LITLE_LEVEL:
            analyseString += "运动速度略快" + personInfo.getStepFrequency() + " " + personInfo.getDayMoveTime();
            break;

        default:
            break;
        }
        switch (checkTime(personInfo)) {
        case Constraint.OVER_LARGE_LEVEL:
            analyseString += ",运动时间太久";
            break;

        case Constraint.OVER_MID_LEVEL:
            analyseString += ",运动时间很长";
            break;

        case Constraint.OVER_LITLE_LEVEL:
            analyseString += ",运动时间略长";
            break;

        default:
            break;
        }

        if (!analyseString.equals("")) {
            analyseString += "。";
        }
        personInfo.setAnalyseString(analyseString);
    }

    public static int checkSpeed(float mSpeed) {
        if (mSpeed > 1.6) {
            return Constraint.OVER_LARGE_LEVEL;
        } else if (mSpeed > 1.4) {
            return Constraint.OVER_MID_LEVEL;
        } else if (mSpeed > 1.2) {
            return Constraint.OVER_LITLE_LEVEL;
        }
        return 0;
    }

    public static int checkTime(long time) {
        if (time > 50 * 60) {
            return Constraint.OVER_LARGE_LEVEL;
        } else if (time > 40 * 60) {
            return Constraint.OVER_MID_LEVEL;
        } else if (time > 30 * 60) {
            return Constraint.OVER_LITLE_LEVEL;
        }
        return 0;
    }

    public static int checkTime(PersonInfo personInfo) {
        long time = personInfo.getDayMoveTime();
        if (time > 120 * 60) {
            return Constraint.OVER_LARGE_LEVEL;
        } else if (time > 110 * 60) {
            return Constraint.OVER_MID_LEVEL;
        } else if (time > 100 * 60) {
            return Constraint.OVER_LITLE_LEVEL;
        }
        return 0;
    }
}
