package com.cn.leedane.Dao;

import java.io.Serializable;

import com.cn.leedane.bean.GalleryBean;
import com.cn.leedane.bean.UserBean;
/**
 * 图库dao接口类
 * @author LeeDane
 * 2016年1月15日 下午3:13:31
 * Version 1.0
 */
public interface GalleryDao <T extends Serializable> extends BaseDao<GalleryBean>{
	/**
	 * 检查该图片是否已经加入图库
	 * @param user
	 * @param path
	 * @return
	 */
	public boolean isExist(UserBean user, String path);
}
