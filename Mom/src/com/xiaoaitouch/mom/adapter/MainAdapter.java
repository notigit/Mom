package com.xiaoaitouch.mom.adapter;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.graphics.Bitmap.Config;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.MainData;
import com.xiaoaitouch.mom.bean.ResultObj;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.HoroscopeModel;
import com.xiaoaitouch.mom.dao.ShareCardModel;
import com.xiaoaitouch.mom.dao.SportCardModel;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.StringRequest;
import com.xiaoaitouch.mom.util.ActionSheetDialog;
import com.xiaoaitouch.mom.util.CardShareDialog;
import com.xiaoaitouch.mom.util.MainDatabase;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.util.Utils;

/**
 * 主界面
 * 
 * @author huxin
 * 
 */
public class MainAdapter extends BaseAdapter {

	private Activity mActivity;
	private List<MainData> mainDatasList;
	private String[] mHoroscopeStr;
	private int[] mHoroscopeImage = new int[] { R.drawable.xz_1,
			R.drawable.xz_2, R.drawable.xz_3, R.drawable.xz_4, R.drawable.xz_5,
			R.drawable.xz_6, R.drawable.xz_7, R.drawable.xz_8, R.drawable.xz_9,
			R.drawable.xz_10, R.drawable.xz_11, R.drawable.xz_12 };

	private String sportInfo = "运动卡片";
	private View view = null;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private UserInfo mUserInfo;
	private String mCurrentTime;
	private int mDueDays = 1;
	private boolean mIsCurrentWeek = false;// 是否是当前周

	public void setSportInfo(String sportInfo, String time) {
		this.sportInfo = sportInfo;
		this.mCurrentTime = time;
		if (view != null) {
			updateSportInfo(view);
		}
	}

	private void updateSportInfo(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder.getPosition() == 0) {
			holder.mMainWeatherViewItem.setVisibility(View.VISIBLE);
			holder.mMainWeatherDegreeTv.setText(Html.fromHtml(sportInfo));
			holder.mMainWeatherTimeTv.setText(mCurrentTime == null ? ""
					: mCurrentTime);
			String dueDay = mDueDays + "天";
			Spannable spanColor = new SpannableString(dueDay);
			AbsoluteSizeSpan span = new AbsoluteSizeSpan(40);
			spanColor.setSpan(span, 0, spanColor.length() - 1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.mMainDueDayTv.setText(spanColor);

		} else {
			holder.mMainWeatherViewItem.setVisibility(View.GONE);
		}
	}

	private String getStyleString() {
		String string = "";
		String weather = "晴天";
		String temp = 22 + "℃";
		String aqi = "良" + "<br/>";
		String runTime = "已经走了<font color = \"#ff7999\">" + 15 + "</font>分钟,";
		String wholeStep = "共计<font color = \"#ff7999\">" + 1156
				+ "</font>步了哦。<br/>";
		String suggestTime = "建议步行<font color = \"#ff7999\">" + 30
				+ "</font>分钟。";
		string = weather + temp + aqi + runTime + wholeStep + suggestTime;
		return string;
	}

