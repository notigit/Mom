package com.xiaoaitouch.mom.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.PhotoUpImageItem;

/**
 * 相册-分组-的图片
 * 
 * @author huxin
 * 
 */
public class AlbumItemAdapter extends BaseAdapter {
	private int mWith = 0;
	private List<PhotoUpImageItem> list;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public AlbumItemAdapter(List<PhotoUpImageItem> list, int with,
			Context context) {
		this.list = list;
		this.mWith = with;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Config.ARGB_8888)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); // 创建配置过的DisplayImageOption对象
	}

	public void setArrayList(List<PhotoUpImageItem> arrayList) {
		this.list = arrayList;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.gallery_image_item,
					parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				mWith, mWith);
		holder.mGalleryImage.setLayoutParams(layoutParams);
		imageLoader.displayImage("file://" + list.get(position).getImagePath(),
				holder.mGalleryImage, options);
		return convertView;
	}

	static class ViewHolder {
		@Bind(R.id.gallery_image)
		ImageView mGalleryImage;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
