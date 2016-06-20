package com.xiaoaitouch.mom.droid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.util.BlockDialog;
import com.xiaoaitouch.mom.util.Logger;
import com.xiaoaitouch.mom.util.Utils;

public class BaseFragmentActivity extends FragmentActivity {
	protected Context mContext;
	protected Activity mActivity;
	protected BlockDialog mBlockDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		mContext = this;
		mBlockDialog=new BlockDialog(mContext);
	}

	protected void startIntent(Class<?> mClass) {
		Intent intent = new Intent(mActivity, mClass);
		startActivity(intent);
	}

	/**
	 * 返回按钮点击
	 */
	protected void onBackBtnClick() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				onBackBtnClick();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Logger.d(getClass().getSimpleName());

		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		Logger.d(getClass().getSimpleName());

		MobclickAgent.onPause(this);
	}

	protected void showToast(CharSequence text, int duration) {
		Utils.showToast(text, duration);
	}

	protected void showToast(CharSequence text) {
		Utils.showToast(text, Toast.LENGTH_SHORT);
	}

	protected void showToast(int resId) {
		Utils.showToast(getString(resId), Toast.LENGTH_SHORT);
	}

	protected void showToast(int resId, int duration) {
		Utils.showToast(getString(resId), duration);
	}
}
