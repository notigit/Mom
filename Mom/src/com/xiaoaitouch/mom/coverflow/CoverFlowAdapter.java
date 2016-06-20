package com.xiaoaitouch.mom.coverflow;

import java.util.ArrayList;

import com.xiaoaitouch.mom.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoverFlowAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	private ArrayList<String> lists;
	private int imgId = R.drawable.shoe_shoe_icon_unselected;
	private int imgSelectId = R.drawable.index_shoe_icon;
	
	private int curPosition = 0;
	private LayoutInflater mInflater;
	
	public CoverFlowAdapter(Context c , ArrayList<String> list) {
		mContext = c;
		this.lists = list;
		this.mInflater = LayoutInflater.from(mContext);
	}
	
	
	public void addItem(String uuid){
		lists.add(uuid);
		notifyDataSetChanged();
	}
	
	public void delItem(int position){
		lists.remove(position);
		notifyDataSetChanged();
	}
	
	public void setCurrentPosition(int position){
		this.curPosition = position;
		notifyDataSetChanged();
	} 
	
	public int getCurrentPosition(){
		return curPosition;
	}

	
	public int getCount() {
		return lists.size();
	}

	public Object getItem(int position) {
		return lists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_shoes, null);
			holder.imgPic = (ImageView) convertView.findViewById(R.id.list_shoes_img);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.list_shoes_tvDesc);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.tvDesc.setText(lists.get(position));
//		if (curPosition == position) {
//			i.setImageResource(imgId);
//		}else {
//			holder.imgPic.setImageResource(imgSelectId);
//		}
//		i.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, 150));
//		i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
//		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 

		//Make sure we set anti-aliasing otherwise we get jaggies
//		BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
//		drawable.setAntiAlias(true);
		return convertView;

		//return mImages[position];
	}
	/** Returns the size (0.0f to 1.0f) of the views 
	 * depending on the 'offset' to the center. */ 
	public float getScale(boolean focused, int offset) { 
		/* Formula: 1 / (2 ^ offset) */ 
		return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
	} 
	
	class ViewHolder{
		ImageView imgPic;
		TextView tvDesc;
	}
}
