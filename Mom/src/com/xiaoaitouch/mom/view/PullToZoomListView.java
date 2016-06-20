package com.xiaoaitouch.mom.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.util.Utils;

public class PullToZoomListView extends ListView implements AbsListView.OnScrollListener {
    private static final int INVALID_VALUE = -1;
    private static final String TAG = "PullToZoomListView";
    private float maxScale = 1;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat + 1.0F * mInterpolatorFlag;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };
    int mActivePointerId = -1;
    private static int mInterpolatorFlag = -1;
    private FrameLayout mHeaderContainer;
    private int mHeaderHeight = 0;

    public int getmHeaderHeight() {
        return mHeaderHeight;
    }

    /**
     * 设置头部的高度
     * 
     * @param mHeaderHeight
     */
    public void setmHeaderHeight(int mHeaderHeight) {
        this.mHeaderHeight = mHeaderHeight;
        ViewGroup.LayoutParams lp2 = new LinearLayout.LayoutParams(Utils.dip2px(mContext,
                AbsListView.LayoutParams.FILL_PARENT), mHeaderHeight);
        PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).setLayoutParams(lp2);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(Utils.dip2px(mContext,
                AbsListView.LayoutParams.FILL_PARENT), AbsListView.LayoutParams.WRAP_CONTENT);
        getHeaderContainer().setLayoutParams(lp);
        maxScale = mScreenHeight * 1.0f / mHeaderHeight - 0.2f;
    }

    boolean isInit = true;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private ImageView mHeaderImage;
    float mLastMotionY = -1.0F;
    float mLastScale = -1.0F;
    float mMaxScale = -1.0F;
    private AbsListView.OnScrollListener mOnScrollListener;
    private ScalingRunnalable mScalingRunnalable;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    private ImageView mShadow;

    // 自定义的headerview
    private View headerView;

    private Context mContext;

    public PullToZoomListView(Context paramContext) {
        super(paramContext);
        init(paramContext);
        mContext = paramContext;
    }

    public PullToZoomListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init(paramContext);
        mContext = paramContext;
    }

    public PullToZoomListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init(paramContext);
        mContext = paramContext;
    }

    private void endScraling(boolean isUp) {
        if (this.mHeaderContainer.getBottom() >= this.mHeaderHeight)
            Log.d("mmm", "endScraling");
        if (isUp) {
            mInterpolatorFlag = -1;
        } else {
            mInterpolatorFlag = 1;
        }
        this.mScalingRunnalable.setIsUp(isUp);
        this.mScalingRunnalable.startAnimation(200L);
    }

    private void init(Context paramContext) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity) paramContext).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        this.mScreenHeight = localDisplayMetrics.heightPixels;
        this.mScreenWidth = localDisplayMetrics.widthPixels;
        this.mHeaderContainer = new FrameLayout(paramContext);
        this.mHeaderContainer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));
        // this.mHeaderImage = new ImageView(paramContext);
        int i = localDisplayMetrics.widthPixels;
        setHeaderViewSize(i, (int) (9.0F * (i / 16.0F)));
        this.mShadow = new ImageView(paramContext);
        FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -2);
        localLayoutParams.gravity = Gravity.CENTER;
        // this.mShadow.setLayoutParams(localLayoutParams);
        // this.mHeaderContainer.addView(this.mHeaderImage);
        // this.mHeaderContainer.addView(this.mShadow);

        // addHeaderView(this.mHeaderContainer);

        this.mScalingRunnalable = new ScalingRunnalable();
        
        initAnimation();
        super.setOnScrollListener(this);
    }

    private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
        int i = (paramMotionEvent.getAction()) >> 8;
        if (paramMotionEvent.getPointerId(i) == this.mActivePointerId)
            if (i != 0) {
                int j = 1;
                this.mLastMotionY = paramMotionEvent.getY(0);
                this.mActivePointerId = paramMotionEvent.getPointerId(0);
                return;
            }
    }

    private void reset() {
        this.mActivePointerId = -1;
        this.mLastMotionY = -1.0F;
        this.mMaxScale = -1.0F;
        this.mLastScale = -1.0F;
    }

    public ImageView getHeaderView() {
        return this.mHeaderImage;
    }

    public FrameLayout getHeaderContainer() {
        return mHeaderContainer;
    }

    public void setHeaderView() {
        addHeaderView(this.mHeaderContainer);
    }

    /*
     * public boolean onInterceptTouchEvent(MotionEvent ev) {
     * 
     * final int action = ev.getAction() & MotionEvent.ACTION_MASK; float
     * mInitialMotionX= 0; float mLastMotionX= 0;
     * 
     * float mInitialMotionY= 0; float mLastMotionY = 0;
     * 
     * boolean isIntercept=false; switch (action) { case
     * MotionEvent.ACTION_DOWN:
     * 
     * mLastMotionY=ev.getY(); break;
     * 
     * case MotionEvent.ACTION_MOVE: mInitialMotionY = ev.getY();
     * 
     * if(Math.abs(mInitialMotionY-mLastMotionY)>50) { isIntercept=true; }
     * break;
     * 
     * }
     * 
     * return isIntercept; }
     */

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        if (this.mHeaderHeight == 0 && this.mHeaderContainer.findViewById(R.id.main_top_up) != null)
            this.mHeaderHeight = this.mHeaderContainer.findViewById(R.id.main_top_up).getHeight();
    }

    @Override
    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {
        // Log.d("mmm", "onScroll");
        // float f = this.mHeaderHeight - this.mHeaderContainer.getBottom();
        // Log.d("mmm", "f|" + f);
        // if ((f > 0.0F) && (f < this.mHeaderHeight)) {
        // Log.d("mmm", "1");
        // // int i = (int) (0.65D * f);
        // // this.mHeaderImage.scrollTo(0, -i);
        // } else if (this.mHeaderImage.getScrollY() != 0) {
        // Log.d("mmm", "2");
        // this.mHeaderImage.scrollTo(0, 0);
        // }
        // mTopHorizontalView =
        // mHeadView.findViewById(R.id.main_top_horizontal_include);
        // mTopVerticalView =
        // mHeadView.findViewById(R.id.main_top_vertical_include);
        if (this.mHeaderContainer.findViewById(R.id.main_top_up) != null) {
            float f = (this.mHeaderContainer.findViewById(R.id.main_top_up).getBottom() * 1.0f / this.mHeaderHeight - 1)
                    / (maxScale - 1);
            mHeaderContainer.findViewById(R.id.main_top_horizontal_include).findViewById(R.id.main_top_h_text)
                    .setAlpha(1 - f);
            mHeaderContainer
                    .findViewById(R.id.main_top_horizontal_include)
                    .findViewById(R.id.main_baby_image)
                    .setX((float) (mScreenWidth
                            * 0.5f
                            - mHeaderContainer.findViewById(R.id.main_top_horizontal_include)
                                    .findViewById(R.id.main_baby_image).getWidth() / 2.0f + mScreenWidth * 0.5f
                            * (1 - f)));
            mHeaderContainer.findViewById(R.id.main_top_vertical_include).setAlpha(f);
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(paramAbsListView, paramInt1, paramInt2, paramInt3);
        }
        
        
        //CHEN 
        startfirstItemIndex = paramInt1;
		startlastItemIndex = paramInt1 + paramInt2 - 1;
		// 判断向下或者向上滑动了
		if ((endfirstItemIndex > startfirstItemIndex) && (endfirstItemIndex > 0)) {
			RunThread(UPREFRESH);
		} else if ((endlastItemIndex < startlastItemIndex) && (endlastItemIndex > 0)) {
			RunThread(DOWNREFRESH);
		}
		endfirstItemIndex = startfirstItemIndex;
		endlastItemIndex = startlastItemIndex;
    }

    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
        if (this.mOnScrollListener != null)
            this.mOnScrollListener.onScrollStateChanged(paramAbsListView, paramInt);
    }

    private void breakTouch() {
        endScraling(false);
    }

    private boolean scrollControl = true;
    private boolean isOpen = false;
    private boolean upFlag = false;

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        Log.d("mmm", "" + (0xFF & paramMotionEvent.getAction()));
        switch (0xFF & paramMotionEvent.getAction()) {
        case MotionEvent.ACTION_OUTSIDE:
        case MotionEvent.ACTION_DOWN:
            if (!this.mScalingRunnalable.mIsFinished) {
                this.mScalingRunnalable.abortAnimation();
            }
            this.mLastMotionY = paramMotionEvent.getY();
            this.mActivePointerId = paramMotionEvent.getPointerId(0);
            this.mMaxScale = (this.mScreenHeight / this.mHeaderHeight);
            this.mLastScale = (PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).getBottom() / this.mHeaderHeight);
            break;
        case MotionEvent.ACTION_MOVE:
            if (!scrollControl) {
                return true;
            }
            Log.d("mmm", "mActivePointerId" + mActivePointerId);
            int j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
            if (j == -1) {
                Log.e("PullToZoomListView", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
            } else {
                if (this.mLastMotionY == -1.0F)
                    this.mLastMotionY = paramMotionEvent.getY(j);
                if (PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).getBottom() >= this.mHeaderHeight) {
                    Log.d("mmm", "11111111111" + this.getScrollY());
                    if (isOpen) {
                        if (paramMotionEvent.getY(j) - this.mLastMotionY < 0) {
                            upFlag = false;
                            isOpen = false;
                            scrollControl = false;
                            endScraling(true);
                        }
                        return true;
                    }

                    ViewGroup.LayoutParams localLayoutParams = PullToZoomListView.this.mHeaderContainer.findViewById(
                            R.id.main_top_up).getLayoutParams();
                    float f = ((paramMotionEvent.getY(j) - this.mLastMotionY
                            + PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).getBottom() + PullToZoomListView.this.mHeaderContainer
                                .getTop()) / this.mHeaderHeight - this.mLastScale)
                            / 2.0F + this.mLastScale;
                    if (!isOpen) {
                        if ((this.mLastScale <= 1.0D) && (f < this.mLastScale) || this.getScrollY() != 0) {
                            localLayoutParams.height = this.mHeaderHeight;
                            PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).setLayoutParams(
                                    localLayoutParams);
                            return super.onTouchEvent(paramMotionEvent);
                        }
                    }

                    Log.d("mmm", "3333333333 this.getScrollY() + " + this.getScrollY() + " " + f + " "
                            + PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).getBottom()
                            + " mHeaderHeight+" + mHeaderHeight);
                    this.mLastScale = Math.min(Math.max(f, 1.0F), this.mMaxScale);
                    localLayoutParams.height = ((int) (this.mHeaderHeight * this.mLastScale));
                    if (localLayoutParams.height < this.mHeaderHeight * 1.5f /*
                                                                              * this
                                                                              * .
                                                                              * mScreenHeight
                                                                              * *
                                                                              * 2.0f
                                                                              * /
                                                                              * 5
                                                                              */) {
                        Log.d("mmm", "4444444");
                    } else {
                        if (!isOpen) {
                            isOpen = true;
                            endScraling(false);
                            scrollControl = false;
                            return true;
                        }
                    }
                    Log.d("mmm", "555555555");
                    PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).setLayoutParams(
                            localLayoutParams);
                    this.mLastMotionY = paramMotionEvent.getY(j);
                    return true;
                }
                this.mLastMotionY = paramMotionEvent.getY(j);
            }
            break;
        case MotionEvent.ACTION_UP:
            scrollControl = true;
            reset();
            if (!isOpen) {
                endScraling(true);
            }
            break;
        case MotionEvent.ACTION_CANCEL:
            int i = paramMotionEvent.getActionIndex();
            this.mLastMotionY = paramMotionEvent.getY(i);
            this.mActivePointerId = paramMotionEvent.getPointerId(i);
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            onSecondaryPointerUp(paramMotionEvent);

            try {
                this.mLastMotionY = paramMotionEvent.getY(paramMotionEvent.findPointerIndex(this.mActivePointerId));
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    public void setHeaderViewSize(int paramInt1, int paramInt2) {
        Object localObject = this.mHeaderContainer.getLayoutParams();
        if (localObject == null)
            localObject = new AbsListView.LayoutParams(paramInt1, AbsListView.LayoutParams.WRAP_CONTENT);
        ((ViewGroup.LayoutParams) localObject).width = paramInt1;
        ((ViewGroup.LayoutParams) localObject).height = paramInt2;
        this.mHeaderContainer.setLayoutParams((ViewGroup.LayoutParams) localObject);
        this.mHeaderHeight = paramInt2;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener paramOnScrollListener) {
        this.mOnScrollListener = paramOnScrollListener;
    }

    public void setShadow(int paramInt) {
        this.mShadow.setBackgroundResource(paramInt);
    }

    class ScalingRunnalable implements Runnable {
        long mDuration;
        boolean mIsFinished = true;
        float mScale;
        long mStartTime;
        boolean mIsUp = false;

        ScalingRunnalable() {
        }

        public void abortAnimation() {
            this.mIsFinished = true;
        }

        public boolean isFinished() {
            return this.mIsFinished;
        }

        public void onDown() {
            float f2;
            ViewGroup.LayoutParams localLayoutParams;
            if ((!this.mIsFinished) && (this.mScale < maxScale)) {
                float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) this.mStartTime)
                        / (float) this.mDuration;
                f2 = this.mScale + (this.mScale - 1.0F) * PullToZoomListView.sInterpolator.getInterpolation(f1) * 0.5f;
                localLayoutParams = PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up)
                        .getLayoutParams();
                System.out.println("+++++++++++++++++++++++" + f2 + " " + this.mScale + " " + maxScale);
                ;
                if (f2 < maxScale) {
                    localLayoutParams.height = PullToZoomListView.this.mHeaderHeight;
                    ;
                    localLayoutParams.height = ((int) (f2 * PullToZoomListView.this.mHeaderHeight));
                    PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).setLayoutParams(
                            localLayoutParams);
                    PullToZoomListView.this.post(this);
                    return;
                } else {
                    f2 = maxScale;
                    localLayoutParams.height = PullToZoomListView.this.mHeaderHeight;
                    localLayoutParams.height = ((int) (f2 * PullToZoomListView.this.mHeaderHeight));
                    PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).setLayoutParams(
                            localLayoutParams);
                    PullToZoomListView.this.post(this);
                }
                this.mIsFinished = true;
                scrollControl = true;
            }
        }

        public void onUp() {
            float f2;
            ViewGroup.LayoutParams localLayoutParams;
            if ((!this.mIsFinished) && (this.mScale > 1.0D)) {
                float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) this.mStartTime)
                        / (float) this.mDuration;
                f2 = this.mScale - (this.mScale - 1.0F) * PullToZoomListView.sInterpolator.getInterpolation(f1);
                localLayoutParams = PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up)
                        .getLayoutParams();
                if (f2 > 1.0F) {
                    Log.d("mmm", "f2>1.0");
                    localLayoutParams.height = PullToZoomListView.this.mHeaderHeight;
                    ;
                    localLayoutParams.height = ((int) (f2 * PullToZoomListView.this.mHeaderHeight));
                    PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).setLayoutParams(
                            localLayoutParams);
                    PullToZoomListView.this.post(this);
                    return;
                } else {
                    f2 = 1;
                    Log.d("mmm", "f2>1.0");
                    localLayoutParams.height = PullToZoomListView.this.mHeaderHeight;
                    ;
                    localLayoutParams.height = ((int) (f2 * PullToZoomListView.this.mHeaderHeight));
                    PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).setLayoutParams(
                            localLayoutParams);
                    PullToZoomListView.this.post(this);
                }
                this.mIsFinished = true;
                scrollControl = true;
                reset();
            }
        }

        public void setIsUp(boolean isUp) {
            mIsUp = isUp;
        }

        public void run() {
            if (mIsUp) {
                onUp();
            } else {
                onDown();
            }
        }

        public void startAnimation(long paramLong) {
            this.mStartTime = SystemClock.currentThreadTimeMillis();
            this.mDuration = paramLong;
            this.mScale = ((float) (PullToZoomListView.this.mHeaderContainer.findViewById(R.id.main_top_up).getBottom()) / PullToZoomListView.this.mHeaderHeight);
            this.mIsFinished = false;
            PullToZoomListView.this.post(this);
        }
    }

    
    
    /**
	 * 下滑刷新
	 */
	private final int DOWNREFRESH = 1;
	/**
	 * 上滑刷新
	 */
	private final int UPREFRESH = 0;
	/**
	 * 初始状态下第一个 条目
	 */
	private int startfirstItemIndex;
	/**
	 * 初始状态下最后一个 条目
	 */
	private int startlastItemIndex;
	/**
	 * 滑动后的第一个条目
	 */
	private int endfirstItemIndex;
	/**
	 * 滑动后的最后一个条目
	 */
	private int endlastItemIndex;
	private View view;
	private Animation animation;
	private Handler handler;
	private Runnable run;
	private Message message;
	
	private void RunThread(final int state) {
		// TODO Auto-generated method stub
		run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				message = handler.obtainMessage(1, state);
				handler.sendMessage(message);
			}
		};
		run.run();
	}

	private void initAnimation(){
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int result = (Integer) msg.obj;
				switch (result) {
					case DOWNREFRESH: {
						//获得最后一个item
						view = getChildAt(getChildCount() - 1);
						
						if (null != view) {
							//加载动画
							animation = AnimationUtils.loadAnimation(mContext, R.anim.translate);
							view.startAnimation(animation);
						}
						break;
					}
					case UPREFRESH: {
						//获得第一个item
						view = getChildAt(0);
						
//						if (null != view) {
//							//加载动画
//							animation = AnimationUtils.loadAnimation(mContext, R.anim.translate);
//							view.startAnimation(animation);
//						}
						break;
					}
					default:
						break;
				}
//				if (null != view) {
//					//加载动画
//					animation = AnimationUtils.loadAnimation(mContext, R.anim.translate);
//					view.startAnimation(animation);
//				}
			}
		};
	}
	
}
