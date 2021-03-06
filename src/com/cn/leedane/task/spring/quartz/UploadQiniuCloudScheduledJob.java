package com.cn.leedane.task.spring.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.cn.leedane.task.spring.scheduling.UploadQiniuCloud;

/**
 * 上传文件到七牛云存储服务器的调度任务
 * @author LeeDane
 * 2015年11月23日 上午10:28:41
 * Version 1.0
 */
public class UploadQiniuCloudScheduledJob extends QuartzJobBean {

	private UploadQiniuCloud uploadQiniuCloud; 

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			uploadQiniuCloud.upload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUploadQiniuCloud(UploadQiniuCloud uploadQiniuCloud) {
		this.uploadQiniuCloud = uploadQiniuCloud;
	}
}
