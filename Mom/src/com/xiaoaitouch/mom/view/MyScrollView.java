package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

    private float y = 0;
    private OnScrollListener onScrollListener = null;

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO 自动生成的构造函数存根
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO 自动生成的构造函数存根
    }

    public MyScrollView(Context context) {
        super(context, null);
        // TODO 自动生成的构造函数存根
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.draw(canvas);
        if (y != getScrollY()) {
            if (onScrollListener != null) {
                boolean isDown = getScrollY() > y;
                y = getScrollY();
                onScrollListener.onScroll(y, isDown);
                Log.d("scrollTo", "draw:" + y);
            }
        }
        // Log.d("scrollTo", "draw");
    }

    /**
     * 
     * 滚动的回调接口
     * 
     * @author liji
     * 
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * 
         * @param scrollY
         *            、
         */
        public void onScroll(float scrollY, boolean isDown);
    }

}
