package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class CusViewPager extends ViewPager {

	private int mTouchSlop;

	private float mLastMotionX;

	private float mLastMotionY;

	private boolean isMoveToDetails = true;

	public CusViewPager(Context context, AttributeSet attrs) {

		super(context, attrs);

		final ViewConfiguration configuration = ViewConfiguration

		.get(getContext());

		mTouchSlop = configuration.getScaledTouchSlop();

	}

	public CusViewPager(Context context) {

		this(context, null);

	}

	public void setMoveToDetails(boolean isMoveToDetails) {

		this.isMoveToDetails = isMoveToDetails;

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		try {

			final float x = ev.getX(0);

			final float y = ev.getY(0);

			switch (ev.getAction()) {

			case MotionEvent.ACTION_DOWN:

				mLastMotionX = x;

				mLastMotionY = y;

				break;

			case MotionEvent.ACTION_MOVE:

				int xDiff = (int) Math.abs(x - mLastMotionX);

				int yDiff = (int) Math.abs(y - mLastMotionY);

				final int x_yDiff = xDiff * xDiff + yDiff * yDiff;

				boolean xMoved = x_yDiff > mTouchSlop * mTouchSlop;

				if (xMoved) {

					if (xDiff > yDiff) {

						return true;

					} else {

						return false;

					}

				}

				break;

			default:

				break;

			}

			return super.onInterceptTouchEvent(ev);

		} catch (IllegalArgumentException ex) {

		}

		return false;

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (!isMoveToDetails)

			return false;

		try {

			return super.onTouchEvent(ev);

		} catch (IllegalArgumentException ex) {

		}

		return false;

	}

}