package com.xiaoaitouch.mom.train.model;

import java.util.ArrayList;
import java.util.List;

import android.text.Html.TagHandler;
import android.util.Log;

/**
 * 孕期症状
 * 
 * @author LG
 * 
 */
public class Symptom {
    private List<String> mSymptomList;
    private final String TAG = "Symptom";
    private String mFeedback = "";
    private static final String HAS_SYMPTOM = "您目前身体不适，不适合运动，请及时向医师咨询。";
    private static final String HAS_NO_SYMPTOM = "身体无不适症状。";

    public Symptom() {
        init();
    }

    private void init() {
        mSymptomList = new ArrayList<String>();
    }

    public void addSymptom(String symptom) {
        mSymptomList.add(symptom);
        setFeedback(HAS_SYMPTOM);
    }

    public boolean removeSymptom(String symptom) {
        if (null == symptom) {
            Log.e(TAG, "removeSymptom()---->symptom is null");
            return false;
        }
        return this.removeSymptom(mSymptomList.indexOf(symptom));
    }

    public boolean removeSymptom(int index) {
        if (index < 0 || index >= mSymptomList.size()) {
            return false;
        }
        mSymptomList.remove(index);
        if (getSymptomCount() == 0) {
            setFeedback(HAS_NO_SYMPTOM);
        }
        return true;
    }

    public void clearSymptom() {
        mSymptomList.clear();
    }

    public int getSymptomCount() {
        return mSymptomList.size();
    }

    public String getFeedback() {
        return this.mFeedback;
    }

    public void setFeedback(String feedback) {
        this.mFeedback = feedback;
    }

}
