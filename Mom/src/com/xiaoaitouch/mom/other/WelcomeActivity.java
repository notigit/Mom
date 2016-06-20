package com.xiaoaitouch.mom.other;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.WelcomeAdapter;
import com.xiaoaitouch.mom.mine.LoginActivity;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.view.DrawOvalView;
import com.xiaoaitouch.mom.view.ScrollerViewPager;
import com.xiaoaitouch.mom.view.SpringIndicator;

/**
 * 欢迎页
 * 
 * @author huxin
 * 
 */
public class WelcomeActivity extends Activity {
    @Bind(R.id.view_pager)
    ScrollerViewPager mViewPager;
    @Bind(R.id.indicator)
    SpringIndicator mSpringIndicator;

    private List<Integer> listViews;
    // private List<View> listViews;
    private View viewOne, viewTwo, viewThree, viewFour, viewFive, viewSex;
    private WelcomeAdapter mWelcomeAdapter;
    protected Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.new_welcome_activity);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        listViews = getViewList();
        // mWelcomeAdapter = new WelcomeAdapter(listViews);
        mWelcomeAdapter = new WelcomeAdapter(listViews, this);
        mViewPager.setAdapter(mWelcomeAdapter);
        mViewPager.fixScrollSpeed();
        mSpringIndicator.setViewPager(mViewPager);
    }

    private List<Integer> getViewList() {
        List<Integer> mViewList = new ArrayList<Integer>();
        // LayoutInflater lf = getLayoutInflater();
        mViewList.add(R.layout.welcome_one_item);
        mViewList.add(R.layout.welcome_two_item);
        mViewList.add(R.layout.welcome_three_item);
        mViewList.add(R.layout.welcome_four_item);
        mViewList.add(R.layout.welcome_five_item);
        mViewList.add(R.layout.welcome_sex_item);
        return mViewList;
    }

    @OnClick(R.id.new_ma_tv)
    public void openGuideActivity() {
        startIntent(GuideActivity.class);
    }

    @OnClick(R.id.login_tv)
    public void openLoginActivity() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.putExtra("isflage", true);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void startIntent(Class<?> activity) {
        Intent intent = new Intent(mActivity, activity);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

}
