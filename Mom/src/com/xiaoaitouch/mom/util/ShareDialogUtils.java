package com.xiaoaitouch.mom.util;

import android.app.Activity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.ShareDialogAdapter;
import com.xiaoaitouch.mom.configs.Constant;

/**
 * 分享对话框
 * 
 * @author huxin
 * 
 */
public class ShareDialogUtils {
	public static final String DESCRIPTOR = "com.umeng.share";
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(DESCRIPTOR);
	private Activity mActivity;
	public String sharedContent;

	public ShareDialogUtils(Activity activity) {
		this.mActivity = activity;
		configPlatforms();
		setShareContent();
	}

	public void showShareDiaglog() {
		ShareDialogAdapter mAdapter = new ShareDialogAdapter(mActivity);
		final ActionSheetDialog dialog = new ActionSheetDialog(mActivity);
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		View view = inflater.inflate(R.layout.share_dialog_item, null);
		GridView mGridView = (GridView) view.findViewById(R.id.share_gridview);
		TextView mTextView = (TextView) view.findViewById(R.id.share_cancle_tv);
		mTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		mGridView.setAdapter(mAdapter);
		// { "QQ空间", "QQ", "微博", "微信", "朋友圈" };
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				switch (position) {
				case 0:
					
					break;
				case 1:
					
					break;
				case 2:
					performShare(SHARE_MEDIA.SINA, dialog);
					break;
				case 3:
					performShare(SHARE_MEDIA.WEIXIN, dialog);
					break;
				case 4:
					performShare(SHARE_MEDIA.WEIXIN_CIRCLE, dialog);
					break;

				default:
					break;
				}
			}
		});
		dialog.setContentView(view);
		dialog.show();
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
	}

	private void performShare(SHARE_MEDIA platform,
			final ActionSheetDialog dialog) {
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
				dialog.cancel();
			}
		});
	}

	private void setShareContent() {
		// 配置sina SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.setShareContent(sharedContent);

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(sharedContent);
		mController.setShareMedia(weixinContent);
		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(sharedContent);
		mController.setShareMedia(circleMedia);
		// 新浪
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent(sharedContent);
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
