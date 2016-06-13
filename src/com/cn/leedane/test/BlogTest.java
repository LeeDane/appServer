package com.cn.leedane.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.JsoupUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.OptionBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.OptionService;
import com.cn.leedane.service.UserService;

/**
 * 博客相关的测试类
 * @author LeeDane
 * 2015年7月3日 下午6:23:15
 * Version 1.0
 */
public class BlogTest extends BaseTest {
	@Resource
	private BlogService<BlogBean> blogService;
	
	@Resource
	private UserService<UserBean> userService;
	
	@Resource
	private OptionService<OptionBean> optionService;
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;

	@Test
	public void addBlog() throws Exception{
		BlogBean blog = new BlogBean();
		blog.setTitle("测试标题2");
		blog.setContent("这是测试信息2。。。。。。。。。。");
		//optionService.loadById(1);
		blog.setCreateUser(userService.loadById(12));
		//operateLogService.loadById(1);
		blog.setCreateTime(new Date());
		blogService.addBlog(blog);
	}
	
	@Test
	public void loadByOneBlog() {
		BlogBean bean = blogService.findById(3);
		System.out.println(bean);
	}
	
	
	/**
	 * 提取所有文章的摘要
	 */
	@Test
	public void addDigest(){
		
		List<Map<String, Object>> idList = blogService.executeSQL("select id from t_blog where id > ?", 2);
		
		int count = 0;
		if(idList.size() > 0){
			count = StringUtil.changeObjectToInt(idList.size());
			System.out.println("一共" + count+ "条数据");
		}
		
		for(int i = 0; i < count; i++){
			try{
				BlogBean bean = blogService.findById(StringUtil.changeObjectToInt(idList.get(i).get("id")));
				if(StringUtil.isNull(bean.getDigest())){
					String digest = JsoupUtil.getInstance().getDigest(bean.getContent(), 0, 120);
					bean.setDigest(digest);
					blogService.update(bean);
				}
				
				System.out.println("blog" + bean.getId() + "操作完成");
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
			
		}
	}
	
	/**
	 * 删除重复标题的数据，保留最新的一条
	 */
	@Test
	public void deleteSameBlog(){
		int index = 0;
		deleteOne(index);
		System.out.println("处理完成啦");
	}
	
	public void deleteOne(int index){
		List<Map<String, Object>> list = blogService.executeSQL("select id, title from t_blog where id > ? order by id asc limit 1", index);
		if(list.size() > 0){
			int preId = StringUtil.changeObjectToInt(list.get(0).get("id"));
			List<Map<String, Object>> list1 = blogService.executeSQL("select id from t_blog where title=? and id > ?", list.get(0).get("title"), preId);
			if(list1.size()> 0){
				//int id = StringUtil.changeObjectToInt(list1.get(0).get("id"));
				try {
					blogService.delete(blogService.findById(preId));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			deleteOne(index + 1);
		}
	}
	
	/**
	 * 查询
	 */
	@Test
	public void searchBlog(){
		String search = "他";
		StringBuffer blogSql = new StringBuffer();
		blogSql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
		blogSql.append(" , b.digest, b.froms, b.create_user_id, u.account ");
		blogSql.append(" from t_blog b inner join t_user u on b.create_user_id = u.id ");
		blogSql.append(" where b.status = ? and ((b.content like '%"+search+"%') or (b.title like '%"+search+"%')) limit 10");
		List<Map<String, Object>> blogs = blogService.executeSQL(blogSql.toString(), ConstantsUtil.STATUS_NORMAL);
		System.out.println(blogs.size());
	}
	
}
