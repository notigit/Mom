package com.xiaoaitouch.mom.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.AlbumItemAdapter;
import com.xiaoaitouch.mom.adapter.AlbumsAdapter;
import com.xiaoaitouch.mom.bean.PhotoUpImageBucket;
import com.xiaoaitouch.mom.bean.PhotoUpImageItem;
import com.xiaoaitouch.mom.util.PhotoUpAlbumHelper;
import com.xiaoaitouch.mom.util.Utils;
import com.xiaoaitouch.mom.util.PhotoUpAlbumHelper.GetAlbumList;

/**
 * 相册的分类管理
 * 
 * @author huxin
 * 
 */
public class GalleryTypeActivity extends Activity implements OnClickListener {
	@Bind(R.id.gallery_type_header_lay)
	LinearLayout mLinearLayout;
	@Bind(R.id.gallery_type_gridview)
	GridView mGridView;
	@Bind(R.id.gallery_type_list_image)
	ImageView mGalleryTypeList;
	@Bind(R.id.gallery_view_one_lay)
	LinearLayout mGalleryTyViewOne;
	@Bind(R.id.gallery_view_two_lay)
	LinearLayout mGalleryTyViewTwo;
	@Bind(R.id.gallery_type_listview)
	ListView mListView;

	// 获取手机相册数据
	private PhotoUpAlbumHelper photoUpAlbumHelper;
	private List<PhotoUpImageBucket> mImageBuckets;
	// 分类下面的图片
	private AlbumItemAdapter mAlbumItemAdapter;
	// 分类listview展示
	private AlbumsAdapter mAlbumsAdapter;
	// 存放相册分类的TextView
	private Map<Integer, TextView> map = new HashMap<Integer, TextView>();
	protected Activity mActivity;

	private List<PhotoUpImageItem> mPhotoUpImageItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_type_activity);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.height = (int) (display.getHeight() - 40);
		this.getWindow().setAttributes(lp);
		mActivity = this;
		ButterKnife.bind(this);
		initData();
	}

	@OnClick(R.id.back_tv)
	public void onBack() {
		finish();
	}

	private void initData() {
		photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();
		photoUpAlbumHelper.init(mActivity);
		photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
			@Override
			public void getAlbumList(List<PhotoUpImageBucket> list) {
				mImageBuckets = list;
				if (mImageBuckets != null && mImageBuckets.size() > 0) {
					setViewData();
				} else {
					Utils.showToast("您手机当前没有照片", Toast.LENGTH_SHORT);
				}
			}
		});
		photoUpAlbumHelper.execute(false);
	}

	private void setViewData() {
		int size = mImageBuckets.size();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int with = dm.widthPixels / 4;
		for (int i = 0; i < size; i++) {
			View headerView = LayoutInflater.from(this).inflate(
					R.layout.horizontal_item, null);
			TextView mTextView = (TextView) headerView
					.findViewById(R.id.horizontal_type_tv);
			if (i == 0) {
				mTextView.setSelected(true);
				mTextView.setFocusable(true);
			}
			map.put(i, mTextView);
			mTextView.setText(mImageBuckets.get(i).getBucketName());
			mTextView.setTag(i);
			mTextView.setOnClickListener(this);
			mLinearLayout.addView(headerView);
		}
		mPhotoUpImageItems = mImageBuckets.get(0).getImageList();
		mAlbumItemAdapter = new AlbumItemAdapter(mPhotoUpImageItems, with,
				mActivity);
		mGridView.setAdapter(mAlbumItemAdapter);
		mAlbumItemAdapter.notifyDataSetChanged();

		mAlbumsAdapter = new AlbumsAdapter(mActivity);
		mAlbumsAdapter.setArrayList(mImageBuckets);
		mListView.setAdapter(mAlbumsAdapter);
	}

	@OnClick({ R.id.gallery_type_list_image, R.id.gallery_type_list_images })
	public void showGallery() {
		if (mGalleryTyViewOne.getVisibility() == View.VISIBLE) {
			mGalleryTyViewOne.setVisibility(View.GONE);
			mGalleryTyViewTwo.setVisibility(View.VISIBLE);
		} else {
			mGalleryTyViewTwo.setVisibility(View.GONE);
			mGalleryTyViewOne.setVisibility(View.VISIBLE);
		}
	}

	@OnItemClick(R.id.gallery_type_listview)
	public void OnItemClickListview(AdapterView<?> parent, int position) {
		mGalleryTyViewTwo.setVisibility(View.GONE);
		mGalleryTyViewOne.setVisibility(View.VISIBLE);
		currentView(position);
	}

	@OnItemClick(R.id.gallery_type_gridview)
	public void OnItemClickGridview(AdapterView<?> parent, int position) {
		PhotoUpImageItem mImageItem = (PhotoUpImageItem) parent.getAdapter()
				.getItem(position);
		Intent data = new Intent();
		data.putExtra("imagePath", mImageItem.getImagePath());
		setResult(1005, data);
		finish();
	}

	public void currentView(int position) {
		for (int i = 0; i < map.size(); i++) {
			if (position == i) {
				((TextView) map.get(position)).setSelected(true);
				((TextView) map.get(position)).setFocusable(true);
			} else {
				((TextView) map.get(i)).setSelected(false);
				((TextView) map.get(i)).setFocusable(false);
			}
		}

		if (mAlbumItemAdapter != null) {
			mAlbumItemAdapter.setArrayList(mImageBuckets.get(position)
					.getImageList());
			mAlbumItemAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.horizontal_type_tv:
			currentView((Integer) v.getTag());
			break;

		default:
			break;
		}
	}
}
