package com.cn.leedane.Dao.impl;
import java.util.List;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.CrawlDao;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.CrawlBean;

/**
 * 爬虫dao实现类
 * @author LeeDane
 * 2015年7月17日 下午6:24:08
 * Version 1.0
 */
public class CrawlDaoImpl extends BaseDaoImpl<CrawlBean> implements CrawlDao<CrawlBean>{
	Logger logger = Logger.getLogger(getClass());
	@Override
	public boolean isExists(String url) {	
		logger.info("isExists():url="+url);
		String sql = "FROM CrawlBean where url = ?";
		@SuppressWarnings("unchecked")
		List<CrawlBean> beans = (List<CrawlBean>) this.getHibernateTemplate().find(sql, url);
		return beans.size()>0 ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrawlBean> findAllNotCrawl(int limit, String source) {
		if(limit < 0) return null;
		logger.info("CrawlDaoImpl-->findAllNotCrawl():limit="+limit);
		String sql = "";
		if(limit == 0){
			sql = "FROM CrawlBean where is_crawl = ? "+ getSourceSQL(source);
			return (List<CrawlBean>) this.getHibernateTemplate().find(sql, false);
		}else{
			sql = "FROM CrawlBean where is_crawl = ? "+ getSourceSQL(source)+" limit ?";
			return (List<CrawlBean>) this.getHibernateTemplate().find(sql, false, limit);
		}
		
	}
	
	private String getSourceSQL(String source){
		String sql = "";
		if(StringUtil.isNull(source)){
			return sql;
		}
		
		sql = " and source = '"+source+"' ";
		
		return sql;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrawlBean> findAllHotNotCrawl(int limit) {
		if(limit < 0) return null;
		logger.info("CrawlDaoImpl-->findAllHotNotCrawl():limit="+limit);
		
		String sql = "";
		if(limit == 0){
			sql = "FROM CrawlBean where is_crawl = ? order by score desc";
			return (List<CrawlBean>) this.getHibernateTemplate().find(sql, false);
		}else{
			sql = "FROM CrawlBean where isCrawl = ? limit ?";
			return (List<CrawlBean>) this.getHibernateTemplate().find(sql, false, limit);
		}
	}

	@Override
	public boolean updateAllScore() {
		logger.info("CrawlDaoImpl-->updateAllScore()");
		return false;
	}
	
	
}
