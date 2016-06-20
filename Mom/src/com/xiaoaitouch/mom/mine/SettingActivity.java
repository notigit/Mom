package com.xiaoaitouch.mom.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.droid.BaseActivity;
import com.xiaoaitouch.mom.util.ButtomDialog;
import com.xiaoaitouch.mom.util.UserDataUtils;

/**
 * 设置
 * 
 * @author huxin
 * 
 */
public class SettingActivity extends BaseActivity {
	@Bind(R.id.mine_setting_out_login_lay)
	LinearLayout mOutLayout;
	@Bind(R.id.more_exist_app)
	Button mExistButton;

	private UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MomApplication application = (MomApplication) getApplication();
		mUserInfo = application.getUserInfo();
		setContentView(R.layout.setting_activity);
		ButterKnife.bind(this);
		initData();
	}

	private void initData() {
		if (mUserInfo != null) {
			if (!TextUtils.isEmpty(mUserInfo.getAccount())
					&& !TextUtils.isEmpty(mUserInfo.getPwd())) {
				mOutLayout.setVisibility(View.VISIBLE);
			} else {
				mOutLayout.setVisibility(View.INVISIBLE);
			}
		} else {
			mOutLayout.setVisibility(View.INVISIBLE);
		}
	}

	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		onBackBtnClick();
	}

	@OnClick(R.id.more_exist_app)
	public void existApp() {
		final ButtomDialog mSheetDialog = new ButtomDialog(mContext);
		View outView = LayoutInflater.from(mContext).inflate(
				R.layout.exist_app_item, null);
		outView.findViewById(R.id.dialog_out_tv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mSheetDialog.dismiss();
						UserDataUtils uDataUtils = new UserDataUtils(mActivity);
						uDataUtils.outLogin();
						mOutLayout.setVisibility(View.INVISIBLE);
					}
				});

		outView.findViewById(R.id.dialog_cancle_lay).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						mSheetDialog.dismiss();
					}
				});
		mSheetDialog.setContentView(outView);
		mSheetDialog.show();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = mSheetDialog.getWindow()
				.getAttributes();
		lp.width = (int) (display.getWidth() - 80); // 设置宽度
		mSheetDialog.getWindow().setAttributes(lp);
	}
}
