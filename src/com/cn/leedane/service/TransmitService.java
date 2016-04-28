package com.cn.leedane.service;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.TransmitBean;
import com.cn.leedane.bean.UserBean;
/**
 * 转发的Service类
 * @author LeeDane
 * 2016年1月13日 上午11:31:25
 * Version 1.0
 */
public interface TransmitService<T extends Serializable> extends BaseService<TransmitBean>{

	/**
	 * 发表转发(对同一个对象可以多次转发)
	 * {'table_name':'t_mood', 'table_id':1, 'content':'转发内容'}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean add(JSONObject jo, UserBean user, HttpServletRequest request) throws Exception;

	

	/**
	 * 取消转发(由于对同一个对象可以多次转发)
	 * {'tid':1, "create_user_id":1}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> deleteTransmit(JSONObject jo, UserBean user, HttpServletRequest request) ;



	/**
	 * 获取转发列表
	 * {'uid':1,'table_name':'t_mood','table_id':1, 'method':'firstloading'
	 * 'pageSize':5,'last_id':0,'first_id':0}
	 * 根据uid获取该用户的转发列表，table_name为空表示获取全部转发列表，table_name不为空，获取指定表转发，table_id
	 * 为空，获取指定表下面的全部转发列表，table_id不为空，获取指定的转发
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request);
}
