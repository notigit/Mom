package com.xiaoaitouch.mom.wheelview;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.sqlite.BeaconTables;

/**
 * 滚动选择器
 * 
 * 
 */
public class PickerView extends View {

	public static final String TAG = "PickerView";

	/**
	 * 每项view 最小缩放比例
	 */
	public static final float SCALE_MIN = 0.45f;
	/**
	 * text之间间距和minTextSize之比
	 */
	public static final float MARGIN_ALPHA = 2.4f;
	/**
	 * 自动回滚到中间的速度
	 */
	public static final float SPEED = 3;

	// private List<String> mDataList;
	/**
	 * 选中的位置，这个位置是mDataList的中心位置，一直不变
	 */
	private int mCurrentSelected;
	private Paint mPaint;

	private float mMaxTextSize = 30;
	private float mMinTextSize = 15;

	private float mMaxTextAlpha = 255;
	private float mMinTextAlpha = 100;

	private int mViewHeight;
	private int mViewWidth;

	private float mLastDownY;
	/**
	 * 滑动的距离
	 */
	private float mMoveLen = 0;
	private boolean isInit = false;
	private onSelectListener mSelectListener;
	private Timer timer;
	private MyTimerTask mTask;

	private ArrayList<BeaconInfo> arrayList;

	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (Math.abs(mMoveLen) < SPEED) {
				mMoveLen = 0;
				if (mTask != null) {
					mTask.cancel();
					mTask = null;
					performSelect();
				}
			} else
				// 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
				mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
			invalidate();
		}

	};

	public PickerView(Context context) {
		super(context);
		init();
	}

	public PickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setOnSelectListener(onSelectListener listener) {
		mSelectListener = listener;
	}

	private void performSelect() {
		if (mSelectListener != null)
			mSelectListener.onSelect(arrayList.get(mCurrentSelected),
					mCurrentSelected);
		// mSelectListener.onSelect(mDataList.get(mCurrentSelected));
	}

	public void setBeaconData(ArrayList<BeaconInfo> lists) {
		this.arrayList = lists;

		// List<String> datas = new ArrayList<String>();
		// for (BeaconInfo beaconInfo : lists) {
		// datas.add(beaconInfo.getDesc());
		// }
		// mDataList = datas;
		mCurrentSelected = arrayList.size() / 2;
		invalidate();
	}

	public void addBeacon(BeaconInfo info) {
		BeaconTables.addBeacons(getContext(), info);
	}

	public void delBeacon(BeaconInfo info) {
		BeaconTables.delBeacons(getContext(), info);
	}

	/**
	 * 选择选中的item的index
	 * 
	 * @param selected
	 */
	public void setSelected(int selected) {
		mCurrentSelected = selected;
		int distance = arrayList.size() / 2 - mCurrentSelected;
		if (distance < 0)
			for (int i = 0; i < -distance; i++) {
				moveHeadToTail();
				mCurrentSelected--;
			}
		else if (distance > 0)
			for (int i = 0; i < distance; i++) {
				moveTailToHead();
				mCurrentSelected++;
			}
		invalidate();
	}

	/**
	 * 选择选中的内容
	 * 
	 * @param mSelectItem
	 */
	public void setSelected(String mSelectItem) {
		for (int i = 0; i < arrayList.size(); i++)
			if (arrayList.get(i).equals(mSelectItem)) {
				setSelected(i);
				break;
			}
	}

	private void moveHeadToTail() {
		BeaconInfo head = arrayList.get(0);
		arrayList.remove(0);
		arrayList.add(head);
	}

	private void moveTailToHead() {
		BeaconInfo tail = arrayList.get(arrayList.size() - 1);
		arrayList.remove(arrayList.size() - 1);
		arrayList.add(0, tail);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewHeight = getMeasuredHeight();
		mViewWidth = getMeasuredWidth();
		// 按照View的高度计算字体大小
		mMaxTextSize = mViewHeight / 4.0f;
		mMinTextSize = mMaxTextSize / 2f;
		isInit = true;
		invalidate();
	}

	private void init() {
		timer = new Timer();
		arrayList = new ArrayList<BeaconInfo>();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 根据index绘制view
		if (isInit)
			drawData(canvas);
	}

	@SuppressLint("NewApi")
	private void drawData(Canvas canvas) {
		// 先绘制选中的text再往上往下绘制其余的text
		float scale = parabola(mViewHeight / 4.0f, mMoveLen);
		float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
		mPaint.setTextSize(size / 10);
		mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
		// text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
		float x = (float) (mViewWidth / 2.0);
		float y = (float) (mViewHeight / 2.0 + mMoveLen);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

		// canvas.drawText(mDataList.get(mCurrentSelected), x, baseline,
		// mPaint);
		drawLayout(canvas, x, y, scale, mPaint, mCurrentSelected);

		// 绘制上方data
		for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
			drawOtherText(canvas, i, -1);
		}
		// 绘制下方data
		for (int i = 1; (mCurrentSelected + i) < arrayList.size(); i++) {
			drawOtherText(canvas, i, 1);
		}
	}

	/**
	 * @param canvas
	 * @param position
	 *            距离mCurrentSelected的差值
	 * @param type
	 *            1表示向下绘制，-1表示向上绘制
	 */
	private void drawOtherText(Canvas canvas, int position, int type) {
		float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
				* mMoveLen);
		float scale = parabola(mViewHeight / 4.0f, d);
		float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
		mPaint.setTextSize(size / 10);
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
		float y = (float) (mViewHeight / 2.0 + type * d);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float x = (float) (mViewWidth / 2.0);
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		// canvas.drawText(mDataList.get(mCurrentSelected + type * position),
		// x , baseline, mPaint);
		int curPosition = mCurrentSelected + type * position;
		drawLayout(canvas, x, y, scale, mPaint, curPosition);
	}

	private Bitmap bgBmp = BitmapFactory.decodeResource(getContext()
			.getResources(), R.drawable.shoe_shoe_bg);
	private Bitmap imgBmp = BitmapFactory.decodeResource(getContext()
			.getResources(), R.drawable.shoe_shoe_icon);

	public void drawLayout(Canvas canvas, float x, float y, float scale,
			Paint mPaint, int position) {
		BeaconInfo info = arrayList.get(position);
		Bitmap bgBitmap = resizeBitmap(bgBmp, scale * 0.7f);
		Bitmap imgBitmap = resizeBitmap(imgBmp, scale * 0.7f);

		canvas.drawBitmap(bgBitmap, x - bgBitmap.getWidth() / 2,
				y - bgBitmap.getHeight() / 2, mPaint);
		canvas.drawBitmap(imgBitmap, x - imgBitmap.getWidth() / 2, y
				- imgBitmap.getHeight() / 2, mPaint);

		canvas.drawText(info.getDesc(), x, y + imgBitmap.getHeight(), mPaint);
	}

	public Bitmap resizeBitmap(Bitmap bm, float scale) {
		Matrix matrix = new Matrix();
		if (scale > SCALE_MIN) {
			matrix.postScale(scale, scale);
		} else {
			matrix.postScale(SCALE_MIN, SCALE_MIN);
		}

		return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
				matrix, true);
	}

	public int getCurPosition() {
		return mCurrentSelected;
	}

	/**
	 * 抛物线
	 * 
	 * @param zero
	 *            零点坐标
	 * @param x
	 *            偏移量
	 * @return scale
	 */
	private float parabola(float zero, float x) {
		float f = (float) (1 - Math.pow(x / zero, 2));
		return f < 0 ? 0 : f;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			doDown(event);
			tag_click = true;
			break;
		case MotionEvent.ACTION_MOVE:
			doMove(event);
			break;
		case MotionEvent.ACTION_UP:
			doUp(event);
			if (tag_click) {
				if (clickListener != null) {
					clickListener.onClick();
				}
			}
			break;
		}
		return true;
	}

	public void setOnCurrentClickListener(OnCurrentClickListener l) {
		// TODO Auto-generated method stub
		this.clickListener = l;
	}

	private void doDown(MotionEvent event) {
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mLastDownY = event.getY();
	}

	private void doMove(MotionEvent event) {

		mMoveLen += (event.getY() - mLastDownY);
		if (mMoveLen != 0.0) {
			tag_click = false;
		}
		System.out.println("move----------->" + mMoveLen);
		if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
			// 往下滑超过离开距离
			moveTailToHead();
			mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
		} else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
			// 往上滑超过离开距离
			moveHeadToTail();
			mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
		}

		mLastDownY = event.getY();
		invalidate();
	}

	private void doUp(MotionEvent event) {
		// 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
		if (Math.abs(mMoveLen) < 0.0001) {
			mMoveLen = 0;
			return;
		}
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mTask = new MyTimerTask(updateHandler);
		timer.schedule(mTask, 0, 10);
	}

	class MyTimerTask extends TimerTask {
		Handler handler;

		public MyTimerTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendMessage(handler.obtainMessage());
		}

	}

	public interface onSelectListener {
		void onSelect(BeaconInfo info, int position);
	}

	private boolean tag_click = false;
	private OnCurrentClickListener clickListener;

	public interface OnCurrentClickListener {
		void onClick();
	}

}
