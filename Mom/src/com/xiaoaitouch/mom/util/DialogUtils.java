package com.xiaoaitouch.mom.util;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.SelfAdapter;
import com.xiaoaitouch.mom.adapter.SymptomAdapter;
import com.xiaoaitouch.mom.dao.MSymptomModel;
import com.xiaoaitouch.mom.dao.MscModel;

public class DialogUtils {
	public static final int BUTTON1 = -1;
	public static final int BUTTON2 = -2;

	@SuppressWarnings("deprecation")
	public static void showAlertDialog(Context context, String title,
			String content, String leftBtnText, String rightBtnText,
			final OnClickListener listener) {
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		Window window = dlg.getWindow();
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setContentView(R.layout.common_ui_dialog);

		TextView titleTv = (TextView) window.findViewById(R.id.dialog_title);
		TextView contentTv = (TextView) window
				.findViewById(R.id.dialog_describe);
		titleTv.setText(title);
		contentTv.setText(content);

		TextView leftBtn = (TextView) window
				.findViewById(R.id.alert_dialog_left_btn);
		leftBtn.setText(leftBtnText);
		TextView rightBtn = (TextView) window
				.findViewById(R.id.alert_dialog_right_btn);
		rightBtn.setText(rightBtnText);
		leftBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(dlg, BUTTON1);
				}
			}
		});
		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(dlg, BUTTON2);
				}
			}
		});
	}

	/**
	 * 绑定界面
	 * 
	 * @param activity
	 * @param listener
	 */
	public static void bindingAlertDialog(Activity activity,
			final OnClickListener listener) {
		final ActionDialog mActionDialog = new ActionDialog(activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.binding_dialog, null);
		view.findViewById(R.id.dialog_right_tv).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mActionDialog.dismiss();
					}
				});
		TextView bindingTv = (TextView) view.findViewById(R.id.binding_tv);
		TextView buyTv = (TextView) view.findViewById(R.id.buy_tv);
		bindingTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(mActionDialog, BUTTON1);
				}
			}
		});
		buyTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(mActionDialog, BUTTON2);
				}
			}
		});
		mActionDialog.setContentView(view);
		mActionDialog.show();
	}

	private OnWeightListener mOnWeightListener;

	public interface OnWeightListener {

		public void onWeight(String weight);

		public void isSure(boolean isFlage);
	}

	public void setOnWeightListener(OnWeightListener l) {
		mOnWeightListener = l;
	}

	/**
	 * 体重dialog
	 * 
	 * @param activity
	 * @param initValue
	 * @param forValue
	 * @param currentIndex
	 * @param ll
	 */
	public static void showMainWeightDialog(Activity activity, int initValue,
			int forValue, int currentIndex, final OnWeightListener ll) {
		final ActionUpDownDialog mActionDialog = new ActionUpDownDialog(
				activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.main_weight_dialog, null);
		String[] mStr = new String[forValue];
		for (int i = 0; i < forValue; i++) {
			mStr[i] = String.valueOf((initValue + i) + " KG");
		}
		view.findViewById(R.id.dialog_right_tv).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mActionDialog.cancel();
					}
				});
		AbstractWheel abstractWheel = (AbstractWheel) view
				.findViewById(R.id.choose_weight_view);
		final ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				activity, mStr);
		ampmAdapter.setItemResource(R.layout.main_common_wheel_item);
		ampmAdapter.setItemTextResource(R.id.main_common_wheel_item_text);
		abstractWheel.setViewAdapter(ampmAdapter);
		abstractWheel.setCurrentItem(currentIndex, false);
		abstractWheel.setCyclic(true);
		abstractWheel.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				ll.isSure(true);
				ll.onWeight(ampmAdapter.getItemText(newValue).toString());
			}
		});
		mActionDialog.setContentView(view);
		mActionDialog.show();
	}

	/**
	 * 症状dialog
	 * 
	 * @param activity
	 * @param mStrList
	 * @param isflage
	 */
	public static void showMainSymptomDialog(Activity activity,
			List<MSymptomModel> mStrList, boolean isflage) {
		final ActionUpDownDialog mActionDialog = new ActionUpDownDialog(
				activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.main_symptom_dialog, null);
		SymptomAdapter mAdapter = new SymptomAdapter(activity, mStrList,
				isflage);
		TextView mRightTv = (TextView) view.findViewById(R.id.dialog_right_tv);
		if (!isflage) {
			mRightTv.setText("关闭");
		}else{
			mRightTv.setText("完成");
		}
		mRightTv.setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mActionDialog.cancel();
					}
				});
		GridView mGridView = (GridView) view
				.findViewById(R.id.symptom_gridview);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
			

			}
		});

		mGridView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});
		mActionDialog.setContentView(view);
		mActionDialog.show();
	}

	/**
	 * 自查dialog
	 * 
	 * @param activity
	 * @param map
	 */
	public static void showMainSelfDialog(Activity activity,
			Map<Integer, MscModel> map, boolean isflage) {
		final ActionUpDownDialog mActionDialog = new ActionUpDownDialog(
				activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.main_self_dialog, null);
		SelfAdapter mAdapter = new SelfAdapter(activity, map, isflage);
		TextView mRightTv = (TextView) view.findViewById(R.id.dialog_right_tv);
		if (!isflage) {
			mRightTv.setText("关闭");
		}else{
			mRightTv.setText("完成");
		}
		mRightTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mActionDialog.cancel();
			}
		});
		ListView mListView = (ListView) view.findViewById(R.id.dialog_listview);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
			}
		});
		mActionDialog.setContentView(view);
		mActionDialog.show();
	}
}
