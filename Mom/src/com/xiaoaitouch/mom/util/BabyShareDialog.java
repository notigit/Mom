package com.xiaoaitouch.mom.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.ShareDialogAdapter;
import com.xiaoaitouch.mom.configs.Constant;

/**
 * 宝贝儿信息分享
 * 
 * @author huxin
 * 
 */
public class BabyShareDialog {
	private Activity mActivity;
	private View mViewMiddle;
	private View mViewButtom;
	private GridView mGridView;
	private boolean isPlayAnimation;
	private ShareDialogAdapter mAdapter;
	private ActionSheetDialogDown mDialogDown;
	private View mShareView;

	private ImageView mBabyImage;
	private TextView mBabyDueTv;
	private TextView mBabyContentTv;
	private View mViewTop1;
	private View mViewTop2;
	//
	public static final String DESCRIPTOR = "com.umeng.share";
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);
	public String sharedContent;

	public BabyShareDialog(Activity activity) {
		this.mActivity = activity;
		configPlatforms();
		mDialogDown = new ActionSheetDialogDown(mActivity);
		mShareView = LayoutInflater.from(mActivity).inflate(
				R.layout.baby_share_item, null);

		mBabyDueTv = (TextView) mShareView.findViewById(R.id.baby_due_day_tv);
		mBabyContentTv = (TextView) mShareView
				.findViewById(R.id.main_share_content_tv);

		mBabyImage = (ImageView) mShareView.findViewById(R.id.baby_week_image);
		mViewTop1 = mShareView.findViewById(R.id.photo_share_top_image1);
		mViewTop2 = mShareView.findViewById(R.id.photo_share_top_image2);
		mGridView = (GridView) mShareView.findViewById(R.id.share_gridview);
		mViewMiddle = mShareView.findViewById(R.id.baby_share_content_view);
		mViewButtom = mShareView.findViewById(R.id.share_gridview_ray);
		// { "QQ空间", "QQ", "微博", "微信", "朋友圈" };
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				switch (position) {
				case 0:
					performShare(SHARE_MEDIA.QZONE);
					break;
				case 1:
					performShare(SHARE_MEDIA.QQ);
					break;
				case 2:
					performShare(SHARE_MEDIA.SINA);
					break;
				case 3:
					performShare(SHARE_MEDIA.WEIXIN);
					break;
				case 4:
					performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 宝贝信息
	 * 
	 * @param due
	 * @param details
	 * @param resId
	 */
	public void babyShareDialog(String due, String details, int resId) {
		isPlayAnimation = false;
		mBabyDueTv.setText(due);
		mBabyContentTv.setText(details.replace("=", ""));
		mBabyImage.setImageResource(resId);

		mViewTop1.setVisibility(View.VISIBLE);
		mViewTop2.setVisibility(View.VISIBLE);
		Animation animationView = AnimationUtils.loadAnimation(mActivity,
				R.anim.photo_share_slide_in_top);
		mViewTop1.setAnimation(animationView);
		mViewTop2.setAnimation(animationView);
		animationView.setAnimationListener(animationListener);

		mDialogDown.setContentView(mShareView);
		mDialogDown.show();
	}

	AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (mViewMiddle != null && !isPlayAnimation) {
				isPlayAnimation = true;
				mViewMiddle.setVisibility(View.VISIBLE);
				mViewButtom.setVisibility(View.VISIBLE);
				mAdapter = new ShareDialogAdapter(mActivity);
				mGridView.setAdapter(mAdapter);
				Animation animationView = AnimationUtils.loadAnimation(
						mActivity, R.anim.photo_share_slide_in_middle);
				mViewMiddle.setAnimation(animationView);
				animationView.setAnimationListener(animationListener);
			} else if (isPlayAnimation
					&& mViewMiddle.getVisibility() == View.VISIBLE) {
				setShareContent();
			}
		}
	};

	/**
	 * 视图截图
	 * 
	 * @param view
	 * @return
	 */
	private Bitmap photoScreen(View view) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache();
	}

	private void performShare(SHARE_MEDIA platform) {
		mController.postShare(mActivity, platform, new SnsPostListener() {
			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "平台分享成功";
				} else {
					showText += "平台分享失败";
				}
				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
			}
		});
		mDialogDown.cancel();
	}

	private void setShareContent() {
		Bitmap bitmap = photoScreen(mViewMiddle);
		UMImage umImage = new UMImage(mActivity, bitmap);
		// 配置sina SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.setShareContent(sharedContent);
		mController.setShareImage(umImage);
		// QQ
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareImage(umImage);
		qqShareContent.setShareContent(sharedContent);
		mController.setShareMedia(qqShareContent);
		// QQ空间
		QZoneShareContent qZoneShareContent = new QZoneShareContent();
		qZoneShareContent.setShareImage(umImage);
		qZoneShareContent.setShareContent(sharedContent);
		mController.setShareMedia(qZoneShareContent);

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareImage(umImage);
		weixinContent.setShareContent(sharedContent);
		mController.setShareMedia(weixinContent);
		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(sharedContent);
		circleMedia.setShareImage(umImage);
		mController.setShareMedia(circleMedia);
		// 新浪
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent(sharedContent);
		sinaContent.setShareImage(umImage);
		mController.setShareMedia(sinaContent);
	}

	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * 添加微信平台分享
	 */
	private void addWXPlatform() {
		// qq
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity,
				Constant.QQ_APP_ID, Constant.QQ_APP_Secret);
		qqSsoHandler.addToSocialSDK();

		// QQ空间
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,
				Constant.QQ_APP_ID, Constant.QQ_APP_Secret);
		qZoneSsoHandler.addToSocialSDK();

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mActivity, Constant.WX_APP_ID,
				Constant.WX_APP_Secret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mActivity,
				Constant.WX_APP_ID, Constant.WX_APP_Secret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

}
