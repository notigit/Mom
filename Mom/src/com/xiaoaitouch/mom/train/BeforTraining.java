package com.xiaoaitouch.mom.train;

public class BeforTraining {
    private ExternalFactor mExternalFactor;
    private InternalFactor mInternalFactor;
    private String mFeedback;

    public BeforTraining() {
        init();
    }

    public ExternalFactor getExternalFactor() {
        return mExternalFactor;
    }

    public InternalFactor getInternalFactor() {
        return mInternalFactor;
    }

    private void init() {
        mExternalFactor = new ExternalFactor();
        mInternalFactor = new InternalFactor();
        mInternalFactor.getPersonInfo().setAge(20);
        mInternalFactor.getPersonInfo().setCalorie(0);
        // mInternalFactor.getPersonInfo().setDayMoveTime(45);
        mInternalFactor.getPersonInfo().setHeight(167);
        mInternalFactor.getPersonInfo().setStepCount(0);
        mInternalFactor.getPersonInfo().setWeight(55);
    }

    public boolean couldTraining() {
        System.out.println("kkkkkkkkkkkkkk--------------->>>>>>>>>>>" + mExternalFactor.getK());
        boolean isExternal = false;
        boolean isInternal = false;
        mExternalFactor.computeK();
        if (mExternalFactor.getK() >= 4) {
            isExternal = true;
        }

        if (mInternalFactor.isInternalFactor()) {
            isInternal = true;
        }

        if (isExternal && isInternal) {
            setFeedback("天气不错，去出门散个步吧。" + mInternalFactor.getPersonInfo().getMaxTrainingSlotTime() / 60 + "分钟。");
            return true;
        }
        setFeedback("不建议出行。");
        return false;
    }

    private void setFeedback(String mFeedback) {
        this.mFeedback = mFeedback;
    }

    public String getFeedback() {
        return this.mFeedback;
    }

    // public boolean couldTraining() {
    // boolean could = false;
    // if (mExternalFactor.isExternalFactorCouldTrain() &&
    // mInternalFactor.isInternalFactor()) {
    // could = true;
    // }
    // return could;
    // }

}
