package com.cn.leedane.lucene.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.service.BlogService;

/**
 * 博客solr索引实现类
 * @author LeeDane
 * 2016年2月18日 下午3:39:53
 * Version 1.0
 */
public class BlogSolrIndexImpl implements SolrIndex{

	private BlogService<BlogBean> blogService;
	
	@SuppressWarnings("unchecked")
	public BlogSolrIndexImpl(){
		blogService = (BlogService<BlogBean>) SpringUtils.getBean("blogService");
	}
	@Override
	public void query(int start, int rows) {
		
	}

}
