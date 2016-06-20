package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.xiaoaitouch.mom.MomApplication;

public class XButton extends Button {

	public XButton(Context context) {
		super(context);
	}

	public XButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public XButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		super.setTypeface(MomApplication.getTypeface());
	}

}
