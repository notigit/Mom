package com.xiaoaitouch.mom.train;

import java.util.Date;

import android.util.Log;

import com.xiaoaitouch.mom.train.model.AirQuality;
import com.xiaoaitouch.mom.train.model.NowTime;
import com.xiaoaitouch.mom.train.model.Temperature;
import com.xiaoaitouch.mom.train.model.Weather;
import com.xiaoaitouch.mom.train.model.Wind;

/**
 * 运动外部影响因素
 * 
 * @author LG
 * 
 *         2015/8/28
 * 
 */
public class ExternalFactor {
    private int k;
    private Wind mWind;
    private Weather mWeather;
    private AirQuality mAirQuality;
    private Temperature mTemperature;
    private NowTime mNowTime;

    public ExternalFactor() {
        mWeather = new Weather();
        mWind = new Wind();
        mAirQuality = new AirQuality();
        mTemperature = new Temperature();
        mNowTime = new NowTime();
        init();
    }

    private void init() {
        mWeather.setWeather("00");
        mWind.setLevel(1);
        mAirQuality.setAqi(80);
        mTemperature.setMinTemperature(16);
        mNowTime.setNowTime(new Date());
        computeK();
    }

    public int computeK() {
        k = mWeather.getK() * mWind.getK() * mAirQuality.getK() * mTemperature.getK() * mNowTime.getK();
        Log.i("kkkkkkkkkkkkkkkkkkk", "kkkkkkkkkkk kkkkkkk kkkkkk kkk- - - -- -- -- --  - > . . . .." + mWeather.getK()
                + " " + mWind.getK() + " " + mAirQuality.getK() + " " + mTemperature.getK() + " " + mNowTime.getK()
                + " ");
        return k;
    }

    public int getK() {
        return k;
    }

    public Wind getWind() {
        return mWind;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public AirQuality getAirQuality() {
        return mAirQuality;
    }

    public Temperature getTemperature() {
        return mTemperature;
    }

    public NowTime getNowTime() {
        return mNowTime;
    }

}
// public class ExternalFactor {
// private AirQuality mAirQuality;
// private Weather mWeather;
//
// /**
// * ��ǰʱ�䣬�ж��Ƿ����ʺ��˶���ʱ��
// */
// private long nowTime;
//
// public ExternalFactor() {
// init();
// }
//
// private void init() {
// mAirQuality = new AirQuality();
// mWeather = new Weather();
// }
//
// private boolean isAirQualityCouldTrain() {
// boolean could = false;
// return could;
// }
//
// private boolean isWeatherCouldTrain() {
// boolean could = false;
// return could;
// }
//
// /**
// * ��ǰʱ����Ƿ������˶�
// *
// * @return
// */
// private boolean isTimeCouldTrain() {
// return isTimeCouldTrain(nowTime);
// }
//
// private boolean isTimeCouldTrain(long time) {
// boolean could = false;
// return could;
// }
//
// public boolean isExternalFactorCouldTrain() {
// boolean could = false;
// if (isAirQualityCouldTrain() && isWeatherCouldTrain() &&
// isTimeCouldTrain()) {
// could = true;
// }
// return could;
// }
//
// public AirQuality getmAirQuality() {
// return mAirQuality;
// }
//
// public void setmAirQuality(AirQuality mAirQuality) {
// this.mAirQuality = mAirQuality;
// }
//
// public Weather getmWeather() {
// return mWeather;
// }
//
// public void setmWeather(Weather mWeather) {
// this.mWeather = mWeather;
// }
//
// public long getNowTime() {
// return nowTime;
// }
//
// public void setNowTime(long nowTime) {
// this.nowTime = nowTime;
// }

// }
