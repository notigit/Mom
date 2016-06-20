package com.xiaoaitouch.mom.other;

import java.lang.reflect.Type;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.MineInfo;
import com.xiaoaitouch.mom.bean.UserMessage;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.droid.BaseActivity;
import com.xiaoaitouch.mom.main.MainFragmentActivity;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.util.UserDataUtils;
import com.xiaoaitouch.mom.util.ViewUtils;
import com.xiaoaitouch.mom.view.RoundedImageView;

/**
 * 引导页
 * 
 * @author huxin
 * 
 */
public class GuideActivity extends BaseActivity implements OnGestureListener,
		OnTouchListener {
	@Bind(R.id.user_info_lay)
	RelativeLayout mUserInfoLay;
	@Bind(R.id.register_lay)
	LinearLayout mLinearLayout;
	@Bind(R.id.due_know_tv)
	TextView mDueKnowTv;
	@Bind(R.id.due_no_know_tv)
	TextView mDueNoKnowTv;
	@Bind(R.id.due_is_know_lay)
	View mTipDueKnowLay;
	@Bind(R.id.user_buttom_lay)
	RelativeLayout mRelativeLayout;
	/**
	 * 昵称和头像view
	 */
	@Bind(R.id.user_nick_name_view)
	View mNickNameView;
	@Bind(R.id.user_var_image)
	RoundedImageView mRoundedImageView;
	@Bind(R.id.user_nick_next_tv)
	TextView mNickNameNextTv;
	@Bind(R.id.user_input_nick_name_et)
	EditText mNickNameContentEt;
	@Bind(R.id.user_top_lay)
	LinearLayout mTopLinearLayout;
	/**
	 * 年龄view
	 */
	@Bind(R.id.age_view)
	View mAgeView;
	@Bind(R.id.age_choose_view)
	AbstractWheel mAgeAbstractWheel;
	private int mAge = 16;
	/**
	 * 身高view
	 */
	@Bind(R.id.height_view)
	View mHeightView;
	@Bind(R.id.height_choose_view)
	AbstractWheel mHeightAbstractWheel;
	@Bind(R.id.height_left_tv)
	TextView mHeightLeftTv;
	private String mHeightLeftStr = "1";
	private int mHeight = 1;
	/**
	 * 体重view
	 */
	@Bind(R.id.weight_view)
	View mWeightView;
	@Bind(R.id.weight_choose_view)
	AbstractWheel mWeightAbstractWheel;
	private int mWeight = 30;
	/**
	 * 预产期view
	 */
	@Bind(R.id.due_view)
	View mDueView;
	@Bind(R.id.due_month_choose_view)
	AbstractWheel mDueMonthAbstractWheel;
	@Bind(R.id.due_year_tv)
	TextView mDueYearTv;
	@Bind(R.id.due_date_choose_view)
	AbstractWheel mDueDateAbstractWheel;
	@Bind(R.id.click_next)
	TextView mClickNextTv;
	private int mDueYearInt;
	/**
	 * 选择末次经期view
	 */
	@Bind(R.id.end_time_due_view)
	View mEndTimeDueView;
	@Bind(R.id.end_time_due_month_choose_view)
	AbstractWheel mEndTimeDueMonthAbstractWheel;
	@Bind(R.id.end_time_due_year_tv)
	TextView mEndTimeDueTv;
	@Bind(R.id.end_time_due_date_choose_view)
	AbstractWheel mEndTimeDueDateAbstractWheel;
	private int mEndTimeDueInt;
	/**
	 * 选择月经周期view
	 */
	@Bind(R.id.menstrual_cycle_view)
	View mMenstrualCycleView;
	@Bind(R.id.menstrual_cycle_choose_view)
	AbstractWheel mMenstrualCycleAbstractWheel;
	private int mMenstrualCycle = 12;

	private boolean isFlages = false;

	private int mYear = 2015;
	private int mMonth = 1;
	private int mDate = 1;
	private int currentViewType = 1;
	private int changeMonth = 1;
	private UserMessage mUserInfo;

	private GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_activity);
		ButterKnife.bind(this);
		initData();
	}

	public void initData() {
		Calendar now = Calendar.getInstance();
		mYear = now.get(Calendar.YEAR);
		mMonth = now.get(Calendar.MONTH) + 1;
		mDate = now.get(Calendar.DATE);
		mDueYearInt = mYear;
		mEndTimeDueInt = mYear;

		mUserInfo = new UserMessage();
		mGestureDetector = new GestureDetector(this);
		mUserInfoLay.setOnTouchListener(this);
		mUserInfoLay.setLongClickable(true);

		mRoundedImageView.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.guide_face_scale));
		mNickNameView.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.guide_slide_in_mide));
		// 选择身高
		mHeightLeftTv.setText("1");
		// 选择预产期
		mDueYearTv.setText(String.valueOf(mYear));
		// 选择末次经期
		mEndTimeDueTv.setText(String.valueOf(mYear));

	}

	/**
	 * 初始视图的值
	 * 
	 * @param values
	 *            值进行拆分 第一位当前位置（默认）第二位是当前的真实值
	 * @param forValue
	 *            循环的次数
	 * @param initvalues
	 *            初始值
	 * @param index
	 *            当前位置
	 * @param type
	 *            view的类型
	 * @param abstractWheel
	 */
	private void initViewData(String values, int forValue, int initvalues,
			int index, int type, AbstractWheel abstractWheel) {
		if (values != null && values.length() > 0) {
			String[] str = values.split(",");
			if (str != null) {
				setAbstractWheelData(initvalues, forValue,
						Integer.valueOf(str[0]), type, abstractWheel);
			}
		} else
			setAbstractWheelData(initvalues, forValue, index, type,
					abstractWheel);

	}

	/**
	 * 根据年月填充天的视图
	 * 
	 * @param year
	 * @param month
	 * @param initDateName
	 * @param abstractWheel
	 */
	private void showChooseDate(int year, final int type, String initDateName,
			final AbstractWheel abstractWheel) {
		int date = 0;
		if (initDateName != null && initDateName.length() > 0) {
			String[] str = initDateName.split(",");
			if (str != null) {
				date = Integer.valueOf(str[0]);
			}
		} else {
			date = mDate - 1;
		}
		int dates = StringUtils.chooseTime(year, changeMonth);
		String[] mStr = new String[dates];
		for (int i = 0; i < dates; i++) {
			mStr[i] = String.valueOf(i + 1);
		}
		final ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				this, mStr);
		ampmAdapter.setItemResource(R.layout.common_wheel_item);
		ampmAdapter.setItemTextResource(R.id.common_wheel_item_text);
		abstractWheel.setViewAdapter(ampmAdapter);
		abstractWheel.setCurrentItem(date, false);
		abstractWheel.setCyclic(true);
		abstractWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				CharSequence charSequence = ampmAdapter.getItemText(newValue);
				if (charSequence != null) {
					String valueCurrent = charSequence.toString();
					if (type == 1) {
						if (mMonth == changeMonth
								&& Integer.valueOf(valueCurrent) <= mDate) {
							abstractWheel.setCurrentItem(mDate - 1, true);
						}
					} else if (type == 2) {
						if (mMonth == changeMonth
								&& Integer.valueOf(valueCurrent) >= mDate) {
							abstractWheel.setCurrentItem(mDate - 1, true);
						}
					}
				}
			}
		});
	}

	/**
	 * 点击和滑动界面动画
	 * 
	 * @param viewAnimation
	 *            隐藏view
	 * @param showView
	 *            显示view
	 * @param isflage
	 *            isflage==true; 代表点击和向右滑动-动画 ； isflage==false;代表向左滑动-动画
	 */
	private void setViewVisibility(View viewAnimation, View showView,
			boolean isflage) {
		viewAnimation.startAnimation(AnimationUtils.loadAnimation(this,
				isflage ? R.anim.guide_slide_out_left
						: R.anim.guide_slide_in_left));
		showView.setVisibility(View.VISIBLE);
		showView.startAnimation(AnimationUtils.loadAnimation(this,
				isflage ? R.anim.guide_slide_int_right
						: R.anim.guide_slide_out_right));
		viewAnimation.setVisibility(View.GONE);
	}

	private void setAbstractWheelData(int initValue, int forValue,
			int currentIndex, final int type, final AbstractWheel abstractWheel) {
		String[] mStr = new String[forValue];
		for (int i = 0; i < forValue; i++) {
			if (type == 2) {
				mStr[i] = String.valueOf(i + 1);
			} else {
				mStr[i] = String.valueOf(initValue + i);
			}
		}
		final ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				this, mStr);
		ampmAdapter.setItemResource(R.layout.common_wheel_item);
		ampmAdapter.setItemTextResource(R.id.common_wheel_item_text);
		abstractWheel.setViewAdapter(ampmAdapter);
		abstractWheel.setCurrentItem(currentIndex, false);
		abstractWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				if (type == 2) {
					String valueUp = ampmAdapter.getItemText(oldValue)
							.toString();
					String valueCurrent = ampmAdapter.getItemText(newValue)
							.toString();
					if (!TextUtils.isEmpty(valueUp)
							&& !TextUtils.isEmpty(valueCurrent)) {
						if (Integer.valueOf(valueUp) == 99
								&& Integer.valueOf(valueCurrent) == 1) {
							mHeightLeftTv.setText("2");
						} else if (Integer.valueOf(valueUp) == 1
								&& Integer.valueOf(valueCurrent) == 99) {
							mHeightLeftTv.setText("1");
						}
					}
				}
			}
		});
		abstractWheel.setCyclic(true);
	}

	@OnClick(R.id.user_nick_next_tv)
	public void userNickNext() {
		String mNickName = mNickNameContentEt.getText().toString().trim();
		if (!TextUtils.isEmpty(mNickName)) {
			mUserInfo.setNickName(mNickName);
			ViewUtils.hideSoftInput(GuideActivity.this,
					mNickNameContentEt.getWindowToken());
			initViewData(mUserInfo.getAge(), 105, mAge, 6, 1, mAgeAbstractWheel);
			mTopLinearLayout.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.guide_slide_in_top));
			Animation animationButtom = AnimationUtils.loadAnimation(this,
					R.anim.guide_slide_in_buttom);
			mNickNameNextTv.startAnimation(animationButtom);
			animationButtom.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					mNickNameView.setVisibility(View.GONE);
					mAgeView.setVisibility(View.VISIBLE);
					mAgeView.startAnimation(AnimationUtils.loadAnimation(
							GuideActivity.this, R.anim.guide_slide_int_right));
					mRelativeLayout.setVisibility(View.VISIBLE);
					currentViewType++;
				}
			});
		} else {
			showToast("请输入昵称");
			ViewUtils.hideSoftInput(GuideActivity.this,
					mNickNameContentEt.getWindowToken());
		}
	}

	@OnClick(R.id.click_next)
	public void userClickNext() {
		if (mClickNextTv.getText().toString().trim().equals("提交") && !isFlages) {
			mUserInfo.setPeriodYear(mDueYearTv.getText().toString().trim());
			mUserInfo.setPeriodMonth(mDueMonthAbstractWheel.getCurrentItem()
					+ "," + (mDueMonthAbstractWheel.getCurrentItem() + 1));

			mUserInfo.setPeriodDay(mDueDateAbstractWheel.getCurrentItem() + ","
					+ (mDueDateAbstractWheel.getCurrentItem() + 1));
			submitData();
		} else if (mClickNextTv.getText().toString().trim().equals("提交")
				&& isFlages) {
			mUserInfo
					.setDays(mMenstrualCycleAbstractWheel.getCurrentItem()
							+ ","
							+ (mMenstrualCycleAbstractWheel.getCurrentItem() + mMenstrualCycle));
			submitData();
		} else {
			showNextView();
		}
	}

	@OnClick(R.id.due_know_tv)
	public void userDueKnow() {
		isFlages = false;
		mWeightView.setVisibility(View.GONE);
		mEndTimeDueView.setVisibility(View.GONE);
		setViewVisibility(mTipDueKnowLay, mLinearLayout, true);
		mDueView.setVisibility(View.VISIBLE);
		setDueAbstractWheel(mUserInfo.getPeriodMonth(),
				mUserInfo.getPeriodDay(), 1, mDueMonthAbstractWheel,
				mDueDateAbstractWheel);
		mClickNextTv.setText("提交");
		currentViewType++;
	}

	@OnClick(R.id.due_no_know_tv)
	public void userDueNoKnow() {
		isFlages = true;
		mWeightView.setVisibility(View.GONE);
		mDueView.setVisibility(View.GONE);
		setViewVisibility(mTipDueKnowLay, mLinearLayout, true);
		mEndTimeDueView.setVisibility(View.VISIBLE);
		mClickNextTv.setText("下一步");
		setDueAbstractWheel(mUserInfo.getmPeriodMonth(),
				mUserInfo.getmPeriodDay(), 2, mEndTimeDueMonthAbstractWheel,
				mEndTimeDueDateAbstractWheel);
		currentViewType++;
	}

	/**
	 * 数据提交
	 */
	private void submitData() {
		mBlockDialog.show();
		GsonTokenRequest<MineInfo> request = new GsonTokenRequest<MineInfo>(
				com.android.volley.Request.Method.POST, Configs.SERVER_URL
						+ "/user/info", new Listener<JsonResponse<MineInfo>>() {

					@Override
					public void onResponse(JsonResponse<MineInfo> response) {
						switch (response.state) {
						case Configs.UN_USE:
							mBlockDialog.cancel();
							showToast("版本过低请升级新版本");
							break;
						case Configs.FAIL:
							mBlockDialog.cancel();
							showToast(response.msg);
							break;
						case Configs.SUCCESS:
							mBlockDialog.cancel();
							UserDataUtils uDataUtils = new UserDataUtils(
									mActivity);
							uDataUtils.saveData(response.data);
							Intent mIntent = new Intent(mContext,
									MainFragmentActivity.class);
							startActivity(mIntent);
							finish();
							break;
						}
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						mBlockDialog.cancel();
						showToast("网络数据加载失败");
					}
				}) {

			@Override
			public Type getType() {
				Type type = new TypeToken<JsonResponse<MineInfo>>() {
				}.getType();

				return type;
			}
		};
		HttpApi.getSubmitInfoParams(mActivity, "/user/info", request,
				getDataValues());
	}

	/**
	 * 获取用户提交的UserMessage对象
	 * 
	 * @return
	 */
	private UserMessage getDataValues() {
		UserMessage userInfo = new UserMessage();
		if (mUserInfo.getPeriodMonth() != null
				&& !mUserInfo.getPeriodMonth().equals(0)
				&& mUserInfo.getPeriodDay() != null) {
			String dueTime = mUserInfo.getPeriodYear() + "-"
					+ getDataValues(mUserInfo.getPeriodMonth().split(",")[1])
					+ "-"
					+ getDataValues(mUserInfo.getPeriodDay().split(",")[1]);
			userInfo.setDueTime(StringUtils.getDateFromStr(dueTime));
		} else if (mUserInfo.getmPeriodMonth() != null
				&& mUserInfo.getmPeriodDay() != null) {
			String lastPeriod = mUserInfo.getmPeriodYear() + "-"
					+ getDataValues(mUserInfo.getmPeriodMonth().split(",")[1])
					+ "-"
					+ getDataValues(mUserInfo.getmPeriodDay().split(",")[1]);
			userInfo.setmLastPeriod(StringUtils.getDateFromStr(lastPeriod));
			userInfo.setDays(mUserInfo.getDays().split(",")[1]);
		}
		userInfo.setAge(mUserInfo.getAge().split(",")[1]);
		userInfo.setHeight(mHeightLeftStr
				+ getDataValues(mUserInfo.getHeight().split(",")[1]));
		userInfo.setNickName(mUserInfo.getNickName());
		userInfo.setWeight(mUserInfo.getWeight().split(",")[1]);
		return userInfo;
	}

	private String getDataValues(String values) {
		String result = "";
		int number = Integer.valueOf(values);
		if (number <= 9) {
			result = "0" + values;
		} else {
			result = String.valueOf(number);
		}
		return result;
	}

	private void showNextView() {
		if (currentViewType == 1) {
			String mNickName = mNickNameContentEt.getText().toString().trim();
			if (!TextUtils.isEmpty(mNickName)) {
				mUserInfo.setNickName(mNickName);
				ViewUtils.hideSoftInput(GuideActivity.this,
						mNickNameContentEt.getWindowToken());
				initViewData(mUserInfo.getAge(), 105, mAge, 6, 1,
						mAgeAbstractWheel);
				setViewVisibility(mNickNameView, mAgeView, true);
				mRelativeLayout.setVisibility(View.VISIBLE);
				currentViewType++;
			} else {
				showToast("请输入昵称");
				ViewUtils.hideSoftInput(GuideActivity.this,
						mNickNameContentEt.getWindowToken());
			}
		} else if (currentViewType == 2) {
			mUserInfo.setAge(mAgeAbstractWheel.getCurrentItem() + ","
					+ (mAgeAbstractWheel.getCurrentItem() + mAge));

			initViewData(mUserInfo.getHeight(), 99, mHeight, 6, 2,
					mHeightAbstractWheel);
			setViewVisibility(mAgeView, mHeightView, true);
			currentViewType++;

		} else if (currentViewType == 3) {
			mHeightLeftStr = mHeightLeftTv.getText().toString().trim();
			mUserInfo.setHeight(mHeightAbstractWheel.getCurrentItem() + ","
					+ (mHeightAbstractWheel.getCurrentItem() + mHeight));

			initViewData(mUserInfo.getWeight(), 121, mWeight, 6, 3,
					mWeightAbstractWheel);
			setViewVisibility(mHeightView, mWeightView, true);
			currentViewType++;
		} else if (currentViewType == 4) {
			mUserInfo.setWeight(mWeightAbstractWheel.getCurrentItem() + ","
					+ (mWeightAbstractWheel.getCurrentItem() + mWeight));

			setViewVisibility(mLinearLayout, mTipDueKnowLay, true);
			currentViewType++;
		} else if (currentViewType == 6 && !isFlages) {
			mUserInfo.setPeriodYear(mDueYearTv.getText().toString().trim());
			mUserInfo.setPeriodMonth(mDueMonthAbstractWheel.getCurrentItem()
					+ "," + (mDueMonthAbstractWheel.getCurrentItem() + 1));

			mUserInfo.setPeriodDay(mDueDateAbstractWheel.getCurrentItem() + ","
					+ (mDueDateAbstractWheel.getCurrentItem() + 1));

		} else if (isFlages && currentViewType == 6) {// 月经周期
			mUserInfo.setmPeriodYear(mEndTimeDueTv.getText().toString().trim());
			mUserInfo.setmPeriodMonth(mEndTimeDueMonthAbstractWheel
					.getCurrentItem()
					+ ","
					+ (mEndTimeDueMonthAbstractWheel.getCurrentItem() + 1));

			mUserInfo.setmPeriodDay(mEndTimeDueDateAbstractWheel
					.getCurrentItem()
					+ ","
					+ (mEndTimeDueDateAbstractWheel.getCurrentItem() + 1));

			initViewData(mUserInfo.getDays(), 30, mMenstrualCycle, 6, 5,
					mMenstrualCycleAbstractWheel);
			setViewVisibility(mEndTimeDueView, mMenstrualCycleView, true);
			mClickNextTv.setText("提交");
		}
	}

	/**
	 * 日期的显示
	 * 
	 * @param initMonthName
	 * @param initDateName
	 * @param type
	 *            type==1；代表是预产期 type==2；代表末次经期
	 * @param abstractWheel
	 *            月
	 * @param abstractWheel2
	 *            天
	 */
	private void setDueAbstractWheel(String initMonthName, String initDateName,
			final int type, final AbstractWheel abstractWheel,
			AbstractWheel abstractWheel2) {
		int month = 0;
		String[] mStr = new String[12];
		for (int i = 0; i < 12; i++) {
			mStr[i] = String.valueOf(i + 1);
		}
		final ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				this, mStr);
		ampmAdapter.setItemResource(R.layout.common_wheel_item);
		ampmAdapter.setItemTextResource(R.id.common_wheel_item_text);
		abstractWheel.setViewAdapter(ampmAdapter);
		if (initMonthName != null && initMonthName.length() > 0) {
			String[] str = initMonthName.split(",");
			if (str != null) {
				month = Integer.valueOf(str[0]);
			}
		} else {
			month = mMonth;
		}
		changeMonth = month;
		abstractWheel.setCurrentItem(month - 1, false);
		abstractWheel.setCyclic(true);
		abstractWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				CharSequence charSequence = ampmAdapter.getItemText(newValue);
				CharSequence oldcharSequence = ampmAdapter
						.getItemText(oldValue);
				if (charSequence != null && oldcharSequence != null) {
					String valueUp = oldcharSequence.toString();
					String valueCurrent = charSequence.toString();
					changeMonth = Integer.valueOf(valueCurrent);
					if (!TextUtils.isEmpty(valueUp)
							&& !TextUtils.isEmpty(valueCurrent)) {
						if (Integer.valueOf(valueUp) == 12
								&& Integer.valueOf(valueCurrent) == 1) {
							if (type == 1) {
								mDueYearInt = mYear + 1;
								mDueYearTv.setText(String.valueOf(mDueYearInt));
							} else if (type == 2) {
								mEndTimeDueInt = mYear + 1;
								mEndTimeDueTv.setText(String
										.valueOf(mEndTimeDueInt));
							}
						} else if (Integer.valueOf(valueUp) == 1
								&& Integer.valueOf(valueCurrent) == 12) {
							if (type == 1) {
								mDueYearInt = mYear;
								mDueYearTv.setText(String.valueOf(mDueYearInt));
							} else if (type == 2) {
								mEndTimeDueInt = mYear;
								mEndTimeDueTv.setText(String
										.valueOf(mEndTimeDueInt));
							}
						}
					}
					if (type == 1) {
						if (mDueYearInt <= mYear) {
							if (Integer.valueOf(valueCurrent) < mMonth) {
								abstractWheel.setCurrentItem(mMonth - 1, true);
							}
						}
						showChooseDate(mDueYearInt, type,
								mUserInfo.getPeriodDay(), mDueDateAbstractWheel);
					} else if (type == 2) {
						if (Integer.valueOf(valueCurrent) > mMonth) {
							abstractWheel.setCurrentItem(mMonth - 1, true);
						}
						showChooseDate(mEndTimeDueInt, type,
								mUserInfo.getmPeriodDay(),
								mEndTimeDueDateAbstractWheel);
					}
				}
			}
		});
		showChooseDate(mYear, type, initDateName, abstractWheel2);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// 向左手势
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			showNextView();
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			if (currentViewType >= 1) {
				currentViewType--;
				showSetUpView();
			} else {
				currentViewType = 1;
			}
		}
		return false;
	}

	/**
	 * 滑动到上一步
	 */
	private void showSetUpView() {
		if (currentViewType == 1) {
			mEndTimeDueView.setVisibility(View.GONE);
			mRelativeLayout.setVisibility(View.GONE);
			setViewVisibility(mAgeView, mNickNameView, false);
			if (mUserInfo.getNickName() != null) {
				mNickNameContentEt.setText(mUserInfo.getNickName());
			}
			currentViewType = 1;
		} else if (currentViewType == 2) {
			setViewVisibility(mHeightView, mAgeView, false);
			initViewData(mUserInfo.getAge(), 105, mAge, 6, 1, mAgeAbstractWheel);
		} else if (currentViewType == 3) {
			setViewVisibility(mWeightView, mHeightView, false);
			initViewData(mUserInfo.getHeight(), 99, mHeight, 6, 2,
					mHeightAbstractWheel);
		} else if (currentViewType == 4) {
			setViewVisibility(mTipDueKnowLay, mLinearLayout, false);
			initViewData(mUserInfo.getWeight(), 121, mWeight, 6, 3,
					mWeightAbstractWheel);
		} else if (currentViewType == 5) {
			mClickNextTv.setText("下一步");
			setViewVisibility(mLinearLayout, mTipDueKnowLay, false);
		} else if (currentViewType == 6) {
			mClickNextTv.setText("下一步");
			setViewVisibility(mMenstrualCycleView, mEndTimeDueView, false);
		}
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
	
	@Override
	protected void onDestroy() {
	    System.gc();
	    super.onDestroy();
	}

}
