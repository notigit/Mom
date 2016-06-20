package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xiaoaitouch.mom.R;

public class DrawOvalView extends View {
	private Paint paint;
	private Context mContext;

	public DrawOvalView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public DrawOvalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(4);
		paint.setColor(mContext.getResources().getColor(R.color.white));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		RectF rectF = new RectF(0, 0, 40, 40);
		canvas.drawOval(rectF, paint);

	}
}