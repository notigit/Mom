package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.xiaoaitouch.mom.MomApplication;

public class XEditText extends EditText {

	public XEditText(Context context) {
		super(context);
	}

	public XEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public XEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		super.setTypeface(MomApplication.getTypeface());
	}

}
