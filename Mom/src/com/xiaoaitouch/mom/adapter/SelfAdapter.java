package com.xiaoaitouch.mom.adapter;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.dao.MscModel;

/**
 * 自查对话框
 * 
 * @author huxin
 * 
 */
public class SelfAdapter extends BaseAdapter {
	private Context mContext;
	private Map<Integer, MscModel> map;
	private boolean mIsFlage;

	public SelfAdapter(Context context, Map<Integer, MscModel> list,
			boolean isflage) {
		this.mContext = context;
		this.map = list;
		this.mIsFlage = isflage;
	}

	@Override
	public int getCount() {
		if (map != null) {
			return map.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (map != null) {
			return map.get(position);
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
					R.layout.main_self_dialog_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MscModel mscBean = map.get(position);
		holder.mMainSelfTv.setText(mscBean.getMessage());
		if (mscBean.getIsOk() == 1) {
			holder.mMainSelfImage
					.setImageResource(R.drawable.check_question_icon_selected);
		}
		if (mIsFlage) {
			setCurrentView(holder.mSelfRay, holder.mMainSelfImage, position,
					mscBean);
		}
		return convertView;
	}

	public void setCurrentView(RelativeLayout mRelativeLayout,
			final ImageView mMainSelfImage, final int position,
			final MscModel mscBean) {
		mRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mscBean.getIsOk() == 1) {
					mscBean.setIsOk(0);
					Animation animation = AnimationUtils.loadAnimation(
							mContext, R.anim.main_self_cancle_scale);
					mMainSelfImage.startAnimation(animation);
					animation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							mMainSelfImage
									.setImageResource(R.color.transparent);
						}
					});
				} else {
					mscBean.setIsOk(1);
					mMainSelfImage
							.setImageResource(R.drawable.check_question_icon_selected);
					mMainSelfImage.startAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.main_self_select_scale));
				}
				map.put(position, mscBean);
				notifyDataSetChanged();
			}
		});
	}

	static class ViewHolder {
		@Bind(R.id.self_dialog_tv)
		TextView mMainSelfTv;
		@Bind(R.id.self_image)
		ImageView mMainSelfImage;
		@Bind(R.id.self_ray)
		RelativeLayout mSelfRay;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}

}
