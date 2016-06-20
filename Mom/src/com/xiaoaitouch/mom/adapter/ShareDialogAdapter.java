package com.xiaoaitouch.mom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.xiaoaitouch.mom.R;

public class ShareDialogAdapter extends BaseAdapter {
	private Context mContext;
	private int[] shareImage = { R.drawable.share_qqspace_icon,
			R.drawable.share_qq_icon, R.drawable.share_webo_icon,
			R.drawable.share_weixin_icon, R.drawable.share_people_icon };
	private String[] shareTypeName = { "QQ空间", "QQ", "微博", "微信", "朋友圈" };

	public ShareDialogAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return shareImage.length;
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
					R.layout.share_dialog_adapter_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.shareImage.setImageResource(shareImage[position]);
		holder.shareTypeName.setText(shareTypeName[position]);
		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.share_name_tv)
		TextView shareTypeName;
		@Bind(R.id.share_image)
		ImageView shareImage;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
