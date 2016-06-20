package com.xiaoaitouch.mom.mine;

import java.lang.reflect.Type;
import java.util.Calendar;

import android.content.Intent;
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
import com.xiaoaitouch.mom.util.ButtomDialog;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.util.UserDataUtils;

/**
 * 预产期界面
 * 
 * @author huxin
 * 
 */
public class DueActivity extends BaseActivity implements OnClickListener {
	@Bind(R.id.user_input_due_tv)
	TextView mInputDue;
	@Bind(R.id.calculated_due_tv)
	TextView mCalculated;
	@Bind(R.id.user_input_due_view)
	View mInputDueView;
	@Bind(R.id.user_due_tv)
	TextView mDueTv;
	@Bind(R.id.user_end_due_tv)
	TextView mEndDueTv;
	@Bind(R.id.user_due_day_tv)
	TextView mDueDay;
	@Bind(R.id.user_calculated_idue_view)
	View mCalculatedDueView;

	private UserInfo mUserInfo;
	private boolean isFlage = false;

	private AbstractWheel abstractWheelYear;
	private AbstractWheel abstractWheelMonth;
	private AbstractWheel abstractWheelDay;
	private int mYear = 0;
	private int mMonth = 0;
	private int mDay = 0;
	private Calendar mCalendar;
	private int mType = 1;
	private int mDayIndex = 0;
	private String mEndDueTime;
	private String mDueTime;
	private int mSelectYear = 0;
	private int mSelectMonth = 0;
	private int mSelectDay = 0;
	private int indexDay = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserInfo = ((MomApplication) getApplication()).getUserInfo();
		setContentView(R.layout.due_activity);
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

	private void submitData() {
		if (isFlage) {
			String mDueStr = "";
			if (!TextUtils.isEmpty(mDueTime)) {
				mDueStr = StringUtils.getDateFromStr(mDueTime);
			}
			String mEndDueStr = "";
			if (!TextUtils.isEmpty(mEndDueTime)) {
				mEndDueStr = StringUtils.getDateFromStr(mEndDueTime);
			}
			String mDueDayStr = mDueDay.getText().toString().trim();
			if (!TextUtils.isEmpty(mDueDayStr)) {
				mDueDayStr = (String) mDueDayStr.subSequence(0,
						mDueDayStr.length() - 1);
			}
			String[] mStr = { mDueStr, mEndDueStr, mDueDayStr };
			submitDueInfo(mStr);
		} else {
			onBackBtnClick();
		}
	}

