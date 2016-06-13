package com.cn.leedane.Dao.impl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.cn.leedane.Dao.BlogDao;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.wechat.util.WeixinUtil;

/**
 * 博客dao实现类
 * @author LeeDane
 * 2015年7月10日 下午6:17:36
 * Version 1.0
 */
public class BlogDaoImpl extends BaseDaoImpl<BlogBean> implements BlogDao<BlogBean>{
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 @Autowired  
	 public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	      super.setSessionFactory(sessionFactory);   
	 }     

	@Override
	public List<Map<String,Object>> searchBlog(String conditions) {
		logger.info("BlogDaoImpl-->searchBlog():conditions="+conditions);
		String sql = "select * from t_blog " + conditions == null ? "" : conditions;
		/*List<BlogBean> beans = jdbcTemplate.query(sql, new RowMapper(){

			@Override
			public List<BlogBean> mapRow(ResultSet rs, int index) throws SQLException {
				List<BlogBean> beans = new ArrayList<BlogBean>();
				beans.
				return null;
			}}, params);*/
		//System.out.println("SQL---------->" + sql +",conditions:" +conditions);
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<BlogBean> getMoreBlog(final int start, final int end,final String showType) {
		logger.info("BlogDaoImpl-->getMoreBlog():start="+start+",end="+end+",showType="+showType);
 		List<BlogBean> list = new ArrayList<BlogBean>();

		//try {
			
            
           //list =  this.getHibernateTemplate().executeWithNativeSession( new HibernateCallback(){

			//	@Override
			//	public Object doInHibernate(Session session)
			//			throws HibernateException {
					//final static String sql = null;
					//if(showType.equalsIgnoreCase("list")){
					final String sql = "select m.id,m.title,m.content,m.tag,admin.account,m.create_time,m.zan_number,m.comment_number,m.transmit_number,m.share_number " + 
								" from t_blog as m " +
								" inner join t_user as admin on m.create_user_id=admin.id " +
								" where admin.account in ('Lee')";
						
					//	
					///}else if(showType.equalsIgnoreCase("details")){
						//sql = "select m.id,m.title,m.tag,a.account,m.time,m.zan_num,m.comment_num,m.transmit_num,m.share_num,m.content from myblog as m join admin as a on m.admin_id=a.id ";
						
					//}					
					//Query query = session.createSQLQuery(sql);
					//query.setFirstResult(start);
					//query.setMaxResults(end);
					//return query.list();
				//}
            	
          //  });
          
		//} catch (Exception e) {
		//	System.out.println("在impl的getMoreBlog()有错");
		//	e.printStackTrace();
		//}
					
		list = this.getHibernateTemplate().execute(new HibernateCallback<List<BlogBean>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<BlogBean> doInHibernate(Session session1) throws HibernateException {
				//ist<BlogBean> list = new ArrayList<BlogBean>();
				Query query = session1.createSQLQuery(sql);
				query.setFirstResult(start);
				query.setMaxResults(end);
				return query.list();
			}
			
		});
		//list = (List<Blog>)this.getHibernateTemplate().find(sql, new Object[]{start,end});
		return list;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlogBean> managerAllBlog() {
		logger.info("BlogDaoImpl-->managerAllBlog()");
		List<BlogBean> list = new ArrayList<BlogBean>();
		try {
			String hql = "from Blog";
			list = (List<BlogBean>) this.getHibernateTemplate().find(hql);			
		} catch (Exception e) {
			System.out.println("在impl的managerAllBlog()有错");
			e.printStackTrace();	
		}
		logger.info("managerAllBlog():list.size()="+list.size());
		return list.size()<1 ? null : list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int getTotalNum() {		
		List<Integer> Bid = new ArrayList<Integer>();
		try {			
			Bid = this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException {
					String sql = "select id from t_blog";
					SQLQuery query = session.createSQLQuery(sql);
					return query.list();
				}
			});				
		} catch (Exception e) {
			e.printStackTrace();
		}	
		logger.info("BlogDaoImpl-->getTotalNum():Bid.size()="+Bid.size());
		return Bid.size();
	}

	@Override
	public void addReadNum(BlogBean blog) {
		logger.info("BlogDaoImpl-->addReadNum():blog="+blog.toString());
		try{
			this.getHibernateTemplate().update(blog);
		}catch(Exception ex){		
			System.out.println("在impl的addReadNum()有错");
			ex.printStackTrace();			
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int getReadNum(final int Bid) {
		logger.info("BlogDaoImpl-->getReadNum():Bid="+Bid);
		int num = 0;
		try {		
			num = this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session)
						throws HibernateException {
					String SQL = "select read_num from myblog where id=?";
					SQLQuery query = session.createSQLQuery(SQL);
					query.setInteger(0, Bid);
					return query.uniqueResult();
				}
			});
						
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return num;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getSearchBlogTotalNum(String conditions,String conditionsType) {
		logger.info("BlogDaoImpl-->getSearchBlogTotalNum():conditions="+conditions+",conditionsType="+conditionsType);
		List<Integer> Bid = new ArrayList<Integer>();
		try {
			String SQL = null;
			if(conditionsType.equalsIgnoreCase("tag")){
				SQL = "select id from myblog where "+conditionsType+" like '%"+conditions+"%'";
			}else if(conditionsType.equalsIgnoreCase("content")){
				SQL = "select id from myblog where "+conditionsType+" like '%"+conditions+"%' or title like '%"+conditions+"%'";
			}else if(conditionsType.equalsIgnoreCase("time")){
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = formatDate.parse(conditions);
				
				String conditions1 = formatDate.format(date);
				
				System.out.println("conditions1:"+conditions1);
				
				
				SQL = "select id from myblog where "+conditionsType+"="+conditions1+"";
			}
			 
			
			Bid = (List<Integer>)this.getHibernateTemplate().find(SQL);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Bid.size();
	}

	@Override
	public List<BlogBean> SearchBlog(int start, int end,String conditions,String conditionsType ) {
		logger.info("BlogDaoImpl-->SearchBlog():start="+start+",end="+end+",conditions="+conditions+",conditionsType="+conditionsType);
		List<BlogBean> list = new ArrayList<BlogBean>();
		/*try {
			String hql = null;
			if(conditionsType.equalsIgnoreCase("tag")){
				hql = "from Blog where "+conditionsType+" like '%"+conditions+"%'";
			}else if(conditionsType.equalsIgnoreCase("content")){
				hql = "from Blog where "+conditionsType+" like '%"+conditions+"%' or title like '%"+conditions+"%'";
			}else if(conditionsType.equalsIgnoreCase("time")){
				hql = "from Blog where "+conditionsType+" like '%"+conditions+"%'";
			}
			
			Query query = session.createQuery(hql);
			query.setFirstResult(start);
			query.setMaxResults(end);

            list = (List<Blog>)query.list();
		} catch (Exception e) {	
			e.printStackTrace();	
		}*/
		
		return list.size()<1 ? null : list;
	}

	@Override
	public int getZanNum(int Bid) {
		logger.info("BlogDaoImpl-->getZanNum():Bid="+Bid);
		int zanNum = 0;
		/*try {
			String SQL = "select zan_num from myblog where id=?";
			Query query = session.createSQLQuery(SQL);
			query.setInteger(0, Bid);
			zanNum = (Integer)query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
		return zanNum;
	}

	@Override
	public int getCommentNum(int Bid) {
		logger.info("BlogDaoImpl-->getCommentNum():Bid="+Bid);
		int commentNum = 0;
		/*try {
			String SQL = "select comment_num from myblog where id=?";
			Query query = session.createSQLQuery(SQL);
			query.setInteger(0, Bid);
			commentNum = (Integer)query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}	*/	
		
		return commentNum;
	}

	@Override
	public List<BlogBean> getLatestBlog(int start , int end) {
		List<BlogBean> list = new ArrayList<BlogBean>();
		logger.info("BlogDaoImpl-->getLatestBlog():start="+start+",end="+end);
		/*try {
			String hql = null;
			if(end<=0){  //查找全部
				hql = "select id,title,rel_time,comment_num,zan_num,read_num from myblog";
				Query query = session.createSQLQuery(hql);
	            list = (List<Blog>)query.list();	
				
			}else{
				hql = "select id,title,rel_time,comment_num,zan_num,read_num from myblog myblog";
				Query query = session.createSQLQuery(hql);
				query.setFirstResult(start);
				query.setMaxResults(end);
	            list = (List<Blog>)query.list();	
			}	
		} catch (Exception e) {
			e.printStackTrace();	
		}*/
			return list.size()<1 ? null : list;
	}

	@Override
	public List<Map<String, Object>> getOneBlog(int id) {
		logger.info("BlogDaoImpl-->getOneBlog():id="+id);
		String sql = "select b.id,b.title,b.content,DATE_FORMAT(b.create_time,'%Y-%m-%d %H:%i:%s') create_time,b.source,b.froms,b.read_number,b.origin_link,u.account from t_blog b inner join t_user u on b.create_user_id = u.id where b.id = ?";
		return jdbcTemplate.queryForList(sql,id);
	}

	@Override
	public List<Map<String, Object>> getLatestBlogById(int lastBlogId, int num) { 
		logger.info("BlogDaoImpl-->getLatestBlogById():lastBlogId="+lastBlogId+",num="+num);
		String sql = "";
		//当最后的博客Id小于1或者取的博客数量小于1
		//将改成默认的取数据库中最新的五条记录
		if(lastBlogId < 1 || num < 1){
			sql = "select b.id,b.title,b.img_url from t_blog b inner join t_user u on b.create_user_id = u.id where b.img_url is not null and b.title <> '' order by b.id desc limit ? ";
			return jdbcTemplate.queryForList(sql,num < 1 ? WeixinUtil.DEFAULT_SEARCH_NUMBER : num);
		}
		sql = "select b.id,b.title,b.img_url from t_blog b inner join t_user u on b.create_user_id = u.id where b.id < ? and b.img_url is not null and b.title <> '' order by b.id desc limit ? ";		
		return jdbcTemplate.queryForList(sql,lastBlogId,num);
	}

	@Override
	public List<Map<String, Object>> getHotestBlogs(int size) {
		logger.info("BlogDaoImpl-->getHotestBlogs():size="+size);
		if(size < 1) size = 5;
		String sql ="select id,title, comment_number,transmit_number,share_number,zan_number,read_number,(comment_number*0.45 + transmit_number*0.25 + share_number*0.2 + zan_number*0.1 + read_number*0.1) score from t_blog order by score desc,create_time desc limit ?";
		return jdbcTemplate.queryForList(sql,size);
	}

	@Override
	public int updateReadNum(int id, int num) {
		logger.info("BlogDaoImpl-->updateReadNum():id="+id+",num="+num);
		String sql = "update t_blog set read_number = ? , is_read = true where id = ?";
		return this.jdbcTemplate.update(sql, num, id);
	}

	@Override
	public boolean deleteById(int id){
		logger.info("BlogDaoImpl-->deleteById():id="+id);
		String sql = "delete from t_blog where id = ?";
		return this.jdbcTemplate.update(sql, id) > 0;
	}

	@Override
	public List<Map<String, Object>> getNewestBlogs(int size) {
		logger.info("BlogDaoImpl-->getNewestBlogs():size="+size);
		if(size < 1) size = 5;
		String sql ="select id,title,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s') create_time from t_blog order by create_time desc limit ?";
		return jdbcTemplate.queryForList(sql,size);
	}

	@Override
	public List<Map<String, Object>> getRecommendBlogs(int size) {
		logger.info("BlogDaoImpl-->getRecommendBlogs():size="+size);
		if(size < 1) size = 5;
		String sql ="select id,title,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s') create_time from t_blog order by create_time desc limit ?";
		return jdbcTemplate.queryForList(sql,size);
	}
}
