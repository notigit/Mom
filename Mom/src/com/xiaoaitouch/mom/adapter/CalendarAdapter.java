package com.xiaoaitouch.mom.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.NewCalendar;
import com.xiaoaitouch.mom.bean.CalendarBean.Calendars;
import com.xiaoaitouch.mom.main.CalendarActivity;

/**
 * 日历
 * 
 * @author huxin
 * 
 */
public class CalendarAdapter extends BaseAdapter {
	private List<NewCalendar> mCalendars;
	private List<Calendars> mCalendarBeans = null;
	private CalendarActivity mCalendarActivity;

	public CalendarAdapter(CalendarActivity activity) {
		this.mCalendarActivity = activity;
	}

	public void setCalendars(List<NewCalendar> newCalendars,
			List<Calendars> calendars) {
		this.mCalendars = newCalendars;
		this.mCalendarBeans = calendars;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mCalendars != null) {
			return mCalendars.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mCalendars != null) {
			return mCalendars.get(position);
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
					R.layout.calendar_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NewCalendar nCalendar = mCalendars.get(position);
		holder.pregnantWeekTv.setText("孕" + nCalendar.getWeek() + "周");
		holder.weekDateTv.setText(nCalendar.getTimeSlot());
		CalendarRowAdapter mAdapter = new CalendarRowAdapter(nCalendar,
				mCalendarActivity);
		mAdapter.setCalendars(mCalendarBeans);
		holder.calendarGridview.setAdapter(mAdapter);
		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.pregnant_week_tv)
		TextView pregnantWeekTv;
		@Bind(R.id.week_date_tv)
		TextView weekDateTv;
		@Bind(R.id.calendar_gridview)
		GridView calendarGridview;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
