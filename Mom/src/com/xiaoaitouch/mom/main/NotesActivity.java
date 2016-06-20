package com.xiaoaitouch.mom.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.gpuimage.GPUImage;
import org.lasque.tusdk.core.gpuimage.GPUImageFilter;
import org.lasque.tusdk.core.gpuimage.extend.FilterManager;
import org.lasque.tusdk.core.gpuimage.extend.FilterManager.FilterManagerDelegate;
import org.lasque.tusdk.impl.view.widget.TuProgressHub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Response.Listener;
import com.baidu.location.BDLocation;
import com.devsmart.android.ui.HorizontalListView;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaoaitouch.event.EventBus;
import com.xiaoaitouch.event.bean.NewFragmentDataEvent;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.CardResult;
import com.xiaoaitouch.mom.bean.SendCardParams;
import com.xiaoaitouch.mom.camera.CameraPreView;
import com.xiaoaitouch.mom.camera.CameraPreView.OnTakePictureListener;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.configs.Constant;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.BlockDialog;
import com.xiaoaitouch.mom.util.MainDatabase;
import com.xiaoaitouch.mom.util.SendCardTask;
import com.xiaoaitouch.mom.util.SendCardTask.CardResultListener;
import com.xiaoaitouch.mom.util.ShareInfo;
import com.xiaoaitouch.mom.util.Utils;
import com.xiaoaitouch.mom.dao.UserInfo;

/**
 * 卡片的发布
 * 
 * @author Administrator
 * 
 */
public class NotesActivity extends Activity implements OnClickListener {
	// 莱卡 人像 美食
	@Bind(R.id.notes_relativeLayout)
	RelativeLayout layoutCamera;
	@Bind(R.id.notes_listview)
	HorizontalListView listview;
	@Bind(R.id.notes_btnFlash)
	ImageView btnFlash; // 闪光
	@Bind(R.id.notes_btnSwitch)
	ImageView btnSwitch; // 摄像头切换
	@Bind(R.id.notes_btnAlbum)
	ImageView btnAblum; // 相册
	@Bind(R.id.notes_btnTakePic)
	ImageView btnTakePic; // 拍照
	@Bind(R.id.notes_layoutInfomation)
	LinearLayout layoutInfomation; // 更多信息
	@Bind(R.id.notes_etContent)
	EditText etContent;
	@Bind(R.id.notes_btnLocation)
	RelativeLayout btnLocation; // 地址定位
	@Bind(R.id.activity_left_tv)
	TextView mCancelTv;
	@Bind(R.id.share_address_tv)
	TextView mShareAddressTv;

	private ImageView gImgView;
	private GPUImage gpuImage;
	private FilterAdapter adapter;

	private Camera mCamera;
	private Parameters parameters;
	private CameraPreView mPreView;

	private int screenWidth; // 屏幕宽度
	private int screenHeight; // 屏幕高度

