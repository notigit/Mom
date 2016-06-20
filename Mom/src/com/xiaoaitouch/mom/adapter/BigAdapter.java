package com.xiaoaitouch.mom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.xiaoaitouch.mom.R;

/**
 * 大事件
 * 
 * @author huxin
 * 
 */
public class BigAdapter extends BaseAdapter {
	private Context mContext;
	private String[] bigTypeName = { "我怀孕了", "第一次胎动", "第一次产检", "我的大卡", "我的小卡",
			"自定义" };

	public BigAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return bigTypeName.length;
	}

	@Override
	public Object getItem(int position) {
		return getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.big_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.bigTypeName.setText(bigTypeName[position]);
		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.big_name_tv)
		TextView bigTypeName;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
