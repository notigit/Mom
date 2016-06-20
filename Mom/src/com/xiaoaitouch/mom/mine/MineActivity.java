package com.xiaoaitouch.mom.mine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoaitouch.event.EventBus;
import com.xiaoaitouch.event.bean.MainEvent;
import com.xiaoaitouch.event.bean.MineEvent;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.ResultObj;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.droid.BaseActivity;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.ResponseListener;
import com.xiaoaitouch.mom.net.request.UploadApi;
import com.xiaoaitouch.mom.util.ButtomDialog;
import com.xiaoaitouch.mom.util.StringUtils;
import com.xiaoaitouch.mom.util.UserDataUtils;
import com.xiaoaitouch.mom.util.Utils;
import com.xiaoaitouch.mom.util.Validation;

public class MineActivity extends BaseActivity implements OnClickListener {
	/** 调用系统相机拍照 */
	public static final int REQUEST_CODE_TAKE_PHOTO = 20121;
	/** 选择相册 */
	public static final int REQUEST_CODE_TAKE_PIC = 20122;
	/** 头像剪切 */
	public static final int REQUEST_CODE_HANDLE_PHOTO = 20123;
	/** 头像文件 */
	public static final File HEAD_FIEL = new File(
			Environment.getExternalStorageDirectory() + "/temp.jpg");
	/** 头像临时存储Uri */
	public static final Uri HEAD_URI = Uri.fromFile(HEAD_FIEL);
	/** 剪切后的头像存储地址 */
	private static String headPath = null;

	@Bind(R.id.user_login_view)
	RelativeLayout mLoginTopView;
	@Bind(R.id.user_is_login_view)
	RelativeLayout mRelativeLayout;
	@Bind(R.id.user_no_login_tv)
	TextView mTextView;
	@Bind(R.id.mine_due_tv)
	TextView mMineDueTv;
	@Bind(R.id.mine_nickname_tv)
	TextView mNickNameTv;
	@Bind(R.id.user_message_list_lay)
	LinearLayout mUserListLay;
	@Bind(R.id.user_head_image)
	ImageView mUserImageView;
	@Bind(R.id.user_name)
	TextView mUserName;

