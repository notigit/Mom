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

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.dao.MSymptomModel;

public class SymptomAdapter extends BaseAdapter {
	private Context mContext;
	private List<MSymptomModel> mSymptomBeans;
	private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
	private boolean mIsFlage;

	/**
	 * 
	 * @param context
	 * @param str
	 * @param isFlage
	 *            isFlage =false 代表引导页 isFlage=true 代表修改
	 */
	public SymptomAdapter(Context context, List<MSymptomModel> mStrList,
			boolean isflage) {
		this.mIsFlage = isflage;
		this.mContext = context;
		this.mSymptomBeans = mStrList;
	}

	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (!mIsFlage) {
			if (mSymptomBeans != null) {
				return mSymptomBeans.size();
			} else {
				return 0;
			}
		} else
			return mSymptomBeans.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return mSymptomBeans.get(position);
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
					R.layout.symptom_view_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!mIsFlage) {
			holder.deleteMark.setVisibility(isShowDelete ? View.VISIBLE
					: View.GONE);// 设置删除按钮是否显示
			holder.symptomRbt.setText(mSymptomBeans.get(position).getSymptom());
			holder.symptomRbt.setTextColor(mContext.getResources().getColor(
					R.color.white));
		} else {
			if (position == mSymptomBeans.size()) {
				holder.symptomRbt.setText("+");
				holder.deleteMark.setVisibility(View.GONE);
			} else {
				holder.deleteMark.setVisibility(isShowDelete ? View.VISIBLE
						: View.GONE);// 设置删除按钮是否显示
				holder.symptomRbt.setText(mSymptomBeans.get(position)
						.getSymptom());
			}
			holder.symptomRbt.setTextColor(mContext.getResources().getColor(
					R.color.white));
		}
		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.symptom_rbt)
		TextView symptomRbt;
		@Bind(R.id.delete_markView)
		ImageView deleteMark;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
