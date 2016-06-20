package com.xiaoaitouch.mom.train.model;


import android.inputmethodservice.Keyboard.Key;

public class Temperature extends BaseFeedbackModel {
    private int mMaxTemperature;
    private int mMinTemperature;
    private int mNowTemperature;

    public int getMaxTemperature() {
        return mMaxTemperature;
    }

    public void setMaxTemperature(int mMaxTemperature) {
        this.mMaxTemperature = mMaxTemperature;
    }

    public int getMinTemperature() {
        return mMinTemperature;
    }

    public void setMinTemperature(int mMinTemperature) {
        this.mMinTemperature = mMinTemperature;
        seleteK();
    }

    public int getNowTemperature() {
        return mNowTemperature;
    }

    public void setNowTemperature(int mNowTemperature) {
        this.mNowTemperature = mNowTemperature;
    }

    private void seleteK() {
        if (mMinTemperature <= -4) {
            setK(0);
            setFeedback("气温过冷");
        }
        if (mMinTemperature >= 27) {
            setK(0);
            setFeedback("气温过热");
        }
        if (mMinTemperature > -4 && mMinTemperature <= 5) {
            setK(1);
            setFeedback("气温偏凉，注意保暖");
        }
        if (mMinTemperature < 27 && mMinTemperature >= 24) {
            setK(1);
            setFeedback("气温偏热，注意补水");
        }
        if (mMinTemperature < 24 && mMinTemperature > 5) {
            setK(2);
            setFeedback("气温适合");
        }
    }

    // private void seleteFeedback() {
    // switch (getK()) {
    // case 0:
    // setFeedback("")
    // break;
    // case 1:
    //
    // break;
    // case 2:
    //
    // break;
    //
    // default:
    // break;
    // }
    // }
}
