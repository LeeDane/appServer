package com.cn.leedane.test;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.FilePathService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;

/**
 * 文件相关的测试类
 * @author LeeDane
 * 2016年2月29日 下午4:26:39
 * Version 1.0
 */
public class FilePathTest extends BaseTest {
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	@Resource
	private UserService<UserBean> userService;
	
	@Resource
	private FilePathService<FilePathBean> filePathService; 
	@Test
	public void testupload(){
		doTestupload();
	}
	
	/**
	 * 测试上传
	 */
	public void doTestupload() {
		if(filePathService != null){
			List<FilePathBean> filePathBeans = filePathService.executeHQL("FilePathBean", " where is_upload_qiniu = 0 ");

			if(filePathBeans != null && filePathBeans.size() > 0){			
				
				for(FilePathBean filePathBean: filePathBeans){					
					try {
					    //更新状态标记为已经上传
					    filePathBean.setUploadQiniu(ConstantsUtil.STATUS_NORMAL);
					    filePathBean.setQiniuPath(ConstantsUtil.QINIU_SERVER_URL + filePathBean.getPath());
					    filePathService.update(filePathBean);
					    System.out.println("上传七牛云存储服务器成功,文件本地路径：" + filePathBean.getPath());
					}catch (Exception e) {
						e.printStackTrace();
						System.out.println("上传七牛云存储服务器失败,文件本地路径：" + filePathBean.getPath());
						continue;
					}
				}
			}else{
				System.out.println("没有要上传到七牛云存储服务器的文件");
			}
		}else{
			System.out.println("filePathService为空");
		}
		
	}
}
