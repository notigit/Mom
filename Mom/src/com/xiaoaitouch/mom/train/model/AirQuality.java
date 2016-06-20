package com.xiaoaitouch.mom.train.model;


public class AirQuality extends BaseFeedbackModel {

    private int mAqi;
    private int mPm25;

    public static final int A_QUALITY = 50;
    public static final int B_QUALITY = 100;

    public AirQuality() {
        mAqi = 0;// 读缓存 缓存没有写0
        mPm25 = 0;// 读缓存 缓存没有写0
    }

    public int getAqi() {
        return mAqi;
    }

    public void setAqi(int mAqi) {
        this.mAqi = mAqi;
        selectK();
    }

    public int getPm25() {
        return mPm25;
    }

    public void setPm25(int mPm25) {
        this.mPm25 = mPm25;
    }

    private void selectK() {
        if (mAqi > 0 && mAqi < A_QUALITY) {
            setK(2);
            setFeedback("优");
        }
        if (mAqi >= A_QUALITY && mAqi < B_QUALITY) {
            setK(1);
            setFeedback("良");
        }
        if (mAqi > B_QUALITY) {
            setK(0);
            setFeedback("空气污染");
        }
    }
}