	public MainAdapter(Activity activity, UserInfo userInfo) {
		this.mUserInfo = userInfo;
		this.mHoroscopeStr = activity.getResources().getStringArray(
				R.array.main_horoscope_name);
		this.mActivity = activity;
		this.imageLoader = ImageLoader.getInstance();
		this.options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Config.ARGB_8888)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
	}

	public void setData(List<MainData> mainDatas, boolean isflage) {
		this.mIsCurrentWeek = isflage;
		this.mainDatasList = mainDatas;
		this.notifyDataSetChanged();
	}

	public void addCardData(MainData mainData) {
		mainDatasList.add(mainData);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mainDatasList != null) {
			return mainDatasList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mainDatasList != null) {
			return mainDatasList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.main_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setPosition(position);
		setViewData(holder, position);
		// if (mIsCurrentWeek) {
		// if (position == 0) {
		// view = convertView;
		// MainData mainData = mainDatasList.get(position);
		// if (mainData.getSi() != null) {
		// mDueDays = mainData.getSi().getDueDays();
		// } else if (mainData.getFc() != null) {
		// mDueDays = mainData.getFc().getDueDays();
		// } else if (mainData.getSc() != null) {
		// mDueDays = mainData.getSc().getDueDays();
		// }
		// updateSportInfo(convertView);
		// }
		// }
		return convertView;
	}

	public class ViewHolder {
		/*********************** 日期 ******************************/
		@Bind(R.id.main_date_view)
		View mDateViewItem;
		@Bind(R.id.main_date_tv)
		TextView mMainDateTv;

		/********************** 天气 卡片 **************************/
		@Bind(R.id.main_weather_view)
		View mMainWeatherViewItem;
		@Bind(R.id.main_due_day_tv)
		TextView mMainDueDayTv;
		@Bind(R.id.main_weather_time_tv)
		TextView mMainWeatherTimeTv;
		@Bind(R.id.main_weather_degree_tv)
		TextView mMainWeatherDegreeTv;
		@Bind(R.id.main_weather_image)
		ImageView mMainWeatherImage;
		/********************** 分享卡片 *************************/
		@Bind(R.id.main_share_view)
		View mMainShareViewItem;
		@Bind(R.id.share_card_image)
		ImageView mShareCardImage;
		@Bind(R.id.share_due_day_tv)
		TextView mShareDueDayTv;
		@Bind(R.id.share_time_tv)
		TextView mShareTimeTv;
		@Bind(R.id.share_entrance_image)
		ImageView mShareImage;
		@Bind(R.id.main_share_content_tv)
		TextView mShareContentTv;
		@Bind(R.id.main_share_address_tv)
		TextView mShareAddressTv;
		@Bind(R.id.share_mood_image)
		ImageView mShareMoodImage;
		/********************** 星座卡片 **************************/
		@Bind(R.id.main_horoscope_view)
		View mMainHoroscopeViewItem;
		@Bind(R.id.babay_horoscope_tv)
		TextView mMainHoroscopeNameTv;
		@Bind(R.id.main_horoscope_day_tv)
		TextView mMainHoroscopeDayTv;
		@Bind(R.id.main_horoscope_time_tv)
		TextView mMainHoroscopeTimeTv;
		@Bind(R.id.main_horoscope_one_image)
		ImageView mMainHoroscopeOneImage;
		@Bind(R.id.main_horoscope_two_image)
		ImageView mMainHoroscopeTwoImage;
		@Bind(R.id.main_horoscope_three_image)
		ImageView mMainHoroscopeThreeImage;
		@Bind(R.id.main_horoscope_four_image)
		ImageView mMainHoroscopeFourImage;
		@Bind(R.id.main_horoscope_five_image)
		ImageView mMainHoroscopeFiveImage;
		@Bind(R.id.main_horoscope_details_tv)
		TextView mMainHoroscopeDetailsTv;
		@Bind(R.id.main_horoscope_image)
		ImageView mMainHoroscopeImage;

		private int position;

		public void setPosition(int position) {
			this.position = position;
		}

		public int getPosition() {
			return position;
		}

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public void setViewData(ViewHolder holder, int position) {
		MainData mainData = mainDatasList.get(position);
		String dateStr = "";
		int type = mainData.getType();
		switch (type) {
		case 1:
			holder.mMainWeatherViewItem.setVisibility(View.VISIBLE);
			holder.mMainShareViewItem.setVisibility(View.GONE);
			holder.mMainHoroscopeViewItem.setVisibility(View.GONE);
			dateStr = mainData.getSi().getDate();
			setSportView(holder, mainData);
			break;
		case 2:
			holder.mMainShareViewItem.setVisibility(View.VISIBLE);
			holder.mMainWeatherViewItem.setVisibility(View.GONE);
			holder.mMainHoroscopeViewItem.setVisibility(View.GONE);
			dateStr = mainData.getFc().getDate();
			setShareView(holder, mainData);
			break;
		case 3:
			holder.mMainHoroscopeViewItem.setVisibility(View.VISIBLE);
			holder.mMainWeatherViewItem.setVisibility(View.GONE);
			holder.mMainShareViewItem.setVisibility(View.GONE);
			dateStr = mainData.getSc().getDate();
			setHoroscopeView(holder, mainData);
			break;

		default:
			break;
		}
		if (position == 0) {
			holder.mDateViewItem.setVisibility(View.VISIBLE);
			holder.mMainDateTv.setText(dateStr);
		} else {
			if (mainData.getIndex() == mainDatasList.get(position - 1)
					.getIndex()) {
				holder.mDateViewItem.setVisibility(View.GONE);
			} else {
				holder.mDateViewItem.setVisibility(View.VISIBLE);
				holder.mMainDateTv.setText(dateStr);
			}
		}
	}

	/**
	 * 运动卡片
	 * 
	 * @param holder
	 * @param mainData
	 */
	private void setSportView(ViewHolder holder, MainData mainData) {
		SportCardModel sCardBean = mainData.getSi();
		if (sCardBean != null) {
			String dueDay = sCardBean.getDueDays() + "天";
			Spannable spanColor = new SpannableString(dueDay);
			AbsoluteSizeSpan span = new AbsoluteSizeSpan(40);
			spanColor.setSpan(span, 0, spanColor.length() - 1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.mMainDueDayTv.setText(spanColor);

			holder.mMainWeatherDegreeTv.setText(Html.fromHtml(sCardBean
					.getSportMessage()));
			String mdate = sCardBean.getDate();
			holder.mMainWeatherTimeTv
					.setText(mdate.substring(5, mdate.length()));
		}

	}

	/**
	 * 分享卡片
	 * 
	 * @param holder
	 * @param mainData
	 */
	private void setShareView(ViewHolder holder, final MainData mainData) {
		ShareCardModel sCardBean = mainData.getFc();
		if (sCardBean != null) {
			String dueDay = sCardBean.getDueDays() + "天";
			Spannable spanColor = new SpannableString(dueDay);
			AbsoluteSizeSpan span = new AbsoluteSizeSpan(40);
			spanColor.setSpan(span, 0, spanColor.length() - 1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.mShareDueDayTv.setText(spanColor);
			holder.mShareContentTv.setText(sCardBean.getMessage());
			holder.mShareAddressTv.setText(sCardBean.getLocation());
			holder.mMainDateTv.setText(sCardBean.getDate());
			holder.mShareTimeTv.setText(StringUtils.convertDate(
					sCardBean.getCreateTime()).substring(10, 16));
			String imageUrl = sCardBean.getImg();
			if (!TextUtils.isEmpty(imageUrl)) {
				holder.mShareCardImage.setVisibility(View.VISIBLE);
				imageLoader.displayImage(Configs.IMAGE_URL + imageUrl,
						holder.mShareCardImage, options);
			} else {
				holder.mShareCardImage.setVisibility(View.GONE);
			}
			int feeling = Integer.valueOf(sCardBean.getFeeling());
			showMoodView(holder.mShareMoodImage, feeling);
			holder.mShareImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openShareDialog(mainData);
				}
			});
		}
	}

	/**
	 * 显示心情图标
	 * 
	 * @param holder
	 * @param feeling
	 */
	private void showMoodView(ImageView mShareMoodImage, int feeling) {
		switch (feeling) {
		case 1:
			mShareMoodImage.setImageResource(R.drawable.bq_1);
			break;
		case 2:
			mShareMoodImage.setImageResource(R.drawable.bq_2);
			break;
		case 3:
			mShareMoodImage.setImageResource(R.drawable.bq_3);
			break;
		case 4:
			mShareMoodImage.setImageResource(R.drawable.bq_4);
			break;
		case 5:
			mShareMoodImage.setImageResource(R.drawable.bq_5);
			break;
		case 6:
			mShareMoodImage.setImageResource(R.drawable.bq_6);
			break;
		default:
			break;
		}
	}

	/**
	 * 星座卡片
	 * 
	 * @param holder
	 * @param mainData
	 */
	private void setHoroscopeView(ViewHolder holder, MainData mainData) {
		HoroscopeModel hspCardBean = mainData.getSc();
		if (hspCardBean != null) {
			String dueDay = hspCardBean.getDueDays() + "天";
			Spannable spanColor = new SpannableString(dueDay);
			AbsoluteSizeSpan span = new AbsoluteSizeSpan(40);
			spanColor.setSpan(span, 0, spanColor.length() - 1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.mMainHoroscopeDayTv.setText(spanColor);

			String mdate = hspCardBean.getDate();
			holder.mMainHoroscopeTimeTv.setText(mdate.substring(5,
					mdate.length()));
			holder.mMainHoroscopeDetailsTv.setText(hspCardBean.getMessage());
			int horoscope = hspCardBean.getHoroscope();
			holder.mMainHoroscopeNameTv.setText(mHoroscopeStr[horoscope - 1]);
			holder.mMainHoroscopeImage
					.setImageResource(mHoroscopeImage[horoscope - 1]);

			int stars = hspCardBean.getStars();
			switch (stars) {
			case 5:
				holder.mMainHoroscopeFiveImage.setVisibility(View.VISIBLE);
			case 4:
				holder.mMainHoroscopeFourImage.setVisibility(View.VISIBLE);
			case 3:
				holder.mMainHoroscopeThreeImage.setVisibility(View.VISIBLE);
			case 2:
				holder.mMainHoroscopeTwoImage.setVisibility(View.VISIBLE);
			case 1:
				holder.mMainHoroscopeOneImage.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 打开分享对话框
	 */
	public void openShareDialog(final MainData mainData) {
		final ActionSheetDialog mSheetDialog = new ActionSheetDialog(mActivity);
		View shareView = LayoutInflater.from(mActivity).inflate(
				R.layout.share_buttom_item, null);
		shareView.findViewById(R.id.dialog_share_tv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mSheetDialog.cancel();
						CardShareDialog mDialog = new CardShareDialog(mActivity);
						mDialog.cardShareDialog(mainData);
					}
				});
		shareView.findViewById(R.id.dialog_delet_tv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mSheetDialog.cancel();
						deleteCard(mainData);
					}
				});
		shareView.findViewById(R.id.dialog_cancle_lay).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mSheetDialog.cancel();
					}
				});
		mSheetDialog.setContentView(shareView);
		mSheetDialog.show();
	}

	/**
	 * 删除卡片
	 */
	private void deleteCard(final MainData mainData) {
		StringRequest request = new StringRequest(Method.POST,
				Configs.SERVER_URL + "/user/mom/delete/"
						+ mainData.getFc().getCardId(), new Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							ResultObj result = new ResultObj(response);
							switch (result.getState()) {
							case ResultObj.FAIL:
								showToast(result.getMessage());
								break;
							case ResultObj.SUCCESS:
								MainDatabase.instance(mActivity)
										.deleteMainShareCard(
												mUserInfo.getUserId(),
												mainData.getFc()
														.getCreateTime());
								mainDatasList.remove(mainData);
								notifyDataSetChanged();
								break;
							case ResultObj.UN_USE:
								showToast("版本过低请升级新版本");
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		HttpApi.deleteCard(mActivity, "/user/mom/delete/"
				+ mainData.getFc().getCardId(), request, null);
	}

	private void showToast(CharSequence text) {
		Utils.showToast(text, Toast.LENGTH_SHORT);
	}

}
