package com.xiaoaitouch.mom.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.Window;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.configs.Constant;

/**
 * 从上向下dialog
 * 
 * @author huxin
 * 
 */
public class ActionSheetDialogDown extends Dialog {
	private Context mContext;

	public ActionSheetDialogDown(Context context) {
		this(context, R.style.ActionSheetDialogDown);
		this.mContext = context;
		init(context);
	}

	public ActionSheetDialogDown(Context context, int theme) {
		super(context, theme == 0 ? R.style.ActionSheetDialogDown : theme);
		init(context);
	}

	private void init(Context context) {
		final Window window = getWindow();
		window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		openBlur(true);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		openBlur(false);
	}

	private void openBlur(boolean isflage) {
		Intent intent = new Intent();
		intent.setAction(Constant.BROADCAST_ACTION);
		intent.putExtra("isBlur", isflage);
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
	}
}
