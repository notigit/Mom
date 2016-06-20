package com.xiaoaitouch.mom.train.model;

public class Wind extends BaseFeedbackModel {
    private int mLevel;

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
        selectK();
        selectFeedback();
    }

    private void selectK() {
        switch (getLevel()) {
        case 0:
        case 1:
        case 2:
            setK(2);
            break;
        case 3:
        case 4:
            setK(1);
            break;
        default:
            setK(0);
            break;
        }
    }

    private void selectFeedback() {
        switch (getLevel()) {
        case 0:
        case 1:
        case 2:
            setFeedback(getLevel() + "级" + " 风和");
            break;
        case 3:
        case 4:
            setFeedback(getLevel() + "级" + "有些风大，注意防风");
            break;
        default:
            setFeedback(getLevel() + "级" + "风太大了，不建议外出.");
            break;
        }
    }

}
