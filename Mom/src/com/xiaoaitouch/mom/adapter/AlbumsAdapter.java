package com.xiaoaitouch.mom.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.PhotoUpImageBucket;

/**
 * 相册分组列表
 * 
 * @author huxin
 * 
 */
public class AlbumsAdapter extends BaseAdapter {
	private List<PhotoUpImageBucket> arrayList;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public AlbumsAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
		arrayList = new ArrayList<PhotoUpImageBucket>();// 初始化集合
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCacheExtraOptions(96, 120).build();
		imageLoader.init(config);

		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)
				// 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); // 创建配置过的DisplayImageOption对象

	};

	public void setArrayList(List<PhotoUpImageBucket> arrayList) {
		this.arrayList = arrayList;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.album_images_item,
					parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PhotoUpImageBucket upImageBucket = arrayList.get(position);
		holder.mGalleryNuber.setText("" + upImageBucket.getCount());
		holder.mGalleryName.setText(upImageBucket.getBucketName());

		imageLoader.displayImage("file://"
				+ upImageBucket.getImageList().get(0).getImagePath(),
				holder.mGalleryImage, options);
		return convertView;
	}

	static class ViewHolder {
		@Bind(R.id.gallery_image)
		ImageView mGalleryImage;
		@Bind(R.id.gallery_name_tv)
		TextView mGalleryName;
		@Bind(R.id.gallery_number_tv)
		TextView mGalleryNuber;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
