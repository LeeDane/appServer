package com.cn.leedane.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * baseTest相关的测试类
 * @author LeeDane
 * 2015年5月27日 下午3:40:31
 * Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
//请注意location的顺序，加载顺序将按照
@ContextConfiguration(locations={"classpath:applicationContext.xml","classpath:spring-beans.xml","classpath:spring-aop.xml"
		,/*"classpath:ehcache.xml","classpath*:spring-lucene.xml"*/})
public abstract class BaseTest extends AbstractJUnit4SpringContextTests {

}
