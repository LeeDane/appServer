package com.cn.leedane.test;

import java.io.IOException;
import java.util.Set;

import com.cn.leedane.Utils.StringUtil;

/**
 * main方法相关的测试类
 * @author LeeDane
 * 2015年7月3日 下午6:28:01
 * Version 1.0
 */
public class MainTest {

	public static void main(String[] args) throws IOException {
		String str = "@d哈 nihao @天天让人  @123";
		Set<String> set = StringUtil.getAtUserName(str);
		if(set != null && set.size()> 0){
			for(String s: set){
				System.out.println(s);
			}
		}
	}	

}