	private void submitDueInfo(String[] str) {
		mBlockDialog.show();
		GsonTokenRequest<MineInfo> request = new GsonTokenRequest<MineInfo>(
				com.android.volley.Request.Method.POST, Configs.SERVER_URL
						+ "/user/modify/due",
				new Listener<JsonResponse<MineInfo>>() {

					@Override
					public void onResponse(JsonResponse<MineInfo> response) {
						mBlockDialog.dismiss();
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
							Intent intent = new Intent();
							setResult(1012, intent);
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
		HttpApi.getUpdateDueInfo(mActivity, "/user/modify/due", request, str);
	}

	private void initView() {
		mCalendar = Calendar.getInstance();
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH) + 1;
		mDayIndex = mCalendar.get(Calendar.DATE);
		mDay = StringUtils.chooseTime(mYear, mMonth);

		mInputDue.setOnClickListener(this);
		mCalculated.setOnClickListener(this);

		findViewById(R.id.user_due_lay).setOnClickListener(this);
		findViewById(R.id.user_end_due_lay).setOnClickListener(this);
		findViewById(R.id.user_due_day_lay).setOnClickListener(this);

		setViewData();
	}

	private void setViewData() {
		if (mUserInfo != null) {
			if (!TextUtils.isEmpty(mUserInfo.getLastMensesTime())
					&& !mUserInfo.getLastMensesTime().equals("0")) {// 末次月经
				mEndDueTime = StringUtils.getStringFromDate(Long
						.valueOf(mUserInfo.getLastMensesTime()));
				mEndDueTv.setText(mEndDueTime);

				String[] mDueTimes = mEndDueTime.split("-");
				mDueTime = StringUtils.getAddDate(mDueTimes, 280);
				mDueTv.setText(mDueTime);

			} else if (!TextUtils.isEmpty(mUserInfo.getDueTime())
					&& !mUserInfo.getDueTime().equals("0")) {// 预产期
				mDueTime = StringUtils.getStringFromDate(Long.valueOf(mUserInfo
						.getDueTime()));
				mDueTv.setText(mDueTime);

				String[] mDueTimes = mDueTime.split("-");
				mEndDueTime = StringUtils.getAddDate(mDueTimes, -280);
				mEndDueTv.setText(mEndDueTime);
			}
			mDueDay.setText(mUserInfo.getMensesCircle() + "天");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_input_due_tv:
			mType = 1;
			mInputDue.setBackgroundResource(R.drawable.due_left_corner_bg);
			mCalculated.setBackgroundResource(R.drawable.due_right_corner);
			mInputDue.setTextColor(getResources().getColor(
					R.color.due_select_color));
			mCalculated.setTextColor(getResources().getColor(R.color.white));
			mInputDueView.setVisibility(View.VISIBLE);
			mCalculatedDueView.setVisibility(View.GONE);
			mDueTv.setText(mDueTime);
			break;
		case R.id.calculated_due_tv:// 计算预产期
			mType = 2;
			mInputDue.setBackgroundResource(R.drawable.due_left_corner);
			mCalculated.setBackgroundResource(R.drawable.due_right_corner_bg);
			mInputDue.setTextColor(getResources().getColor(R.color.white));
			mCalculated.setTextColor(getResources().getColor(
					R.color.due_select_color));
			mInputDueView.setVisibility(View.GONE);
			mCalculatedDueView.setVisibility(View.VISIBLE);
			mEndDueTv.setText(mEndDueTime);
			break;
		case R.id.user_end_due_lay:
			showDateDialog("末次月经日期", true);
			break;
		case R.id.user_due_day_lay:
			showDialog(12, 30, 6, R.layout.new_menstrual_cycle_register_item,
					5, "月经周期");
			break;

		case R.id.user_due_lay:
			showDateDialog("预产期", false);
			break;

		default:
			break;
		}

	}

	private String getDataValues(int values) {
		String result = "";
		if (values <= 9) {
			result = "0" + values;
		} else {
			result = String.valueOf(values);
		}
		return result;
	}

	private void showDateDialog(String title, boolean isflage) {
		final ButtomDialog mChooseDialog = new ButtomDialog(mActivity);
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		View view = inflater.inflate(R.layout.new_due_item, null);
		TextView mTitle = (TextView) view.findViewById(R.id.dialog_title);
		mTitle.setText(title);
		view.findViewById(R.id.done_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						isFlage = true;
						String dueTime = mSelectYear + "-"
								+ getDataValues(mSelectMonth) + "-"
								+ getDataValues(mSelectDay);
						if (mType == 1) {// 预产期
							mDueTime = dueTime;
							mDueTv.setText(dueTime);
							mEndDueTime = StringUtils.getAddDate(
									dueTime.split("-"), -280);
							mEndDueTv.setText(mEndDueTime);
						} else {
							mEndDueTime = dueTime;
							mEndDueTv.setText(dueTime);
							mDueTime = StringUtils.getAddDate(
									dueTime.split("-"), 280);
							mDueTv.setText(mDueTime);
						}
						mChooseDialog.dismiss();
					}
				});
		abstractWheelYear = (AbstractWheel) view
				.findViewById(R.id.due_yser_choose_view);
		abstractWheelMonth = (AbstractWheel) view
				.findViewById(R.id.due_month_choose_view);
		abstractWheelDay = (AbstractWheel) view
				.findViewById(R.id.due_date_choose_view);

		setAbstractWheelYear(mYear - 5, 100, 5, isflage, "年");
		setAbstractWheelMonth(1, 12, mMonth - 1, isflage, "月");

		mChooseDialog.setContentView(view);
		mChooseDialog.show();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = mChooseDialog.getWindow()
				.getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		mChooseDialog.getWindow().setAttributes(lp);
	}

