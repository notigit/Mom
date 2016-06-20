package com.xiaoaitouch.mom.adapter;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.CalendarBean.Calendars;
import com.xiaoaitouch.mom.bean.DayDate;
import com.xiaoaitouch.mom.bean.NewCalendar;
import com.xiaoaitouch.mom.main.CalendarActivity;
import com.xiaoaitouch.mom.util.SharedPreferencesUtil;
import com.xiaoaitouch.mom.util.StringUtils;

/**
 * 日历每行
 * 
 * @author huxin
 * 
 */
public class CalendarRowAdapter extends BaseAdapter {

	private String[] str = { "日", "一", "二", "三", "四", "五", "六" };
	private NewCalendar nCalendar;
	private CalendarActivity mCalendarActivity;
	private List<DayDate> mDayDates;// 每周对应的日期
	private List<Calendars> mCalendarBeans = null;

	public CalendarRowAdapter(NewCalendar newCalendar, CalendarActivity activity) {
		this.nCalendar = newCalendar;
		this.mCalendarActivity = activity;
		this.mDayDates = nCalendar.getmDayDates();
	}

	public void setCalendars(List<Calendars> calendars) {
		this.mCalendarBeans = calendars;
	}

	@Override
	public int getCount() {
		if (mDayDates != null) {
			return mDayDates.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mDayDates != null) {
			return mDayDates.get(position);
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
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.calendar_gradview_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.calendarWeekTv.setText(str[position]);

		final DayDate dayDate = mDayDates.get(position);
		if (StringUtils.compareDate(dayDate.getDayDate(),
				nCalendar.getCurrentDate()) <= 0) {
			// 代表当天有数据
			if (isDayData(dayDate.getDayDate())) {
				holder.isDataImage.setVisibility(View.VISIBLE);
			}
			// 代表用户当前选择的日期
			if (dayDate.getDayDate().equals(nCalendar.getSelectDate())) {
				holder.calendarContentBg.setVisibility(View.VISIBLE);
				holder.calendarWeekTv.setTextColor(mCalendarActivity
						.getResources().getColor(R.color.calendar_word_color));
			}
			// 代表当天位置
			if (dayDate.getDayDate().equals(nCalendar.getCurrentDate())) {
				holder.calendarDayTv.setVisibility(View.VISIBLE);
			}
			holder.mRelativeLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SharedPreferencesUtil.putString(mCalendarActivity,
							"user_select_date", dayDate.getDayDate());
					
					Intent mIntent = new Intent();
					mIntent.putExtra("date", dayDate.getDayDate());
					mCalendarActivity.setResult(1012, mIntent);
					mCalendarActivity.finish();
				}
			});

			if (StringUtils.compareDate(dayDate.getDayDate(),
					nCalendar.getDueDate()) < 0) {
				holder.mRelativeLayout.setClickable(false);
				holder.isDataImage.setVisibility(View.INVISIBLE);
			}
		} else {
			holder.mRelativeLayout.setClickable(false);
			holder.isDataImage.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private boolean isDayData(String day) {
		if (mCalendarBeans != null && mCalendarBeans.size() > 0) {
			for (int i = 0; i < mCalendarBeans.size(); i++) {
				if (day.equals(mCalendarBeans.get(i).getDate())) {
					return true;
				}
			}
		}
		return false;
	}

	static class ViewHolder {
		@Bind(R.id.calendar_content_bg_iv)
		ImageView calendarContentBg;
		@Bind(R.id.calendar_week_tv)
		TextView calendarWeekTv;
		@Bind(R.id.calendar_day_tv)
		TextView calendarDayTv;
		@Bind(R.id.calendar_is_data_image)
		ImageView isDataImage;
		@Bind(R.id.calendar_ray)
		RelativeLayout mRelativeLayout;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}
}
