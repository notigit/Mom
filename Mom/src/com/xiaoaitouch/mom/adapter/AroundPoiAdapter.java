package com.xiaoaitouch.mom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xiaoaitouch.mom.R;

/**
 * 地图周边热点
 * 
 * @author huxin
 * 
 */
public class AroundPoiAdapter extends BaseAdapter {
	private Context mContext;
	private List<PoiInfo> mkPoiInfoList;
	private int selected = -1;

	public AroundPoiAdapter(Context context, List<PoiInfo> list) {
		this.mContext = context;
		this.mkPoiInfoList = list;
	}

	public void setNewList(List<PoiInfo> list, int index) {
		this.mkPoiInfoList = list;
		this.selected = index;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mkPoiInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return mkPoiInfoList.get(position);
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
		holder.mNearbyName.setText(mkPoiInfoList.get(position).name);
		holder.mNearbyAddress.setText(mkPoiInfoList.get(position).address);
		if (selected == position) {
			holder.mSelectImage.setVisibility(View.VISIBLE);
		} else {
			holder.mSelectImage.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	static class ViewHolder {
		@Bind(R.id.map_nearby_name)
		TextView mNearbyName;
		@Bind(R.id.map_nearby_address)
		TextView mNearbyAddress;
		@Bind(R.id.map_select_address_image)
		ImageView mSelectImage;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}
}
