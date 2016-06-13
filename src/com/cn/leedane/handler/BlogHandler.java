package com.cn.leedane.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.BlogService;

/**
 * 文章处理类
 * @author LeeDane
 * 2016年4月6日 下午2:36:00
 * Version 1.0
 */
@Component
public class BlogHandler {
	@Autowired
	private BlogService<BlogBean> blogService;
	
	private RedisUtil redisUtil = RedisUtil.getInstance();
	
	public void setBlogService(BlogService<BlogBean> blogService) {
		this.blogService = blogService;
	}
	
	
	@Autowired
	private CommentHandler commentHandler;
	
	@Autowired
	private TransmitHandler transmitHandler;
	
	@Autowired
	private ZanHandler zanHandler;
	
	@Autowired
	private UserHandler userHandler;
	
	public void setCommentHandler(CommentHandler commentHandler) {
		this.commentHandler = commentHandler;
	}
	
	public void setTransmitHandler(TransmitHandler transmitHandler) {
		this.transmitHandler = transmitHandler;
	}
	
	public void setZanHandler(ZanHandler zanHandler) {
		this.zanHandler = zanHandler;
	}
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	/**
	 * 获取博客的详细信息
	 * @param blogId
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getBlogDetail(int blogId, UserBean user){
		return getBlogDetail(blogId, user, false);
	}
	
	/**
	 * 获取博客的详细信息
	 * @param blogId
	 * @param user
	 * @param onlyContent true表示只获取内容
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBlogDetail(int blogId, UserBean user, boolean onlyContent){
		String blogKey = getBlogKey(blogId);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		JSONArray jsonArray = new JSONArray();
		if(!redisUtil.hasKey(blogKey)){
			StringBuffer sql = new StringBuffer();
			sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time");
			sql.append(" , b.digest, b.froms, b.create_user_id, (select account from t_user where id = b.create_user_id) account");
			sql.append(" from t_blog b");
			sql.append(" where b.id=? ");
			sql.append(" and b.status = ?");
			list = blogService.executeSQL(sql.toString(), blogId, ConstantsUtil.STATUS_NORMAL);
			jsonArray = JSONArray.fromObject(list);
			redisUtil.addString(blogKey, jsonArray.toString());
		}else{
			String blog = redisUtil.getString(blogKey);
			if(StringUtil.isNotNull(blog)){
				jsonArray = JSONArray.fromObject(blog);
				list = (List<Map<String, Object>>) jsonArray;
			}
		}
		
		if(list != null && list.size() == 1 && !onlyContent){
			list.get(0).put("comment_number", commentHandler.getCommentNumber(blogId, "t_blog"));
			list.get(0).put("transmit_number", transmitHandler.getTransmitNumber(blogId, "t_blog"));
			list.get(0).put("zan_number", zanHandler.getZanNumber(blogId, "t_blog"));
//			list.get(0).put("zan_users", zanHandler.getZanUser(blogId, "t_blog", user, 6));
//			int createUserId = StringUtil.changeObjectToInt(list.get(0).get("create_user_id"));
			//暂时没用上图片
//			if( createUserId > 0)
//				//填充图片信息
//				list.get(0).put("user_pic_path", userHandler.getUserPicPath(createUserId, "30x30"));
		}
		return list;
	}
	
	/**
	 * 删除在redis
	 * @param blogId
	 * @return
	 */
	public boolean delete(int blogId){
		return redisUtil.delete(getBlogKey(blogId));
	}
	
	/**
	 * 获取心情在redis的key
	 * @param blogId
	 * @return
	 */
	public static String getBlogKey(int blogId){
		return ConstantsUtil.BLOG_REDIS +blogId;
	}
}
