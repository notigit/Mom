package com.xiaoaitouch.mom.camera;

import java.io.IOException;
import java.util.List;

import com.xiaoaitouch.mom.util.ShareInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreView extends SurfaceView implements SurfaceHolder.Callback {

	public Context mContext;
	public Camera mCamera;
	private SurfaceHolder mHolder;

	private Parameters parameters;

	public CameraPreView(Context context, Camera camera) {
		super(context);
		// TODO Auto-generated constructor stub\
		this.mContext = context;
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/** 安全方式获得相机的一个实例对象 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {

		}
		return c;
	}

	/** 相机参数的初始化设置 */
	private void initCamera() {
		int screenWidth = ShareInfo.getTagInt(mContext, ShareInfo.TAG_SCREEN_WIDTH);
		mCamera.setDisplayOrientation(90);
		parameters = mCamera.getParameters();
		Size pViewSize = getCameraPreViewSize(parameters, screenWidth);
		parameters.setPreviewSize(pViewSize.width, pViewSize.height);
		Size picSize = getPictureResolution(parameters);
		parameters.setPictureSize(picSize.width, picSize.height);
		mCamera.setParameters(parameters);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mCamera == null) {
			mCamera = getCameraInstance();
		}
		System.out.println(mCamera + "=======================" + mHolder);
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
//		mCamera.setPreviewCallback(null) ;
//		mCamera.stopPreview();
//		mCamera.release();
//		mCamera = null;
	}

	int cameraPosition = 1;	//默认摄像头，1前置
	/**
	 * 切换摄像头
	 * 
	 */
	public void switchCamera() {
		// TODO Auto-generated method stub
		int cameraCount = 0;
		CameraInfo cameraInfo = new CameraInfo();
		cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

		try {
			for (int i = 0; i < cameraCount; i++) {
				Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
				if (cameraPosition == 1) {
					// 现在是后置，变更为前置
//					if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																						// CAMERA_FACING_BACK后置
						mCamera.stopPreview();// 停掉原来摄像头的预览
						mCamera.release();// 释放资源
						mCamera = null;// 取消原来摄像头
						mCamera = Camera.open(1);// 打开当前选中的摄像头
						mCamera.autoFocus(new AutoFocusCallback() {
							@Override
							public void onAutoFocus(boolean success, Camera camera) {
								if (success) {
									initCamera();				// 实现相机的参数初始化
									camera.cancelAutoFocus();	// 只有加上了这一句，才会自动对焦。
								}
							}

						});
						try {
							mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mCamera.setDisplayOrientation(90);
						mCamera.startPreview();// 开始预览
						cameraPosition = 0;
						break;
//					}
				} else {
					// 现在是前置， 变更为后置
//					if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																					// CAMERA_FACING_BACK后置
						mCamera.stopPreview();// 停掉原来摄像头的预览
						mCamera.release();// 释放资源
						mCamera = null;// 取消原来摄像头
						mCamera = Camera.open(0);// 打开当前选中的摄像头
						mCamera.autoFocus(new AutoFocusCallback() {
							@Override
							public void onAutoFocus(boolean success, Camera camera) {
								if (success) {
									initCamera();				// 实现相机的参数初始化
									camera.cancelAutoFocus();	// 只有加上了这一句，才会自动对焦。
								}
							}

						});
						try {
							mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mCamera.setDisplayOrientation(90);
						mCamera.startPreview();// 开始预览
						cameraPosition = 1;
						break;
//					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}
	
	public void focus(){
		mCamera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (success) {
					initCamera();				// 实现相机的参数初始化
					camera.cancelAutoFocus();	// 只有加上了这一句，才会自动对焦。
				}
			}
		});
	} 
	
	
	/**
	 * 拍照
	 * 
	 */
	public void takePic() {
		mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
	}

	/**
	 * 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量 快门按下的回调
	 */
	ShutterCallback mShutterCallback = new ShutterCallback() {
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i("TAG", "myShutterCallback:onShutter...");
		}
	};

	/** 对jpeg图像数据的回调,最重要的一个回调 */
	PictureCallback mJpegPictureCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i("TAG", "myJpegCallback:onPictureTaken...");
			Bitmap bmp = null;
			if (null != data) {
				bmp = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位图
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
			// 保存图片到sdcard
			if (null != bmp) {
				// 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation",
				// 90)失效。
				// 图片竟然不能旋转了，故这里要旋转下
				// Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
				if (onTakePictureListener != null) {
					if (cameraPosition == 0) {
						onTakePictureListener.onPictureTaken(rotateBitmap(bmp, -90));
					}else {
						onTakePictureListener.onPictureTaken(getRotateBitmap(bmp, 90));
					}
				}
			}
			// 再次进入预览
			// mCamera.startPreview();
		}
	};

	/**
	 * 位图旋转
	 * 
	 * @param bitmap
	 * @param range
	 * @return
	 */
	private Bitmap getRotateBitmap(Bitmap bm, int range) {
		Matrix m = new Matrix();
		m.setRotate(range, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		float targetX, targetY;
		if (range == 90) {
			targetX = bm.getHeight();
			targetY = 0;
		} else {
			targetX = bm.getHeight();
			targetY = bm.getWidth();
		}

		final float[] values = new float[9];
		m.getValues(values);

		float x1 = values[Matrix.MTRANS_X];
		float y1 = values[Matrix.MTRANS_Y];

		m.postTranslate(targetX - x1, targetY - y1);

		Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bm1);
		canvas.drawBitmap(bm, m, paint);
		return bm1;
	}
	
	/** 
     * 图片旋转 
     *  
     * @param bmp 
     *            要旋转的图片 
     * @param degree 
     *            图片旋转的角度，负值为逆时针旋转，正值为顺时针旋转 
     * @return 
     */  
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {  
        Matrix matrix = new Matrix();  
        matrix.postRotate(degree);  
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);  
    }  
	
	/**
	 * 获取预览图尺寸
	 * @param parameters
	 * @param screenWidth 		视图宽度，预览图宽度不能大于容器视图
	 * @return
	 */
	private Size getCameraPreViewSize(Camera.Parameters parameters, int screenWidth) {
		List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
		for (Size size : supportedPreviewSizes) {
			if (size.width <= screenWidth) {
				return size;
			}
		}
		return supportedPreviewSizes.get(supportedPreviewSizes.size() - 2);
	}
	
	/** 获取图片分辨率
	 * @param parameters
	 * @return
	 */
	private Size getPictureResolution(Camera.Parameters parameters){
		List<Size> supportedPictureSizes = parameters
				.getSupportedPictureSizes();
		 for (int i = 0; i < supportedPictureSizes.size(); i++) {
			 Size size = supportedPictureSizes.get(i);
			 if (size.width == 1920) {
					return size;
			 }
		 }
		return supportedPictureSizes.get(supportedPictureSizes.size() - 1);
	}

	public OnTakePictureListener onTakePictureListener;

	/**
	 * 设置拍照监听
	 * 
	 * @param listener
	 */
	public void setOnTakePictureListener(OnTakePictureListener listener) {
		this.onTakePictureListener = listener;
	}

	/**
	 * 拍照监听
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnTakePictureListener {
		void onPictureTaken(Bitmap bitmap);
	}

}
