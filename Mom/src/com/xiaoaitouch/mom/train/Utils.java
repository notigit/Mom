package com.xiaoaitouch.mom.train;

import com.xiaoaitouch.mom.train.model.PersonInfo;

public class Utils {
    private final static float METRIC_WALKING_FACTOR = 0.708f;

    public static float comCalorie(int step) {
        float calories = 0;

        calories = (PersonInfo.getInstance().getWeight() * METRIC_WALKING_FACTOR)
        // Distance:
                * PersonInfo.getInstance().getHeight() * 0.413f// mStepLength(centimeters)
                / 100000.0f * step; // centimeters/kilometer
        return calories;
    }

    /**
     * 返回路程 (km)
     * 
     * @param step
     * @return
     */
    public static float comDistance(int step) {
        float distance = 0;
        distance = PersonInfo.getInstance().getHeight() * 0.413f// mStepLength(centimeters)
                / 100000.0f * step; // centimeters/kilometer
        return distance;
    }
}
