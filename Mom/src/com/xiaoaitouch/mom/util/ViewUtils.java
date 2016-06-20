package com.xiaoaitouch.mom.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 初始化视图
 * 
 * @author huxin
 * 
 */
public class ViewUtils {
	public static void showView(View view) {
		view.setVisibility(View.VISIBLE);
	}

	public static void hiddenView(View view) {
		view.setVisibility(View.GONE);
	}

	public static void hideSoftInput(Activity activity, IBinder windowToken) {
		if (activity == null || windowToken == null) {
			return;
		}

		final InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);

		if (imm != null) {
			imm.hideSoftInputFromWindow(windowToken, 0);
		}
	}

	public static void hideAutoSoftInput(Window window) {
		if (window != null) {
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}
	}

	public static void hideAutoSoftInput(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 
                InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
