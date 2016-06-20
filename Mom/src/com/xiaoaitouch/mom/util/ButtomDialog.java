package com.xiaoaitouch.mom.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.xiaoaitouch.mom.R;

/**
 * 从低向上出现的dialog
 * 
 * @author huxin
 * 
 */
public class ButtomDialog extends Dialog {

	public ButtomDialog(Context context) {
		this(context, R.style.ActionSheetDialog);
		init(context);
	}

	public ButtomDialog(Context context, int theme) {
		super(context, theme == 0 ? R.style.ActionSheetDialog : theme);
		init(context);
	}

	private void init(Context context) {
		final Window window = getWindow();
		window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}
}