	private UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.mine_activity);
		ButterKnife.bind(this);
		initView();
		setDueData();
	}

	public void initView() {
		findViewById(R.id.mine_register_tv).setOnClickListener(this);
		findViewById(R.id.mine_login_tv).setOnClickListener(this);

		findViewById(R.id.mine_nickname_view_lay).setOnClickListener(this);
		findViewById(R.id.mine_meterial_view_lay).setOnClickListener(this);
		findViewById(R.id.mine_due_view_lay).setOnClickListener(this);
	}

	public void setDueData() {
		MomApplication application = (MomApplication) getApplication();
		mUserInfo = application.getUserInfo();
		String dueTime = "";
		if (mUserInfo != null) {
			mRelativeLayout.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(mUserInfo.getAccount())
					&& !TextUtils.isEmpty(mUserInfo.getPwd())) {
				mTextView.setVisibility(View.GONE);
				mLoginTopView.setVisibility(View.VISIBLE);
			} else {
				mTextView.setVisibility(View.VISIBLE);
				mRelativeLayout.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(mUserInfo.getLastMensesTime())
					&& !mUserInfo.getLastMensesTime().equals("0")) {// 末次月经
				String[] mDueTime = StringUtils.getStringFromDate(
						Long.valueOf(mUserInfo.getLastMensesTime())).split("-");
				dueTime = StringUtils.getAddDate(mDueTime, 280);
			} else if (!TextUtils.isEmpty(mUserInfo.getDueTime())
					&& !mUserInfo.getDueTime().equals("0")) {// 预产期
				dueTime = StringUtils.getStringFromDate(Long.valueOf(mUserInfo
						.getDueTime()));
			}
			mNickNameTv.setText(mUserInfo.getNickName());
			mUserName.setText(mUserInfo.getNickName());
			mMineDueTv.setText(dueTime);
			// 下载用户头像
			ImageLoader.getInstance().displayImage(
					Configs.IMAGE_URL + mUserInfo.getHeadPic(), mUserImageView);
		} else {
			mLoginTopView.setVisibility(View.GONE);
			mRelativeLayout.setVisibility(View.VISIBLE);
			mTextView.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.user_head_image)
	public void updateHead() {
		LayoutInflater inflater = getLayoutInflater();
		final ButtomDialog mChooseDialog = new ButtomDialog(mActivity);
		View rootView = inflater
				.inflate(R.layout.user_image_action_sheet, null);
		TextView takePhotoTV = (TextView) rootView
				.findViewById(R.id.image_choose_takephoto_tv);
		takePhotoTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 拍照
				mChooseDialog.dismiss();
				// 相机拍照
				takePhoto();
			}
		});
		TextView albumTv = (TextView) rootView
				.findViewById(R.id.image_choose_album_tv);
		albumTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 选择照片
				mChooseDialog.dismiss();
				// 系统相册
				takePicture();
			}
		});
		TextView cancelTv = (TextView) rootView
				.findViewById(R.id.action_sheet_cancel_tv);
		cancelTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 取消
				mChooseDialog.dismiss();
			}
		});
		mChooseDialog.setContentView(rootView);
		mChooseDialog.show();
	}

	/**
	 * 调用系统相机拍照获取图片
	 */
	public void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		if (Validation.checkSDCard(mActivity)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 下面这句指定调用相机拍照后的照片存储的路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), "temp.jpg")));
			startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
		}
	}

	/**
	 * 选择系统相册
	 */
	public void takePicture() {
		// 调用系统相册
		Intent picIntent = new Intent(Intent.ACTION_PICK,
				Media.EXTERNAL_CONTENT_URI);
		picIntent.setType("image/*");// 设置类型
		startActivityForResult(picIntent, REQUEST_CODE_TAKE_PIC);
	}

	/**
	 * 提交修改用户头像
	 * 
	 * @param data
	 */
	public void submitUpdateHead(Intent data) {
		Map<String, String> mPostParams = new HashMap<String, String>();
		mPostParams.put("uniqueness", Utils.createUniqueness());
		mPostParams
				.put("timestemp", String.valueOf(System.currentTimeMillis()));
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			headPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/"
					+ System.currentTimeMillis()
					+ ".jpg";
			File file = new File(headPath);
			UploadApi.uploadImg(
					HttpApi.getValues("/user/headpic", mPostParams),
					file.getName(), bitmap, new ResponseListener<String>() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
						}

						@Override
						public void onResponse(String response) {
							if (!TextUtils.isEmpty(response)) {
								try {
									ResultObj result = new ResultObj(response);
									switch (result.getState()) {
									case ResultObj.FAIL:
										showToast(result.getMessage());
										break;
									case ResultObj.SUCCESS:
										JSONObject object = result
												.getObjectData();
										String headUrl = object
												.getString("headPic");
										ImageLoader.getInstance().displayImage(
												Configs.IMAGE_URL + headUrl,
												mUserImageView);
										saveHead(headUrl);
										break;
									case ResultObj.UN_USE:
										showToast("版本过低请升级新版本");
										break;
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							} else {
								showToast("修改失败");
							}
						}
					});
			recycleImage();
			headPath = null;
		}
	}

	/**
	 * 保存和修改本地数据
	 * 
	 * @param headUrl
	 */
	private void saveHead(String headUrl) {
		if (!TextUtils.isEmpty(headUrl)) {
			UserDataUtils uDataUtils = new UserDataUtils(mActivity);
			uDataUtils.updateHead(headUrl);
		}
	}

	/**
	 * 头像上传成功之后回收图片
	 */
	public void recycleImage() {
		try {
			// 回收剪切钱的原始图片
			if (HEAD_FIEL.exists()) {
				HEAD_FIEL.delete();
			}
			// 回收剪切后的图片
			if (headPath != null) {
				File file = new File(headPath);
				if (file.exists()) {
					file.delete();
				}
			}
		} catch (Exception e) {
			Log.e("ImageUtils--recycleImage", "图片回收失败");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_TAKE_PIC:// 相册选择
			if (data != null) {
				trimPhoto(data.getData());
			}
			break;
		case REQUEST_CODE_TAKE_PHOTO:// 系统相机
			if (HEAD_URI != null) {
				trimPhoto(HEAD_URI);
			}
			break;
		case REQUEST_CODE_HANDLE_PHOTO:// 图片剪切后处理
			if (data != null) {
				// 创建图片剪切后的存储路径
				submitUpdateHead(data);
			}
			break;
		}
		if (resultCode == 1012) {
			this.setDueData();
			EventBus.getDefault().post(new MainEvent());
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 */
	public void trimPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", true);

		intent.putExtra("scale", true);// 黑边
		intent.putExtra("scaleUpIfNeeded", true);// 黑边
		startActivityForResult(intent, REQUEST_CODE_HANDLE_PHOTO);
	}

	public void onEvent(MineEvent event) {
		this.setDueData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mine_nickname_view_lay:
			startIntent(UpdateNickNameActivity.class);
			break;
		case R.id.mine_due_view_lay:
			Intent mIntent = new Intent(mActivity, DueActivity.class);
			startActivityForResult(mIntent, 1002);
			break;
		case R.id.mine_meterial_view_lay:
			startIntent(MaterialActivity.class);
			break;
		case R.id.mine_register_tv:
			startIntent(RegisterActivity.class);
			break;
		case R.id.mine_login_tv:
			startIntent(LoginActivity.class);
			break;
		default:
			break;
		}
	}


	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		onBackBtnClick();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
