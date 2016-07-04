package com.cn.leedane.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.BlogDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.OperateLogService;

/**
 * 博客service实现类
 * @author LeeDane
 * 2015年7月10日 下午6:19:30
 * Version 1.0
 */
public class BlogServiceImpl extends BaseServiceImpl<BlogBean> implements BlogService<BlogBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private BlogDao<BlogBean> blogDao;
	
	public void setBlogDao(BlogDao<BlogBean> blogDao) {
		this.blogDao = blogDao;
	}
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	@Resource
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	Map<String,Object> message = new HashMap<String,Object>();
	@Override
	public Map<String,Object> addBlog(BlogBean blog) throws Exception{	
		logger.info("BlogServiceImpl-->addBlog():blog="+blog);
		if(super.save(blog)){
			message.put("isSuccess",true);
			message.put("message","保存成功");
		}else{
			message.put("isSuccess",false);
			message.put("message","操作失败");
		}
		
		return message;
	}

	@Override
	public List<Map<String, Object>> searchBlog(String conditions) {
		logger.info("BlogServiceImpl-->searchBlog():conditions="+conditions);
		return this.blogDao.searchBlog(conditions);
	}

	@Override
	public Map<String,Object> getIndexBlog(int start,int end,String showType) {
		//if
		logger.info("BlogServiceImpl-->getIndexBlog():start="+start+",end="+end+",showType="+showType);
		List<BlogBean> blogs = this.blogDao.getMoreBlog(start, end, showType);
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(blogs.size()>0){
			message.put("isSuccess",true);
			message.put("message",blogs);	
		}else{
			//没有找到相关的文章
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
		}
		return message;
	}

	@Override
	public List<BlogBean> managerAllBlog() {
		logger.info("BlogServiceImpl-->managerAllBlog()");
		return this.blogDao.managerAllBlog();
	}

	@Override
	public int getTotalNum() {
		logger.info("BlogServiceImpl-->getTotalNum()");
		return this.blogDao.getTotalNum();
	}

	@Override
	public int getZanNum(int Bid) {
		logger.info("BlogServiceImpl-->getZanNum():Bid="+Bid);
		return this.blogDao.getZanNum(Bid);
	}

	@Override
	public int getCommentNum(int Bid) {
		logger.info("BlogServiceImpl-->getCommentNum():Bid="+Bid);
		return this.blogDao.getCommentNum(Bid);
	}

	@Override
	public int getSearchBlogTotalNum(String conditions, String conditionsType) {
		logger.info("BlogServiceImpl-->getSearchBlogTotalNum():conditions="+conditions+",conditionsType="+conditionsType);
		return this.blogDao.getSearchBlogTotalNum(conditions, conditionsType);
	}

	@Override
	public List<BlogBean> SearchBlog(int start, int end, String conditions,
			String conditionsType) {
		logger.info("BlogServiceImpl-->SearchBlog():conditions="+conditions+",conditionsType="+conditionsType+",start="+start+",end="+end);
		return this.blogDao.SearchBlog(start, end, conditions, conditionsType);
	}

	@Override
	public void addReadNum(BlogBean blog) {
		logger.info("BlogServiceImpl-->addReadNum():blog="+blog);
	}

	@Override
	public int getReadNum(int Bid) {
		logger.info("BlogServiceImpl-->getReadNum():Bid="+Bid);
		return this.blogDao.getReadNum(Bid);
	}

	@Override
	public List<BlogBean> getLatestBlog(int start, int end) {
		logger.info("BlogServiceImpl-->getLatestBlog():start="+start+",end="+end);
		return this.blogDao.getLatestBlog(start, end);
	}

	@Override
	public List<Map<String, Object>> getOneBlog(int id) {
		logger.info("BlogServiceImpl-->getOneBlog():id="+id);
		return this.blogDao.getOneBlog(id);
	}

	@Override
	public List<Map<String, Object>> getLatestBlogById(int lastBlogId, int num) {
		logger.info("BlogServiceImpl-->getLatestBlogById():lastBlogId="+lastBlogId+",num="+num);
		return this.blogDao.getLatestBlogById(lastBlogId,num);
	}

	@Override
	public List<Map<String, Object>> getHotestBlogs(int size) {
		logger.info("BlogServiceImpl-->getHotestBlogs():size="+size);
		return this.blogDao.getHotestBlogs(size);
	}

	@Override
	public int updateReadNum(int id, int num) {
		logger.info("BlogServiceImpl-->updateReadNum():id="+id+",num="+num);
		return this.blogDao.updateReadNum(id, num);
	}

	@Override
	public Map<String, Object> deleteById(JSONObject jo, HttpServletRequest request, UserBean user){
		int id = JsonUtil.getIntValue(jo, "b_id");
		logger.info("BlogServiceImpl-->deleteById():id="+id);
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(id < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		boolean result = this.blogDao.deleteById(id);
		if(result){
			message.put("isSuccess", true);
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作成功.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作成功.value);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作失败.value);
		}
		String subject = user.getAccount() + "删除了ID为"+id + "的博客" + StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "deleteById()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}

	@Override
	public List<Map<String, Object>> getNewestBlogs(int size) {
		logger.info("BlogServiceImpl-->getNewestBlogs():size="+size);
		return this.blogDao.getNewestBlogs(size);
	}

	@Override
	public List<Map<String, Object>> getRecommendBlogs(int size) {
		logger.info("BlogServiceImpl-->getRecommendBlogs():size="+size);
		return this.blogDao.getRecommendBlogs(size);
	}

	@Override
	public Map<String, Object> search(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->search():jo="+jo.toString());
		String searchKey = JsonUtil.getStringValue(jo, "searchKey");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(StringUtil.isNull(searchKey)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.检索关键字不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.检索关键字不能为空.value);
			return message;
		}
		
		List<Map<String, Object>> rs = blogDao.executeSQL("select id, img_url, title, has_img, tag, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, digest, froms, source, create_user_id from "+DataTableType.博客.value+" where status=? and (digest like '%"+searchKey+"%' or title like '%"+searchKey+"%' or content like '%"+searchKey+"%') order by create_time desc limit 25", ConstantsUtil.STATUS_NORMAL);
		if(rs != null && rs.size() > 0){
			int createUserId = 0;
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("create_user_id"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
			}
		}
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}
	
	@Override
	public Map<String, Object> addTag(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->addTag():jo="+jo.toString());
		int bid = JsonUtil.getIntValue(jo, "bid");
		String tag = JsonUtil.getStringValue(jo, "tag");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(bid < 1 || StringUtil.isNull(tag)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		
		if(tag.length() > 5){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.标签长度不能超过5位.value));
			message.put("responseCode", EnumUtil.ResponseCode.标签长度不能超过5位.value);
			return message;
		}
		
		BlogBean blogBean = blogDao.findById(bid);
		
		if(blogBean == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		boolean cut = false;
		String oldTag = blogBean.getTag();
		if(StringUtil.isNotNull(oldTag)){
			String[] oldArray = oldTag.split(",");
			StringBuffer b = new StringBuffer();
			if(oldArray.length > 2){
				cut = true;
				for(int i = 1; i < 3; i++){
					b.append(oldArray[i]);
					b.append(",");
				}
				tag = b.toString() +tag;
			}else{
				tag = oldTag +"," +tag;
			}
		}
		
		blogBean.setTag(tag);
		
		boolean result = blogDao.update(blogBean);
		
		String subject = user.getAccount() + "为博客ID为"+bid + "添加标签" +tag + StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "addTag()", ConstantsUtil.STATUS_NORMAL, 0);
		
		if(result){
			if(cut){
				message.put("message", "添加成功，标签数量超过3个，已自动删掉第一个");
			}else
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.标签添加成功.value));
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
		
		message.put("isSuccess", result);
		return message;
	}
}
