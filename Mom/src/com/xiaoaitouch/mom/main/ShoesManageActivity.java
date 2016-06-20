package com.xiaoaitouch.mom.main;

import java.util.ArrayList;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.other.MipcaActivityCapture;
import com.xiaoaitouch.mom.sqlite.BeaconTables;
import com.xiaoaitouch.mom.sqlite.DBHelper;
import com.xiaoaitouch.mom.util.ShareInfo;
import com.xiaoaitouch.mom.wheelview.BeaconInfo;
import com.xiaoaitouch.mom.wheelview.PickerView;
import com.xiaoaitouch.mom.wheelview.PickerView.OnCurrentClickListener;
import com.xiaoaitouch.mom.wheelview.PickerView.onSelectListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 小爱智能孕妇鞋管理
 * 
 * @author Administrator
 * 
 */
public class ShoesManageActivity extends Activity implements OnClickListener {
    private static final int REQUEST_DETAILS = 1;
    private static final int REQUEST_ZX = 2;

    private PickerView pickerView;
    private ImageView btnAdd;
    private ImageView btnDel;
    private ImageView btnBack;
    private DBHelper dbHelper;

    private ArrayList<BeaconInfo> shoesLists;

    private int shoesCount;
    private Context mContext;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoes_manage_layout);
        mActivity = this;
        mContext = this;
        initGallery();
        getWidget();
    }

    public void initGallery() {
        pickerView = (PickerView) this.findViewById(R.id.shoes_manage_pickerView);
        shoesLists = BeaconTables.getBeaconList(this);
        shoesCount = shoesLists.size();

        if (shoesLists.size() > 0) {
            pickerView.setBeaconData(shoesLists);
            pickerView.setOnSelectListener(new onSelectListener() {
                @Override
                public void onSelect(BeaconInfo info, int position) {
                    // TODO Auto-generated method stub
                    System.out.println("position ------------->" + info.getDesc());
                    ShareInfo.saveTagString(mContext, ShareInfo.TAG_BLE_MAC, info.getUuid());
                }
            });

            pickerView.setOnCurrentClickListener(new OnCurrentClickListener() {
                @Override
                public void onClick() {
                	showSelect();
                }
            });

            pickerView.setVisibility(View.VISIBLE);
        } else {
            pickerView.setVisibility(View.GONE);
        }
    }

    public void getWidget() {
        btnBack = (ImageView) this.findViewById(R.id.shoes_manage_btnBack);
        btnAdd = (ImageView) this.findViewById(R.id.shoes_manage_btnAdd);
        btnDel = (ImageView) this.findViewById(R.id.shoes_manage_btnDel);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }
    
    public void showSelect(){
    	AlertDialog dialog = new AlertDialog.Builder(this).create();
    	dialog.setTitle("小爱智能鞋");
    	dialog.setMessage("是否连接小爱智能鞋?");
    	dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				 BeaconInfo info = shoesLists.get(pickerView.getCurPosition());
				 ShareInfo.saveTagString(mContext, ShareInfo.TAG_BLE_MAC, info.getUuid());
                 System.out.println("cur_uuid:" + shoesLists.get(pickerView.getCurPosition()).getUuid());
                 finish();
			}
		});
    	dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	dialog.show();
    }

    public void addShoes(String uuid) {
        BeaconInfo info = new BeaconInfo();
        info.setUuid(uuid);
        info.setDesc("运动鞋No." + (shoesCount++));
        pickerView.addBeacon(info);
        initGallery();
    }

    public void delShoes(BeaconInfo info) {
        pickerView.delBeacon(info);
        initGallery();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.shoes_manage_btnAdd:
            Intent intent = new Intent(ShoesManageActivity.this, ShoesDetailsActivity.class);
            startActivityForResult(intent, REQUEST_DETAILS);
            break;
        case R.id.shoes_manage_btnDel:
            delShoes(shoesLists.get(pickerView.getCurPosition()));
            break;
        case R.id.shoes_manage_btnBack:
            onBackBtnClick();
            break;
        }
    }

    protected void onBackBtnClick() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                onBackBtnClick();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_ZX) {
            if (resultCode == RESULT_OK) {
                addShoes(data.getStringExtra("result"));
            }
        } else if (requestCode == REQUEST_DETAILS) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, MipcaActivityCapture.class);
                startActivityForResult(intent, REQUEST_ZX);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