	/**
	 * 年份
	 * 
	 * @param initValue
	 * @param forValue
	 * @param currentIndex
	 * @param isflage
	 * @param content
	 */
	private void setAbstractWheelYear(int initValue, int forValue,
			final int currentIndex, final boolean isflage, String content) {
		String[] mStr = new String[forValue];
		for (int i = 0; i < forValue; i++) {
			mStr[i] = String.valueOf(initValue + i) + content;
		}
		final ArrayWheelAdapter<String> ampmAdapterYear = new ArrayWheelAdapter<String>(
				mActivity, mStr);
		ampmAdapterYear.setItemResource(R.layout.common_wheel_items);
		ampmAdapterYear.setItemTextResource(R.id.common_wheel_item_text);
		abstractWheelYear.setViewAdapter(ampmAdapterYear);
		abstractWheelYear.setCurrentItem(currentIndex, false);
		String str = ampmAdapterYear.getItemText(currentIndex) + "";
		str = (String) str.subSequence(0, str.length() - 1);
		mYear = Integer.valueOf(str);
		mSelectYear = mYear;
		abstractWheelYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				CharSequence charSequence = ampmAdapterYear
						.getItemText(newValue);
				if (charSequence != null) {
					String str = charSequence.toString();
					str = (String) str.subSequence(0, str.length() - 1);
					if (!TextUtils.isEmpty(str)) {
						mSelectYear = Integer.valueOf(str);
						if (isflage) {
							int minYear = mYear - 2;
							if (mSelectYear > mYear || mSelectYear <= minYear) {
								abstractWheelYear.setCurrentItem(currentIndex,
										true);
							} else {
								int day = StringUtils.chooseTime(mSelectYear,
										mSelectMonth);
								setAbstractWheelDay(1, day, 0, isflage, "日");
							}
						} else {
							int maxYear = mYear + 2;
							if (mSelectYear < mYear || mSelectYear >= maxYear) {
								abstractWheelYear.setCurrentItem(currentIndex,
										true);
							} else {
								int day = StringUtils.chooseTime(mSelectYear,
										mSelectMonth);
								setAbstractWheelDay(1, day, 0, isflage, "日");
							}
						}
					}
				}
			}
		});
		abstractWheelYear.setCyclic(true);
		setAbstractWheelDay(1, mDay, mDayIndex - 1, isflage, "日");
	}

	/**
	 * 月份
	 * 
	 * @param initValue
	 * @param forValue
	 * @param currentIndex
	 * @param isflage
	 * @param content
	 */
	private void setAbstractWheelMonth(int initValue, int forValue,
			final int currentIndex, final boolean isflage, String content) {
		String[] mStr = new String[forValue];
		for (int i = 0; i < forValue; i++) {
			mStr[i] = String.valueOf(initValue + i) + content;
		}
		final ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				mActivity, mStr);
		ampmAdapter.setItemResource(R.layout.common_wheel_items);
		ampmAdapter.setItemTextResource(R.id.common_wheel_item_text);
		abstractWheelMonth.setViewAdapter(ampmAdapter);
		abstractWheelMonth.setCurrentItem(currentIndex, false);
		String str = ampmAdapter.getItemText(currentIndex) + "";
		str = (String) str.subSequence(0, str.length() - 1);
		mMonth = Integer.valueOf(str);
		mSelectMonth = mMonth;
		abstractWheelMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				CharSequence charSequence = ampmAdapter.getItemText(newValue);
				if (charSequence != null) {
					String str = charSequence.toString();
					str = (String) str.subSequence(0, str.length() - 1);
					if (!TextUtils.isEmpty(str)) {
						mSelectMonth = Integer.valueOf(str);
						if (isflage) {
							if (mSelectYear == mYear) {
								if (mMonth < mSelectMonth) {
									abstractWheelMonth.setCurrentItem(
											currentIndex, true);
								} else {
									int day = StringUtils.chooseTime(
											mSelectYear, mSelectMonth);
									setAbstractWheelDay(1, day, 0, isflage, "日");
								}
							} else {
								int day = StringUtils.chooseTime(mSelectYear,
										mSelectMonth);
								setAbstractWheelDay(1, day, 0, isflage, "日");
							}
						} else {
							if (mSelectYear == mYear) {
								if (mMonth >= mSelectMonth) {
									abstractWheelMonth.setCurrentItem(
											currentIndex, true);
								} else {
									int day = StringUtils.chooseTime(
											mSelectYear, mSelectMonth);
									setAbstractWheelDay(1, day, 0, isflage, "日");
								}
							} else {
								int day = StringUtils.chooseTime(mSelectYear,
										mSelectMonth);
								setAbstractWheelDay(1, day, 0, isflage, "日");
							}
						}
					}
				}
			}
		});
		abstractWheelMonth.setCyclic(true);
	}

	/**
	 * 显示这个月的天数
	 * 
	 * @param initValue
	 * @param forValue
	 * @param currentIndex
	 * @param isflage
	 * @param content
	 */
	private void setAbstractWheelDay(int initValue, int forValue,
			int currentIndex, final boolean isflage, String content) {
		String[] mStr = new String[forValue];
		for (int i = 0; i < forValue; i++) {
			if (mDayIndex == (initValue + i)) {
				indexDay = i;
			}
			mStr[i] = String.valueOf(initValue + i) + content;
		}
		final ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				mActivity, mStr);
		ampmAdapter.setItemResource(R.layout.common_wheel_items);
		ampmAdapter.setItemTextResource(R.id.common_wheel_item_text);
		abstractWheelDay.setViewAdapter(ampmAdapter);
		abstractWheelDay.setCurrentItem(currentIndex, false);
		String str = ampmAdapter.getItemText(currentIndex) + "";
		String mDayStr = (String) str.subSequence(0, str.length() - 1);
		mSelectDay = Integer.valueOf(mDayStr);
		abstractWheelDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				CharSequence charSequence = ampmAdapter.getItemText(newValue);
				if (charSequence != null) {
					String str = charSequence.toString();
					String mDayStr = (String) str.subSequence(0,
							str.length() - 1);
					mSelectDay = Integer.valueOf(mDayStr);
					if (isflage) {
						if (mSelectYear == mYear) {
							if (mSelectMonth == mMonth) {
								if (mSelectDay > mDayIndex) {
									abstractWheelDay.setCurrentItem(indexDay,
											true);
								}
							}
						}
					} else {
						if (mSelectYear == mYear) {
							if (mSelectMonth == mMonth) {
								if (mSelectDay < mDayIndex) {
									abstractWheelDay.setCurrentItem(indexDay,
											true);
								}
							}
						}
					}
				}
			}
		});
		abstractWheelDay.setCyclic(true);
	}

	/**
	 * 底部选择框
	 * 
	 * @param initValue
	 * @param forValue
	 * @param currentIndex
	 * @param layout
	 * @param type
	 * @param title
	 */
	private void showDialog(final int initValue, int forValue,
			int currentIndex, int layout, final int type, String title) {
		String[] mStr = new String[forValue];
		for (int i = 0; i < forValue; i++) {
			mStr[i] = String.valueOf(initValue + i);
		}
		final ButtomDialog mChooseDialog = new ButtomDialog(mActivity);
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
		WindowManager windowManager = mActivity.getWindowManager();
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
				if (type == 5) {
					mDueDay.setText(ampmAdapter.getItemText(newValue) + "天");
				}
			}
		});
	}
}
