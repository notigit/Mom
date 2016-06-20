package com.xiaoaitouch.mom.train.model;

public class Weather extends BaseFeedbackModel {

    /**
     * 建议出行天气
     */
    public static final String WEATHER_SUNNY = "00";
    public static final String WEATHER_CLOUDY = "01";
    public static final String WEATHER_OVERCAST = "02";

    private String mFromWeather;
    private String mToWeather;
    private String mWeather;

    public String getFromWeather() {
        return mFromWeather;
    }

    public void setFromWeather(String mFromWeather) {
        this.mFromWeather = mFromWeather;
    }

    public String getToWeather() {
        return mToWeather;
    }

    public void setToWeather(String mToWeather) {
        this.mToWeather = mToWeather;
    }

    public String getWeather() {
        return mWeather;
    }

    public void setWeather(String mWeather) {
        this.mWeather = mWeather;
        selectK();
    }

    private void selectK() {
        if (mWeather.equals(WEATHER_SUNNY)) {
            setK(2);
            setFeedback("晴天");
            return;
        }
        if (mWeather.equals(WEATHER_CLOUDY)) {
            setK(2);
            setFeedback("多云，天气不错");
            return;
        }
        if (mWeather.equals(WEATHER_OVERCAST)) {
            setK(1);
            setFeedback("阴天，小心有雨");
            return;
        }
        setK(0);
        setFeedback("天气不好，谨慎出行" + mWeather);
    }

}