	private double mlat = 0.0d;// 经度
	private double mlng = 0.0d;// 维度
	private String mAddress;// 地名
	private int feelingType = 1;
	// 选择本地图片的地址
	private String selectImagePath = "";
	private BDLocation mBDLocation = null;
	private static final String SAVE_PIC_PATH = Environment
			.getExternalStorageState().equalsIgnoreCase(
					Environment.MEDIA_MOUNTED) ? Environment
			.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";// 保存到SD卡
	private String mDate = "";
	private int mType = 1;
	private BlockDialog mBlockDialog;
	private long createTime = 0L;
	private UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_layout);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.height = (int) (display.getHeight() - 40);
		ButterKnife.bind(this);
		MomApplication mApplication = (MomApplication) getApplication();
		mBDLocation = mApplication.getBDLocation();
		mUserInfo = mApplication.getUserInfo();
		init();
		initCamera();
		getWidget();
	}

	@Override
	protected void onDestroy() {
		if (mPreView.mCamera != null) {
			adapter.recycle();
			mPreView.mCamera.setPreviewCallback(null);
			mPreView.mCamera.stopPreview();
			mPreView.mCamera.release();
			mPreView.mCamera = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				Intent intent = new Intent();
				intent.setAction(Constant.BROADCAST_ACTION);
				intent.putExtra("isBlur", false);
				LocalBroadcastManager.getInstance(NotesActivity.this)
						.sendBroadcast(intent);
				finish();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@OnClick(R.id.activity_left_tv)
	public void onBack() {
		onActivityFinish();
	}

	private void onActivityFinish() {
		Intent intent = new Intent();
		intent.setAction(Constant.BROADCAST_ACTION);
		intent.putExtra("isBlur", false);
		LocalBroadcastManager.getInstance(NotesActivity.this).sendBroadcast(
				intent);
		EventBus.getDefault().post(new NewFragmentDataEvent());
		finish();
	}

	/**
	 * 初始化基础数据
	 * 
	 */
	public void init() {
		mBlockDialog = new BlockDialog(this);
		mDate = getIntent().getStringExtra("date");
		mType = getIntent().getIntExtra("type", 1);
		if (mBDLocation != null) {
			mAddress = mBDLocation.getAddrStr();
			mlat = mBDLocation.getLatitude();
			mlng = mBDLocation.getLongitude();
			mShareAddressTv.setText(mAddress);
		}
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		ShareInfo.saveTagInt(this, ShareInfo.TAG_SCREEN_WIDTH, screenWidth);
		ShareInfo.saveTagInt(this, ShareInfo.TAG_SCREEN_HEIGHT, screenHeight);
	}

	/**
	 * 初始化摄像头
	 * 
	 */
	public void initCamera() {
		mCamera = CameraPreView.getCameraInstance();
		mCamera.setDisplayOrientation(90);
		parameters = mCamera.getParameters();
		Size pViewSize = getCameraPreViewSize(parameters, screenWidth);
		parameters.setPreviewSize(pViewSize.width, pViewSize.height);
		Size picSize = getPictureResolution(parameters);
		parameters.setPictureSize(picSize.width, picSize.height);
		mCamera.setParameters(parameters);

		mPreView = new CameraPreView(this, mCamera);
		mPreView.setLayoutParams(new FrameLayout.LayoutParams(screenWidth,
				screenWidth, Gravity.TOP));
		mPreView.setOnTakePictureListener(new OnTakePictureListener() {
			@Override
			public void onPictureTaken(Bitmap bitmap) {
				gImgView = new ImageView(NotesActivity.this);
				setImageview(bitmap, false);
			}
		});
		mPreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPreView.focus();
			}
		});
	}

	/**
	 * 获取组件
	 * 
	 */
	public void getWidget() {
		new SymbolSwitch();
		btnFlash.setOnClickListener(this);
		btnSwitch.setOnClickListener(this);
		btnTakePic.setOnClickListener(this);

		btnFlash.setTag(0);
		btnFlash.performClick();

		layoutCamera.addView(mPreView);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initFilter();
			}
		}, 1000);

	}

	/**
	 * 初始滤镜
	 * 
	 */
	public void initFilter() {
		// TuProgressHub.setStatus(NotesActivity.this,
		// TuSdkContext.getString("lsq_initing"));
		// TuSdk.checkFilterManager(mFilterManagerDelegate);

		adapter = new FilterAdapter(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (gImgView != null) {
					gpuImage.setFilter(adapter.getFilter(position));
					gImgView.setImageBitmap(gpuImage
							.getBitmapWithFilterApplied());
				}
			}
		});
	}

	/**
	 * 滤镜适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class FilterAdapter extends BaseAdapter {
		ArrayList<String> filterLists; // 滤镜名称
		ArrayList<Bitmap> bmpLists; // 滤镜后bitmap
		LayoutInflater mInflater;
		Map<String, String> hashMap;
		Context mContext;

		public FilterAdapter(Context context) {
			this.mContext = context;
			this.mInflater = LayoutInflater.from(context);
			this.hashMap = new HashMap<String, String>();

			FilterManager manager = FilterManager.shared();
			this.filterLists = (ArrayList<String>) manager.getFilterNames();
			this.bmpLists = new ArrayList<Bitmap>();
			for (String fName : filterLists) {
				transFliterName(fName);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory
						.decodeResource(context.getResources(),
								R.drawable.test_filter, options);
				GPUImage gpuImage = new GPUImage(context);
				gpuImage.setImage(bitmap);
				gpuImage.setFilter(manager.getFilterWrap(fName).getFilter());
				bmpLists.add(gpuImage.getBitmapWithFilterApplied());
				gpuImage.getBitmapWithFilterApplied().recycle();
			}
		}

		public void transFliterName(String filterName) {
			if (filterName.equals("Normal")) {
				hashMap.put(filterName, "无效果");
			} else if (filterName.equals("Brilliant")) {
				hashMap.put(filterName, "灿烂");
			} else if (filterName.equals("Leica")) {
				hashMap.put(filterName, "莱卡");
			} else if (filterName.equals("Gloss")) {
				hashMap.put(filterName, "光泽");
			} else if (filterName.equals("Harmony")) {
				hashMap.put(filterName, "和谐");
			} else if (filterName.equals("Noir")) {
				hashMap.put(filterName, "黑白");
			} else if (filterName.equals("SkinNature")) {
				hashMap.put(filterName, "自然");
			} else if (filterName.equals("SkinPink")) {
				hashMap.put(filterName, "粉嫩");
			} else if (filterName.equals("SkinJelly")) {
				hashMap.put(filterName, "果冻");
			} else if (filterName.equals("SkinNoir")) {
				hashMap.put(filterName, "黑白");
			} else if (filterName.equals("SkinRuddy")) {
				hashMap.put(filterName, "红润");
			} else if (filterName.equals("SkinPowder")) {
				hashMap.put(filterName, "蜜粉");
			} else if (filterName.equals("SkinSugar")) {
				hashMap.put(filterName, "糖水色");
			} else if (filterName.equals("Abao")) {
				hashMap.put(filterName, "阿宝");
			} else if (filterName.equals("Thick")) {
				hashMap.put(filterName, "浓郁");
			} else if (filterName.equals("Vintage")) {
				hashMap.put(filterName, "复古");
			} else if (filterName.equals("Nostalgic")) {
				hashMap.put(filterName, "怀旧");
			} else if (filterName.equals("Forest")) {
				hashMap.put(filterName, "森林");
			}
		}

		public void recycle(){
			for (Bitmap bmp : bmpLists) {
				bmp.recycle();
			}
			bmpLists = null;
		}
		
		@Override
		public int getCount() {
			return filterLists.size();
		}

		public GPUImageFilter getFilter(int position) {
			FilterManager manager = FilterManager.shared();
			return manager.getFilterWrap(filterLists.get(position)).getFilter();
		}

		@Override
		public Object getItem(int position) {
			return filterLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();

			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.list_item_filter, null);
				holder.imgFilter = (ImageView) convertView
						.findViewById(R.id.list_filter_imgFilter);
				holder.tvName = (TextView) convertView
						.findViewById(R.id.list_filter_tvFilterName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvName.setText(hashMap.get(filterLists.get(position)));
			holder.imgFilter.setImageBitmap(bmpLists.get(position));
			
			
			return convertView;
		}

		class ViewHolder {
			ImageView imgFilter;
			TextView tvName;

		}

	}

	/**
	 * 预览图比例缩放 , 跟预览图尺寸匹配 mPreView.setLayoutParams(new
	 * FrameLayout.LayoutParams(screenWidth, screenWidth, Gravity.TOP));
	 * 
	 * @param parameters
	 * @param screenResolution
	 * @return
	 */
	private Point getBestCameraResolution(Camera.Parameters parameters,
			Point screenResolution) {
		float tmp = 0f;
		float mindiff = 100f;
		float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
		Size best = null;
		List<Size> supportedPreviewSizes = parameters
				.getSupportedPreviewSizes();
		for (Size s : supportedPreviewSizes) {

			System.out.println("#:" + s.width + "*" + s.height);
			tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
			if (tmp < mindiff) {
				mindiff = tmp;
				best = s;
			}
		}
		return new Point(best.width, best.height);
	}

	/**
	 * 获取预览图尺寸
	 * 
	 * @param parameters
	 * @param screenWidth
	 *            视图宽度，预览图宽度不能大于容器视图
	 * @return
	 */
	private Size getCameraPreViewSize(Camera.Parameters parameters,
			int screenWidth) {
		List<Size> supportedPreviewSizes = parameters
				.getSupportedPreviewSizes();
		for (Size size : supportedPreviewSizes) {
			
			if (size.width <= screenWidth) {
				return size;
			}
		}
		return supportedPreviewSizes.get(supportedPreviewSizes.size() - 2);
	}

	/**
	 * 获取图片分辨率
	 * 
	 * @param parameters
	 * @return
	 */
	private Size getPictureResolution(Camera.Parameters parameters) {
		List<Size> supportedPictureSizes = parameters
				.getSupportedPictureSizes();
		 for (int i = 0; i < supportedPictureSizes.size(); i++) {
			 Size size = supportedPictureSizes.get(i);
			 if (size.width == 1920) {
					return size;
			 }
		 }
		return supportedPictureSizes.get(supportedPictureSizes.size() - 2);
	}

	/**
	 * 等比缩放图片
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scale = ((float) newWidth) / width;
		float scaleHeight = ((float) newWidth) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 滤镜管理器委托
	 */
	private FilterManagerDelegate mFilterManagerDelegate = new FilterManagerDelegate() {
		@Override
		public void onFilterManagerInited(FilterManager manager) {
			TuProgressHub.showSuccess(NotesActivity.this,
					TuSdkContext.getString("lsq_inited"));
		}
	};

	/**
	 * 闪光模式 ， 默认位置从0 0 自动 ， 1 关闭 ， 2 打开
	 */
	private void changeFlash() {
		Parameters parameters = mCamera.getParameters();

		int status = (Integer) btnFlash.getTag();
		if (status == 0) {
			btnFlash.setImageResource(R.drawable.record_light_icon_auto);
			parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
			btnFlash.setTag(1);
		} else if (status == 1) {
			btnFlash.setImageResource(R.drawable.record_light_icon_close);
			parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			btnFlash.setTag(2);
		} else if (status == 2) {
			btnFlash.setImageResource(R.drawable.record_light_icon_open);
			parameters.setFlashMode(Parameters.FLASH_MODE_ON);
			btnFlash.setTag(0);
		}

		mCamera.setParameters(parameters);
	}

	/**
	 * 获取当前滤镜处理后Bitmap
	 * 
	 * @return
	 */
	private Bitmap getCurrentImage() {
		if (gpuImage == null) {
			return null;
		} else
			return gpuImage.getBitmapWithFilterApplied();
	}

	/**
	 * 表情控制
	 * 
	 * @author Administrator
	 * 
	 */
	class SymbolSwitch implements OnClickListener {
		private ImageView imgSymbol01;
		private ImageView imgSymbol02;
		private ImageView imgSymbol03;
		private ImageView imgSymbol04;
		private ImageView imgSymbol05;
		private ImageView imgSymbol06;

		private ImageView imgSymbolTab01;
		private ImageView imgSymbolTab02;
		private ImageView imgSymbolTab03;
		private ImageView imgSymbolTab04;
		private ImageView imgSymbolTab05;
		private ImageView imgSymbolTab06;

		public SymbolSwitch() {
			imgSymbol01 = (ImageView) findViewById(R.id.notes_imgSymbol01);
			imgSymbol02 = (ImageView) findViewById(R.id.notes_imgSymbol02);
			imgSymbol03 = (ImageView) findViewById(R.id.notes_imgSymbol03);
			imgSymbol04 = (ImageView) findViewById(R.id.notes_imgSymbol04);
			imgSymbol05 = (ImageView) findViewById(R.id.notes_imgSymbol05);
			imgSymbol06 = (ImageView) findViewById(R.id.notes_imgSymbol06);

			imgSymbolTab01 = (ImageView) findViewById(R.id.notes_imgSymbolTab01);
			imgSymbolTab02 = (ImageView) findViewById(R.id.notes_imgSymbolTab02);
			imgSymbolTab03 = (ImageView) findViewById(R.id.notes_imgSymbolTab03);
			imgSymbolTab04 = (ImageView) findViewById(R.id.notes_imgSymbolTab04);
			imgSymbolTab05 = (ImageView) findViewById(R.id.notes_imgSymbolTab05);
			imgSymbolTab06 = (ImageView) findViewById(R.id.notes_imgSymbolTab06);

			imgSymbol01.setOnClickListener(this);
			imgSymbol02.setOnClickListener(this);
			imgSymbol03.setOnClickListener(this);
			imgSymbol04.setOnClickListener(this);
			imgSymbol05.setOnClickListener(this);
			imgSymbol06.setOnClickListener(this);
			imgSymbol01.performClick();
		}

		private void switchSymbol(int position) {
			feelingType = position;
			switch (position) {
			case 1:
				imgSymbolTab01.setVisibility(View.VISIBLE);
				imgSymbolTab02.setVisibility(View.INVISIBLE);
				imgSymbolTab03.setVisibility(View.INVISIBLE);
				imgSymbolTab04.setVisibility(View.INVISIBLE);
				imgSymbolTab05.setVisibility(View.INVISIBLE);
				imgSymbolTab06.setVisibility(View.INVISIBLE);
				break;
			case 2:
				imgSymbolTab01.setVisibility(View.INVISIBLE);
				imgSymbolTab02.setVisibility(View.VISIBLE);
				imgSymbolTab03.setVisibility(View.INVISIBLE);
				imgSymbolTab04.setVisibility(View.INVISIBLE);
				imgSymbolTab05.setVisibility(View.INVISIBLE);
				imgSymbolTab06.setVisibility(View.INVISIBLE);
				break;
			case 3:
				imgSymbolTab01.setVisibility(View.INVISIBLE);
				imgSymbolTab02.setVisibility(View.INVISIBLE);
				imgSymbolTab03.setVisibility(View.VISIBLE);
				imgSymbolTab04.setVisibility(View.INVISIBLE);
				imgSymbolTab05.setVisibility(View.INVISIBLE);
				imgSymbolTab06.setVisibility(View.INVISIBLE);
				break;
			case 4:
				imgSymbolTab01.setVisibility(View.INVISIBLE);
				imgSymbolTab02.setVisibility(View.INVISIBLE);
				imgSymbolTab03.setVisibility(View.INVISIBLE);
				imgSymbolTab04.setVisibility(View.VISIBLE);
				imgSymbolTab05.setVisibility(View.INVISIBLE);
				imgSymbolTab06.setVisibility(View.INVISIBLE);
				break;
			case 5:
				imgSymbolTab01.setVisibility(View.INVISIBLE);
				imgSymbolTab02.setVisibility(View.INVISIBLE);
				imgSymbolTab03.setVisibility(View.INVISIBLE);
				imgSymbolTab04.setVisibility(View.INVISIBLE);
				imgSymbolTab05.setVisibility(View.VISIBLE);
				imgSymbolTab06.setVisibility(View.INVISIBLE);
				break;
			case 6:
				imgSymbolTab01.setVisibility(View.INVISIBLE);
				imgSymbolTab02.setVisibility(View.INVISIBLE);
				imgSymbolTab03.setVisibility(View.INVISIBLE);
				imgSymbolTab04.setVisibility(View.INVISIBLE);
				imgSymbolTab05.setVisibility(View.INVISIBLE);
				imgSymbolTab06.setVisibility(View.VISIBLE);
				break;
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.notes_imgSymbol01:
				switchSymbol(1);
				break;
			case R.id.notes_imgSymbol02:
				switchSymbol(2);
				break;
			case R.id.notes_imgSymbol03:
				switchSymbol(3);
				break;
			case R.id.notes_imgSymbol04:
				switchSymbol(4);
				break;
			case R.id.notes_imgSymbol05:
				switchSymbol(5);
				break;
			case R.id.notes_imgSymbol06:
				switchSymbol(6);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notes_btnFlash: // 闪光
			changeFlash();
			break;
		case R.id.notes_btnSwitch: // 摄像头切换
			mPreView.switchCamera();
			break;
		case R.id.notes_btnTakePic: // 拍照
			mPreView.takePic();
			break;
		default:
			break;
		}
	}

	@OnClick(R.id.notes_btnLocation)
	public void openMapActivity() {
		Intent intent = new Intent(this, MapActivity.class);
		startActivityForResult(intent, 1004);
	}

	@OnClick(R.id.notes_btnAlbum)
	public void openGalleryTypeActivity() {
		Intent mIntent = new Intent(this, GalleryTypeActivity.class);
		startActivityForResult(mIntent, 1001);
	}

	@OnClick(R.id.activity_top_right_tv)
	public void sendCard() {
		mBlockDialog.show();
		createTime = System.currentTimeMillis();
		String content = etContent.getText().toString().trim();
		SendCardParams params = new SendCardParams();
		params.setMessage(content);
		params.setFeeling(feelingType);
		params.setLat(mlat);
		params.setLng(mlng);
		params.setLocation(mAddress);
		params.setType(mType);
		params.setDate(mDate);
		params.setCreateTime(createTime);
		// 判断是否拍照
		Bitmap bitmap = getCurrentImage();
		if (bitmap != null) {
			sendCarImage(bitmap, params);
		} else {
			if (TextUtils.isEmpty(content)) {
				showToast("请输入记录的内容");
			} else
				sendCardNoImage(params);
		}
	}

	/**
	 * 发送有图片的卡片
	 * 
	 * @param bitmap
	 * @param params
	 */
	private void sendCarImage(Bitmap bitmap, SendCardParams params) {
		try {
			params.setFilePath(saveFile(bitmap, System.currentTimeMillis()
					+ ".jpg", "/mom"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		SendCardTask task = new SendCardTask(params,
				((MomApplication) getApplication()).getUserInfo(),
				new CardResultListener() {

					@Override
					public void onCardResultSendSuccess(CardResult msg) {
						mBlockDialog.cancel();
						MainDatabase.instance(NotesActivity.this)
								.sendAddShareCardModel(mUserInfo.getUserId(),
										msg);
						onActivityFinish();
					}

					@Override
					public void onCardResultSendFailed(
							JsonResponse<CardResult> result) {
						mBlockDialog.cancel();
						switch (result.state) {
						case Configs.UN_USE:
							showToast("版本过低请升级新版本");
							break;
						case Configs.FAIL:
							showToast(result.msg);
							break;
						}
					}
				});
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			task.execute();
		}
	}

	/**
	 * 保存本地文件
	 * 
	 * @param bm
	 * @param fileName
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String saveFile(Bitmap bm, String fileName, String path)
			throws IOException {
		String subForder = SAVE_PIC_PATH + path;
		File foder = new File(subForder);
		if (!foder.exists()) {
			foder.mkdirs();
		}
		File myCaptureFile = new File(subForder, fileName);
		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
		return myCaptureFile.getPath();
	}

	/**
	 * 上传没有图片的
	 * 
	 * @param params
	 */
	private void sendCardNoImage(SendCardParams params) {

		GsonTokenRequest<CardResult> request = new GsonTokenRequest<CardResult>(
				com.android.volley.Request.Method.POST, Configs.SERVER_URL
						+ "/user/mom/feeling",
				new Listener<JsonResponse<CardResult>>() {

					@Override
					public void onResponse(JsonResponse<CardResult> response) {
						mBlockDialog.cancel();
						switch (response.state) {
						case Configs.UN_USE:
							showToast("版本过低请升级新版本");
							break;
						case Configs.FAIL:
							showToast(response.msg);
							break;
						case Configs.SUCCESS:
							MainDatabase.instance(NotesActivity.this)
									.sendAddShareCardModel(
											mUserInfo.getUserId(),
											response.data);
							onActivityFinish();
							break;
						}
					}

				}, null) {

			@Override
			public Type getType() {
				Type type = new TypeToken<JsonResponse<CardResult>>() {
				}.getType();

				return type;
			}
		};
		HttpApi.sendCard(NotesActivity.this, "/user/mom/feeling", request,
				params);
	}

	private void showToast(CharSequence text) {
		Utils.showToast(text, Toast.LENGTH_SHORT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1002) {
			if (data != null) {
				mAddress = data.getStringExtra("address");
				mlat = data.getDoubleExtra("Lat", 0);
				mlng = data.getDoubleExtra("lon", 0);
				mShareAddressTv.setText(mAddress);
			}
		} else if (resultCode == 1005) {
			if (data != null) {
				selectImagePath = data.getStringExtra("imagePath");
				gImgView = new ImageView(NotesActivity.this);
				ImageLoader.getInstance().displayImage(
						"file://" + selectImagePath, gImgView,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap bitmap) {
								setImageview(bitmap, true);
							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
							}
						});
			}
		}
	}

	private void setImageview(Bitmap bitmap, boolean isflage) {
		gpuImage = new GPUImage(NotesActivity.this);
		gImgView.setLayoutParams(new FrameLayout.LayoutParams(screenWidth,
				screenWidth));
		gImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		if (isflage) {
			gImgView.setImageBitmap(bitmap);
			gpuImage.setImage(bitmap);
		} else {
			Bitmap bmp = zoomImg(bitmap, screenWidth);
			gImgView.setImageBitmap(bmp);
			gpuImage.setImage(bmp);
		}
		mPreView.setVisibility(View.GONE);
		btnFlash.setVisibility(View.GONE);
		btnSwitch.setVisibility(View.GONE);
		btnAblum.setVisibility(View.GONE);
		btnTakePic.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);
		layoutCamera.addView(gImgView);
	}

}
