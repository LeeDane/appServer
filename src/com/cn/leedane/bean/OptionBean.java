package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 选项配置类
 * @author LeeDane
 * 2015年4月3日 上午9:54:29
 * Version 1.0
 */
@Entity
@Table(name="T_OPTION")
public class OptionBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	//状态：-1：草稿 0：禁止  1：正常 
	/**
	 * 选项的键名
	 */
	private String optionKey;
	
	/**
	 * 选择的键值
	 */
	private String optionValue; 
	
	/**
	 * 选项的版本
	 */
	private float version;
	
	private String desc;
	
	
	@Column(name="option_key")
	public String getOptionKey() {
		return optionKey;
	}
	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}
	
	@Column(name="option_value")
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	
	@Column(nullable=false)
	public float getVersion() {
		return version;
	}
	public void setVersion(float version) {
		this.version = version;
	}
	
	@Column(name="option_desc")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
