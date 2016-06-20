package com.xiaoaitouch.mom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.LocationBean;

/**
 * 地图地点搜索
 * 
 * @author huxin
 * 
 */
public class SearchPoiAdapter extends BaseAdapter {
	private Context mContext;
	private List<LocationBean> cityPoiList;

	public SearchPoiAdapter(Context context, List<LocationBean> list) {
		this.mContext = context;
		this.cityPoiList = list;
	}

	@Override
	public int getCount() {
		if (cityPoiList != null) {
			return cityPoiList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (cityPoiList != null) {
			return cityPoiList.get(position);
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
					R.layout.map_nearby_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LocationBean cityPoi = cityPoiList.get(position);
		holder.mNearbyName.setText(cityPoi.getLocName());
		holder.mNearbyAddress.setText(cityPoi.getAddStr());
		return convertView;
	}

	static class ViewHolder {
		@Bind(R.id.map_nearby_name)
		TextView mNearbyName;
		@Bind(R.id.map_nearby_address)
		TextView mNearbyAddress;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}

}
