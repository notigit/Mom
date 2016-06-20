package com.xiaoaitouch.mom.mine;

import java.lang.reflect.Type;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Response.Listener;
import com.google.gson.reflect.TypeToken;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.MineInfo;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.droid.BaseActivity;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.ActionSheetDialog;
import com.xiaoaitouch.mom.util.UserDataUtils;

/**
 * 资料
 * 
 * @author huxin
 * 
 */
public class MaterialActivity extends BaseActivity implements OnClickListener {
	@Bind(R.id.user_age_tv)
	TextView mAgeTv;
	@Bind(R.id.user_height_tv)
	TextView mHeightTv;

	private String mAge;
	private String mHeight;
	private String[] mStr;
	private UserInfo mUserInfo;
	private String meters = "1";
	private TextView mHeightLeftTv;
	private boolean isFlage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserInfo = ((MomApplication) getApplication()).getUserInfo();

		setContentView(R.layout.material_activity);
		ButterKnife.bind(this);
		initView();
	}

	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		submitData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			submitData();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		findViewById(R.id.user_age_view_lay).setOnClickListener(this);
		findViewById(R.id.user_height_view_lay).setOnClickListener(this);
		if (mUserInfo != null) {
			mAgeTv.setText(mUserInfo.getAge() + "岁");
			mHeightTv.setText(mUserInfo.getHeight() + "cm");
		}
	}

	private void submitData() {
		if (isFlage) {
			mAge = mAgeTv.getText().toString().trim();
			mAge = mAge.substring(0, mAge.length() - 1);
			mHeight = mHeightTv.getText().toString().trim();
			mHeight = mHeight.substring(0, mHeight.length() - 2);

			mStr = (mAge + "-" + mHeight).split("-");
			submitUpdateUserInfo(mStr);
		} else {
			onBackBtnClick();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_age_view_lay:
			showDialog(16, 105, 6, R.layout.new_age_register_item, 1, "年龄");
			break;
		case R.id.user_height_view_lay:
			showDialog(1, 99, 6, R.layout.new_height_register_item, 2, "身高");
			break;
		default:
			break;
		}
	}

	/**
	 * 提交用户修改信息
	 * 
	 * @param str
	 */
	private void submitUpdateUserInfo(String[] str) {
		GsonTokenRequest<MineInfo> request = new GsonTokenRequest<MineInfo>(
				com.android.volley.Request.Method.POST, Configs.SERVER_URL
						+ "/user/modify/info",
				new Listener<JsonResponse<MineInfo>>() {

					@Override
					public void onResponse(JsonResponse<MineInfo> response) {
						switch (response.state) {
						case Configs.UN_USE:
							showToast("版本过低请升级新版本");
							break;
						case Configs.FAIL:
							showToast(response.msg);
							break;
						case Configs.SUCCESS:
							UserDataUtils uDataUtils = new UserDataUtils(
									mActivity);
							uDataUtils.saveData(response.data);
							onBackBtnClick();
							break;
						}
					}

				}, null) {

			@Override
			public Type getType() {
				Type type = new TypeToken<JsonResponse<MineInfo>>() {
				}.getType();

				return type;
			}
		};
		HttpApi.getUpdateUserInfo(mActivity, "/user/modify/info", request, str);
	}

	private void showDialog(final int initValue, int forValue,
			int currentIndex, int layout, final int type, String title) {
		String[] mStr = new String[forValue];
		for (int i = 0; i < forValue; i++) {
			mStr[i] = String.valueOf(initValue + i);
		}
		final ActionSheetDialog mChooseDialog = new ActionSheetDialog(mActivity);
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		View view = inflater.inflate(layout, null);
		TextView mTitle = (TextView) view.findViewById(R.id.dialog_title);
		mTitle.setText(title);
		view.findViewById(R.id.done_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						mChooseDialog.dismiss();
					}
				});
		if (type == 2) {
			mHeightLeftTv = (TextView) view.findViewById(R.id.height_left_tv);
		}
		AbstractWheel abstractWheel = (AbstractWheel) view
				.findViewById(R.id.choose_view);
		final ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				mActivity, mStr);
		ampmAdapter.setItemResource(R.layout.common_wheel_items);
		ampmAdapter.setItemTextResource(R.id.common_wheel_item_text);
		abstractWheel.setViewAdapter(ampmAdapter);
		abstractWheel.setCurrentItem(currentIndex, false);
		mChooseDialog.setContentView(view);
		mChooseDialog.show();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = mChooseDialog.getWindow()
				.getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		mChooseDialog.getWindow().setAttributes(lp);
		abstractWheel.setCyclic(true);
		abstractWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				isFlage = true;
				if (type == 2) {
					String valueUp = ampmAdapter.getItemText(oldValue)
							.toString();
					String valueCurrent = ampmAdapter.getItemText(newValue)
							.toString();
					if (!TextUtils.isEmpty(valueUp)
							&& !TextUtils.isEmpty(valueCurrent)) {
						if (Integer.valueOf(valueUp) == 99
								&& Integer.valueOf(valueCurrent) == 1) {
							meters = "2";
							mHeightLeftTv.setText("2");
						} else if (Integer.valueOf(valueUp) == 1
								&& Integer.valueOf(valueCurrent) == 99) {
							meters = "1";
							mHeightLeftTv.setText("1");
						}
					}
					if (Integer.valueOf(valueCurrent) <= 9) {
						mHeightTv.setText(meters + "0"
								+ ampmAdapter.getItemText(newValue) + "cm");
					} else
						mHeightTv.setText(meters
								+ ampmAdapter.getItemText(newValue) + "cm");
				} else if (type == 1) {
					mAgeTv.setText(ampmAdapter.getItemText(newValue) + "岁");
				}
			}
		});
	}
}
