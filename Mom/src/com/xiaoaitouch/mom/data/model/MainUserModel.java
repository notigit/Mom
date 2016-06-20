package com.xiaoaitouch.mom.data.model;

import java.util.List;

import com.xiaoaitouch.mom.dao.MSymptomModel;
import com.xiaoaitouch.mom.dao.MWeightModel;
import com.xiaoaitouch.mom.dao.MscModel;

/**
 * 主界面用户当周数据本地保存实体类
 * 
 * @author huxin
 * 
 */
public class MainUserModel {
	public MWeightModel mWeightModel;
	public List<MSymptomModel> mSymptomModel;
	public List<MscModel> mscModel;
	
}
