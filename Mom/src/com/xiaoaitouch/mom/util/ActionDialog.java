package com.xiaoaitouch.mom.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.xiaoaitouch.mom.R;

/**
 * 从右到左出现
 * 
 * @author huxin
 * 
 */
public class ActionDialog extends Dialog {

	public ActionDialog(Context context) {
		this(context, R.style.ActionRightLeftDialog);
		init(context);
	}

	public ActionDialog(Context context, int theme) {
		super(context, theme == 0 ? R.style.ActionRightLeftDialog : theme);
		init(context);
	}

	private void init(Context context) {
		final Window window = getWindow();
		window.setGravity(Gravity.CENTER);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

}
