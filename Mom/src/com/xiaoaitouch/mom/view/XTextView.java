package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;

public class XTextView extends TextView {

	public XTextView(Context context) {
		super(context);
	}

	public XTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public XTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
//		super.setTypeface(MomApplication.getTypeface());
	}

}
