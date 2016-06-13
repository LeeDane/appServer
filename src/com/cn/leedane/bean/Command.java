package com.cn.leedane.bean;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cn.leedane.Exception.ErrorException;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.StringUtil;

/**
 * 口令实体类
 * @author LeeDane
 * 2015年8月17日 上午9:37:11
 * Version 1.0
 */
@Entity
@Table(name="T_COMMAND")
public class Command extends RecordTimeBean{
	//口令的状态,1：正常，0:禁用，2：过期
	private static final long serialVersionUID = 1L;
	
	private String overdueFormat;  //过期的表达式，目前支持格式：如"1天","1小时","1分钟","1秒钟","1个月","1年"
	
	private Date overdueTime;  //过期时间
	
	private String value;    //值
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name="overdue_format")
	public String getOverdueFormat() {
		
		return overdueFormat;
	}
	
	public void setOverdueFormat(String overdueFormat) {
		this.overdueFormat = overdueFormat;
	}
	

	@Column(name="overdue_time")
	public Date getOverdueTime() {
		if(!StringUtil.isNull(overdueFormat)){
			try {
				return DateUtil.getOverdueTime(new Date(), overdueFormat);
			} catch (ErrorException e) {
				e.printStackTrace();
			}
		}
		return overdueTime;
	}
	public void setOverdueTime(Date overdueTime) {
		
		this.overdueTime = overdueTime;
	}
	
	/*@Override
	public int getStatus() {
		if(overdueTime != null && status != 1){
			if(DateUtil.isOverdue(new Date(), overdueTime)){
				return 2;
			}else{
				return 0;
			}
		}
		return status;
	}*/
}
