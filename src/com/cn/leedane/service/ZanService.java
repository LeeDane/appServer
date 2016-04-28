package com.cn.leedane.service;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.UserBean;
import com.cn.leedane.bean.ZanBean;
/**
 * 赞的Service类
 * @author LeeDane
 * 2016年1月13日 下午2:30:27
 * Version 1.0
 */
public interface ZanService<T extends Serializable> extends BaseService<ZanBean>{

	/**
	 * 添加赞(已经存在直接返回true)
	 * {'table_name':'t_mood', 'table_id':1}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> addZan(JSONObject jo, UserBean user, HttpServletRequest request);

	

	/**
	 * 取消赞
	 * {'zid':1, 'create_user_id':1}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> deleteZan(JSONObject jo, UserBean user, HttpServletRequest request) ;



	/**
	 * 获取赞列表
	 * {'uid':1,'table_name':'t_mood','table_id':1, 'method':'firstloadings'
	 * 'pageSize':5,'last_id':0,'first_id':0}
	 * 根据uid获取该用户的赞列表，table_name为空表示获取全部赞列表，table_name不为空，获取指定表赞，table_id
	 * 为空，获取指定表下面的全部赞列表，table_id不为空，获取指定的赞
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request);
}
