package com.xiaoaitouch.mom.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.AroundPoiAdapter;
import com.xiaoaitouch.mom.bean.LocationBean;
import com.xiaoaitouch.mom.util.BaiduMapUtilByRacer;
import com.xiaoaitouch.mom.util.BaiduMapUtilByRacer.GeoCodePoiListener;
import com.xiaoaitouch.mom.util.BaiduMapUtilByRacer.LocateListener;
import com.xiaoaitouch.mom.util.Utils;

public class MapActivity extends Activity {
	@Bind(R.id.mapview)
	MapView mMapView;
	@Bind(R.id.map_listview)
	ListView mListView;
	BaiduMap mBaiduMap;
	private UiSettings mUiSettings;
	private AroundPoiAdapter mAroundPoiAdapter;
	private LocationBean mLocationBean;
	// 定位poi地名信息数据源
	private List<PoiInfo> aroundPoiList;

	private Marker mMarker = null;
	private boolean isCanUpdateMap = true;
	private Context mContext;
	private double mlat;// 经度
	private double mlng;// 维度
	private String mAddress;// 地名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
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

	@OnClick(R.id.activity_left_tv)
	public void onBack() {
		finish();
	}

	@OnClick(R.id.activity_top_right_tv)
	public void saveData() {
		Intent data = new Intent();
		data.putExtra("address", mAddress);
		data.putExtra("Lat", mlat);
		data.putExtra("lon", mlng);
		setResult(1002, data);
		finish();
	}

	@OnClick(R.id.map_search_ray)
	public void openAddressSearchActivity() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("location", mLocationBean);
		Intent mIntent = new Intent(this, AddressSearchActivity.class);
		mIntent.putExtras(bundle);
		startActivityForResult(mIntent, 1001);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				finish();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private void initView() {
		mMapView.showScaleControl(false);// 去掉比例尺
		BaiduMapUtilByRacer.goneMapViewChild(mMapView, true, true);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
		mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
		mUiSettings = mBaiduMap.getUiSettings();
		mUiSettings.setCompassEnabled(false);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		myLocateLocation();
	}

	@OnClick(R.id.map_location_image)
	public void myLocation() {
		myLocateLocation();
	}

	/**
	 * 定位
	 */
	public void myLocateLocation() {
		BaiduMapUtilByRacer.locateByBaiduMap(mContext, 2000,
				new LocateListener() {

					@Override
					public void onLocateSucceed(LocationBean locationBean) {
						mLocationBean = locationBean;
						if (mMarker != null) {
							mMarker.remove();
						} else {
							mBaiduMap.clear();
						}
						mlat = locationBean.getLatitude();
						mlng = locationBean.getLongitude();
						mAddress = locationBean.getAddStr();
						mMarker = BaiduMapUtilByRacer.showMarkerByResource(
								locationBean.getLatitude(),
								locationBean.getLongitude(),
								R.drawable.icon_geo, mBaiduMap, 0, true);
						LatLng ptCenter = new LatLng(
								locationBean.getLatitude(), locationBean
										.getLongitude());
						// 反Geo搜索
						reverseGeoCode(ptCenter, -1);
					}

					@Override
					public void onLocateFiled() {

					}

					@Override
					public void onLocating() {

					}
				});
	}

	OnMapStatusChangeListener mapStatusChangeListener = new OnMapStatusChangeListener() {
		/**
		 * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
		 * 
		 * @param status
		 *            地图状态改变开始时的地图状态
		 */
		public void onMapStatusChangeStart(MapStatus status) {
		}

		/**
		 * 地图状态变化中
		 * 
		 * @param status
		 *            当前地图状态
		 */
		public void onMapStatusChange(MapStatus status) {
		}

		/**
		 * 地图状态改变结束
		 * 
		 * @param status
		 *            地图状态改变结束后的地图状态
		 */
		public void onMapStatusChangeFinish(MapStatus status) {
			if (isCanUpdateMap) {
				LatLng ptCenter = new LatLng(status.target.latitude,
						status.target.longitude);
				// 反Geo搜索
				reverseGeoCode(ptCenter, -1);
			} else {
				isCanUpdateMap = true;
			}
		}
	};

	/**
	 * Geo搜索
	 * 
	 * @param ll
	 */
	public void reverseGeoCode(LatLng ll, final int position) {
		BaiduMapUtilByRacer.getPoisByGeoCode(ll.latitude, ll.longitude,
				new GeoCodePoiListener() {

					@Override
					public void onGetSucceed(LocationBean locationBean,
							List<PoiInfo> poiList) {
						mLocationBean = (LocationBean) locationBean;
						if (aroundPoiList == null) {
							aroundPoiList = new ArrayList<PoiInfo>();
						}
						aroundPoiList.clear();
						if (poiList != null) {
							aroundPoiList.addAll(poiList);
						} else {
							Utils.showToast("该周边没有热点", Toast.LENGTH_SHORT);
						}
						updatePoiListAdapter(aroundPoiList, position);
					}

					@Override
					public void onGetFailed() {
						Utils.showToast("抱歉，未能找到结果", Toast.LENGTH_SHORT);
					}
				});
	}

	/**
	 * 刷新热门地名列表界面的adapter
	 * 
	 * @param list
	 * @param index
	 */
	private void updatePoiListAdapter(List<PoiInfo> list, int index) {
		if (mAroundPoiAdapter == null) {
			mAroundPoiAdapter = new AroundPoiAdapter(mContext, list);
			mListView.setAdapter(mAroundPoiAdapter);
		} else {
			mAroundPoiAdapter.setNewList(list, index);
		}
	}

	@OnItemClick(R.id.map_listview)
	public void OnItemClick(AdapterView<?> parent, int position) {
		isCanUpdateMap = false;
		PoiInfo poiInfo = aroundPoiList.get(position);
		mAddress = poiInfo.address;
		mlat = poiInfo.location.latitude;
		mlng = poiInfo.location.longitude;
		BaiduMapUtilByRacer.moveToTarget(poiInfo.location.latitude,
				poiInfo.location.longitude, mBaiduMap);
		LatLng ll = new LatLng(poiInfo.location.latitude,
				poiInfo.location.longitude);
		reverseGeoCode(ll, position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1002:
			if (data != null) {
				mAddress = data.getStringExtra("address");
				mlat = data.getDoubleExtra("Lat", 0);
				mlng = data.getDoubleExtra("lon", 0);
				BaiduMapUtilByRacer.moveToTarget(mlat, mlng, mBaiduMap);
				LatLng ll = new LatLng(mlat, mlng);
				reverseGeoCode(ll, -1);
			}

			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (aroundPoiList != null) {
			aroundPoiList.clear();
			aroundPoiList = null;
		}
		mAroundPoiAdapter = null;
		if (mMapView != null) {
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
			mMapView.destroyDrawingCache();
			mMapView.onDestroy();
			mMapView = null;
		}
		mMarker = null;
		super.onDestroy();
		System.gc();
	}

}
