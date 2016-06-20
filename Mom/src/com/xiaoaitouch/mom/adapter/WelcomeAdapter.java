package com.xiaoaitouch.mom.adapter;

import java.util.List;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.view.RoundedImageViews;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeAdapter extends PagerAdapter implements View.OnTouchListener {

    private List<Integer> mViewList;
    private LayoutInflater mInflater;

    public WelcomeAdapter(List<Integer> list, Context context) {
        this.mViewList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(android.view.ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        // ((RoundedImageViews) ((View)
        // object).findViewById(R.id.image)).deleteBitmap();
        // container.removeViewAt(position);
    };

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(mViewList.get(position), null);
        container.addView(view, 0);
        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}