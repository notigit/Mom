package com.xiaoaitouch.mom.droid;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xiaoaitouch.mom.util.BlockDialog;
import com.xiaoaitouch.mom.util.Logger;
import com.xiaoaitouch.mom.util.Utils;

public abstract class BaseFragment extends Fragment {
	protected BlockDialog mBlockDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBlockDialog = new BlockDialog(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();

		Logger.d(getClass().getSimpleName());

		MobclickAgent.onPageStart(getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();

		Logger.d(getClass().getSimpleName());

		MobclickAgent.onPageEnd(getClass().getSimpleName());
	}

	public Intent getIntent() {
		return getActivity().getIntent();
	}

	protected void startIntent(Class<?> activity) {
		Intent mIntent = new Intent(getActivity(), activity);
		getActivity().startActivity(mIntent);
	}

	protected void startIntent(Class<?> activity, Bundle bundle) {
		Intent mIntent = new Intent(getActivity(), activity);
		mIntent.putExtras(bundle);
		getActivity().startActivity(mIntent);
	}

	protected void finish() {
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			getActivity().finish();
		}

	}

	protected void showToast(CharSequence text, int duration) {
		Utils.showToast(text, duration);
	}

	protected void showToast(CharSequence text) {
		Utils.showToast(text, Toast.LENGTH_SHORT);
	}

	protected void showToast(int resId, int duration) {
		showToast(getString(resId), duration);
	}

	protected Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	protected int getColor(int resId) {
		return getResources().getColor(resId);
	}

	public boolean onBackPressed() {
		return false;
	}

}
