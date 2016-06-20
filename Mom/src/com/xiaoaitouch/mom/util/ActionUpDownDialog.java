package com.xiaoaitouch.mom.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.xiaoaitouch.mom.R;

/**
 * 进从上到中间
 * 出事中间到底部
 * 
 * @author huxin
 * 
 */
public class ActionUpDownDialog extends Dialog {

	public ActionUpDownDialog(Context context) {
		this(context, R.style.ActionUpDownDialog);
		this.setCanceledOnTouchOutside(false);
		init(context);
	}

	public ActionUpDownDialog(Context context, int theme) {
		super(context, theme == 0 ? R.style.ActionUpDownDialog : theme);
		this.setCanceledOnTouchOutside(false);
		init(context);
	}

	private void init(Context context) {
		final Window window = getWindow();
		window.setGravity(Gravity.CENTER);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}

}
