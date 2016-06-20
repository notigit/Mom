package com.xiaoaitouch.mom.util;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xiaoaitouch.mom.bean.LocationBean;

public class BaiduMapUtilByRacer {
	public static GeoCoder mGeoCoder = null;
	public static GeoCodeListener mGeoCodeListener = null;

	public interface GeoCodeListener {
		void onGetSucceed(LocationBean locationBean);

		void onGetFailed();
	}

	public static GeoCodePoiListener mGeoCodePoiListener = null;

	public interface GeoCodePoiListener {
		void onGetSucceed(LocationBean locationBean, List<PoiInfo> poiList);

		void onGetFailed();
	}

	public static PoiSearch mPoiSearch = null;
	public static PoiSearchListener mPoiSearchListener = null;

	public interface PoiSearchListener {
		void onGetSucceed(List<LocationBean> locationList, PoiResult res);

		void onGetFailed();
	}

	public static PoiDetailSearchListener mPoiDetailSearchListener = null;

	public interface PoiDetailSearchListener {
		void onGetSucceed(LocationBean locationBean);

		void onGetFailed();
	}

	public static LocationClient mLocationClient = null;
	public static LocationClientOption option = null;
	public static LocateListener mLocateListener = null;
	public static MyLocationListenner mMyLocationListenner = null;
	public static int locateTime = 500;

	public interface LocateListener {
		void onLocateSucceed(LocationBean locationBean);

		void onLocateFiled();

		void onLocating();
	}

	/**
	 * 百度定位
	 * 
	 * @param mContext
	 * @param time
	 * @param listener
	 */
	public static void locateByBaiduMap(Context mContext, int time,
			LocateListener listener) {
		mLocateListener = listener;
		locateTime = time;
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(mContext);
		}
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		if (mMyLocationListenner == null) {
			mMyLocationListenner = new MyLocationListenner();
		}
		mLocationClient.registerLocationListener(mMyLocationListenner);
		if (option == null) {
			option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(time);
			option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		}
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	/**
	 * 定位SDK监听函数
	 * 
	 * @author huxin
	 * 
	 */
	public static class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (mLocateListener != null) {
				mLocateListener.onLocating();
			}
			// map view 销毁后不在处理新接收的位置
			if (location == null || location.getProvince() == null
					|| location.getCity() == null || mLocateListener == null) {
				if (mLocateListener != null) {
					mLocateListener.onLocateFiled();
				}
				if (locateTime < 1000) {
					stopAndDestroyLocate();
				}
				return;
			}
			LocationBean mLocationBean = new LocationBean();
			mLocationBean.setProvince(location.getProvince());
			mLocationBean.setCity(location.getCity());
			mLocationBean.setDistrict(location.getDistrict());
			mLocationBean.setStreet(location.getStreet());
			mLocationBean.setLatitude(location.getLatitude());
			mLocationBean.setLongitude(location.getLongitude());
			mLocationBean.setTime(location.getTime());
			mLocationBean.setLocType(location.getLocType());
			mLocationBean.setRadius(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				mLocationBean.setSpeed(location.getSpeed());
				mLocationBean.setSatellite(location.getSatelliteNumber());
				mLocationBean.setDirection(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				mLocationBean.setLocName(location.getStreet());
				// 运营商信息
				mLocationBean.setOperationers(location.getOperators());
			}
			if (mLocateListener != null) {
				mLocateListener.onLocateSucceed(mLocationBean);
			}
			stopAndDestroyLocate();
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	/**
	 * 停止及置空mLocationClient
	 */
	public static void stopAndDestroyLocate() {
		locateTime = 500;
		if (mLocationClient != null) {
			if (mMyLocationListenner != null) {
				mLocationClient
						.unRegisterLocationListener(mMyLocationListenner);
			}
			mLocationClient.stop();
		}
		mMyLocationListenner = null;
		mLocateListener = null;
		mLocationClient = null;
		option = null;
	}

	/**
	 * 通过resource显示Marker
	 * 
	 * @param lat
	 * @param lon
	 * @param resource
	 * @param mBaiduMap
	 * @param distance
	 * @param isMoveTo
	 * @return
	 */
	public static Marker showMarkerByResource(double lat, double lon,
			int resource, BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
		BitmapDescriptor bdView = BitmapDescriptorFactory
				.fromResource(resource);
		OverlayOptions ooView = new MarkerOptions()
				.position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
				.draggable(true);
		if (isMoveTo) {
			moveToTarget(lat, lon, mBaiduMap);
		}
		return (Marker) (mBaiduMap.addOverlay(ooView));
	}

	/**
	 * 隐藏百度logo和縮放按鍵
	 * 
	 * @param mMapView
	 * @param goneLogo
	 * @param goneZoomControls
	 */
	public static void goneMapViewChild(MapView mMapView, boolean goneLogo,
			boolean goneZoomControls) {
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView && goneLogo) { // 隐藏百度logo
				child.setVisibility(View.GONE);
			}
			if (child instanceof ZoomControls && goneZoomControls) { // 隐藏百度的縮放按鍵
				child.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 通过城市和关键名搜索地名
	 * 
	 * @param cityName
	 * @param keyName
	 * @param pageNum
	 * @param listener
	 */
	public static void getPoiByPoiSearch(String cityName, String keyName,
			int pageNum, PoiSearchListener listener) {
		mPoiSearchListener = listener;
		if (cityName == null || keyName == null) {
			if (mPoiSearchListener != null) {
				mPoiSearchListener.onGetFailed();
			}
			destroyPoiSearch();
			return;
		}
		if (mPoiSearch == null) {
			mPoiSearch = PoiSearch.newInstance();
		}
		mPoiSearch.setOnGetPoiSearchResultListener(new MyPoiSearchListener());
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityName)
				.keyword(keyName).pageNum(pageNum));
	}

