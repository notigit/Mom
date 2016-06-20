package com.xiaoaitouch.mom.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import com.baidu.mapapi.search.poi.PoiResult;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.SearchPoiAdapter;
import com.xiaoaitouch.mom.bean.LocationBean;
import com.xiaoaitouch.mom.util.BaiduMapUtilByRacer;
import com.xiaoaitouch.mom.util.Utils;
import com.xiaoaitouch.mom.util.BaiduMapUtilByRacer.PoiSearchListener;

/**
 * 地点搜索
 * 
 * @author huxin
 * 
 */
public class AddressSearchActivity extends Activity {
	@Bind(R.id.map_search_listview)
	ListView mListView;
	@Bind(R.id.map_search_content_et)
	EditText mSearchContentEt;
	
	// 搜索当前城市poi数据源
	private LocationBean mLocationBean;
	private static List<LocationBean> searchPoiList;
	private SearchPoiAdapter mSearchPoiAdapter;

	public static final int SHOW_MAP = 0;
	private static final int SHOW_SEARCH_RESULT = 1;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_search_activity);
		mContext = this;
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
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		mLocationBean = (LocationBean) getIntent().getSerializableExtra(
				"location");
		mSearchContentEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int start, int before,
					int count) {
				if (cs.toString().trim().length() > 0) {
					getPoiByPoiSearch();
				} else {
					if (searchPoiList != null) {
						searchPoiList.clear();
					}
					showMapOrSearch(SHOW_MAP);
					hideSoftinput(mContext);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.performClick();
				hideSoftinput(mContext);
				return false;
			}
		});
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param view
	 */
	private void hideSoftinput(Context mContext) {
		InputMethodManager manager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (manager.isActive()) {
			manager.hideSoftInputFromWindow(mSearchContentEt.getWindowToken(),
					0);
		}
	}

	@OnClick(R.id.cancle_tv)
	public void cancleSearch() {
		hideSoftinput(mContext);
		finish();
	}

	@OnItemClick(R.id.map_search_listview)
	public void OnItemClick(AdapterView<?> parent, int position) {
		LocationBean locationBean = (LocationBean) parent.getAdapter().getItem(
				position);
		Intent data = new Intent();
		data.putExtra("address", locationBean.getAddStr());
		data.putExtra("Lat", locationBean.getLatitude());
		data.putExtra("lon", locationBean.getLongitude());
		setResult(1002, data);
		finish();
	}

	public void getPoiByPoiSearch() {
		if (mLocationBean != null && mLocationBean.getCity() != null) {
			BaiduMapUtilByRacer.getPoiByPoiSearch(mLocationBean.getCity(),
					mSearchContentEt.getText().toString().trim(), 0,
					new PoiSearchListener() {

						@Override
						public void onGetSucceed(
								List<LocationBean> locationList, PoiResult res) {
							if (mSearchContentEt.getText().toString().trim()
									.length() > 0) {
								if (searchPoiList == null) {
									searchPoiList = new ArrayList<LocationBean>();
								}
								searchPoiList.clear();
								searchPoiList.addAll(locationList);
								updateCityPoiListAdapter();
							}
						}

						@Override
						public void onGetFailed() {
							Utils.showToast("抱歉，未能找到结果", Toast.LENGTH_SHORT);
						}
					});
		} else {
			Utils.showToast("定位失败", Toast.LENGTH_SHORT);
		}
	}

	// 刷新当前城市兴趣地点列表界面的adapter
	private void updateCityPoiListAdapter() {
		if (mSearchPoiAdapter == null) {
			mSearchPoiAdapter = new SearchPoiAdapter(mContext, searchPoiList);
			mListView.setAdapter(mSearchPoiAdapter);
		} else {
			mSearchPoiAdapter.notifyDataSetChanged();
		}
		showMapOrSearch(SHOW_SEARCH_RESULT);
	}

	// 显示地图界面亦或搜索结果界面
	private void showMapOrSearch(int index) {
		if (index == SHOW_SEARCH_RESULT) {
		} else {
			if (searchPoiList != null) {
				searchPoiList.clear();
			}
		}
	}
}
