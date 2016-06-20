package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.xiaoaitouch.mom.MomApplication;

public class XRadioButton extends RadioButton {

	public XRadioButton(Context context) {
		super(context);
	}

	public XRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public XRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		super.setTypeface(MomApplication.getTypeface());
	}

}