	public static class MyPoiSearchListener implements
			OnGetPoiSearchResultListener {

		@Override
		public void onGetPoiDetailResult(PoiDetailResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				if (mPoiDetailSearchListener != null) {
					mPoiDetailSearchListener.onGetFailed();
				}
				destroyPoiSearch();
				return;
			}
			LocationBean mLocationBean = new LocationBean();
			mLocationBean.setLocName(result.getName());
			mLocationBean.setAddStr(result.getAddress());
			mLocationBean.setLatitude(result.getLocation().latitude);
			mLocationBean.setLongitude(result.getLocation().longitude);
			mLocationBean.setUid(result.getUid());
			if (mPoiDetailSearchListener != null) {
				mPoiDetailSearchListener.onGetSucceed(mLocationBean);
			}
			destroyPoiSearch();
		}

		@Override
		public void onGetPoiResult(PoiResult res) {
			if (res == null
					|| res.error == SearchResult.ERRORNO.RESULT_NOT_FOUND
					|| res.getAllPoi() == null) {
				if (mPoiSearchListener != null) {
					mPoiSearchListener.onGetFailed();
				}
				destroyPoiSearch();
				return;
			}
			List<LocationBean> searchPoiList = new ArrayList<LocationBean>();
			if (res.getAllPoi() != null) {
				for (PoiInfo info : res.getAllPoi()) {
					LocationBean cityPoi = new LocationBean();
					cityPoi.setAddStr(info.address);
					cityPoi.setCity(info.city);
					cityPoi.setLatitude(info.location.latitude);
					cityPoi.setLongitude(info.location.longitude);
					cityPoi.setUid(info.uid);
					cityPoi.setLocName(info.name);
					searchPoiList.add(cityPoi);
				}
			}
			if (mPoiSearchListener != null) {
				mPoiSearchListener.onGetSucceed(searchPoiList, res);
			}
			destroyPoiSearch();
		}
	}

	public static void destroyPoiSearch() {
		if (mPoiSearch != null) {
			mPoiSearch.destroy();
			mPoiSearch = null;
		}
		mPoiSearchListener = null;
		mPoiDetailSearchListener = null;
	}

	/**
	 * 根据经纬度获取周边热点名
	 * 
	 * @param lat
	 * @param lon
	 * @param listener
	 */
	public static void getPoisByGeoCode(double lat, double lon,
			GeoCodePoiListener listener) {
		mGeoCodePoiListener = listener;
		if (mGeoCoder == null) {
			mGeoCoder = GeoCoder.newInstance();
		}
		mGeoCoder.setOnGetGeoCodeResultListener(new MyGeoCodeListener());
		// 反Geo搜索
		mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
				.location(new LatLng(lat, lon)));
	}

	/**
	 * geo搜索的回調
	 * 
	 * @author huxin
	 * 
	 */
	public static class MyGeoCodeListener implements
			OnGetGeoCoderResultListener {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				if (mGeoCodeListener != null) {
					mGeoCodeListener.onGetFailed();
				}
				if (mGeoCodePoiListener != null) {
					mGeoCodePoiListener.onGetFailed();
				}
				destroyGeoCode();
				return;
			}
			// 反Geo搜索
			mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(result
					.getLocation()));
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			LocationBean mLocationBean = new LocationBean();
			mLocationBean.setProvince(result.getAddressDetail().province);
			mLocationBean.setCity(result.getAddressDetail().city);
			mLocationBean.setDistrict(result.getAddressDetail().district);
			mLocationBean.setLocName(result.getAddressDetail().street);
			mLocationBean.setStreet(result.getAddressDetail().street);
			mLocationBean.setStreetNum(result.getAddressDetail().streetNumber);
			mLocationBean.setLatitude(result.getLocation().latitude);
			mLocationBean.setLongitude(result.getLocation().longitude);
			if (mGeoCodeListener != null) {
				mGeoCodeListener.onGetSucceed(mLocationBean);
			}
			if (mGeoCodePoiListener != null) {
				mGeoCodePoiListener.onGetSucceed(mLocationBean,
						result.getPoiList());
			}
			destroyGeoCode();
		}
	}

	/**
	 * 移动到改点
	 * 
	 * @param lat
	 * @param lon
	 * @param mBaiduMap
	 */
	public static void moveToTarget(double lat, double lon, BaiduMap mBaiduMap) {
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(
				lat, lon)));
	}

	public static void destroyGeoCode() {
		if (mGeoCoder != null) {
			mGeoCoder.destroy();
			mGeoCoder = null;
		}
		mGeoCodeListener = null;
		mGeoCodePoiListener = null;
	}
}
