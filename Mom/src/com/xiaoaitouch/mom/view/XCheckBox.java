package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.xiaoaitouch.mom.MomApplication;

public class XCheckBox extends CheckBox {

	public XCheckBox(Context context) {
		super(context);
	}

	public XCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public XCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		super.setTypeface(MomApplication.getTypeface());
	}

}
