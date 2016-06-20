package com.xiaoaitouch.mom.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xiaoaitouch.mom.R;

public class BlockDialog extends Dialog {

	private static int default_width = 150; // 默认宽度
	private static int default_height = 150;// 默认高度
	private ImageView animationIV;
	private AnimationDrawable animationDrawable;

	// 设置默认高度为160，宽度120，并且可根据屏幕像素密度自动进行大小调整
	public BlockDialog(Context context) {
		this(context, default_width, default_height,
				R.layout.layout_block_dialog_demo, R.style.Theme_dialog);
		this.setCanceledOnTouchOutside(false);
		animationIV = (ImageView) findViewById(R.id.animationIV);
		animationDrawable = (AnimationDrawable) animationIV.getDrawable();
	}

	public BlockDialog(Context context, String message) {
		this(context, default_width, default_height,
				R.layout.layout_block_dialog_demo, R.style.Theme_dialog);
		this.setCanceledOnTouchOutside(false);
	}

	public BlockDialog(Context context, boolean outCancle) {
		this(context, default_width, default_height,
				R.layout.layout_block_dialog_demo, R.style.Theme_dialog);
		this.setCanceledOnTouchOutside(outCancle);// 设置点击周围会不会消失, false 不消失
	}

	public BlockDialog(Context context, int width, int height, int layout,
			int style) {
		super(context, style);

		// set content
		setContentView(layout);

		// set window params
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();

		// set width,height by density and gravity
		float density = getDensity(context);
		params.width = (int) (width * density);
		params.height = (int) (height * density);
		params.gravity = Gravity.CENTER;

		window.setAttributes(params);
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

	@Override
	public void show() {
		super.show();
		animationDrawable.start();
	}

}
