package com.cn.leedane.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.UserBean;

/**
 * 文件路径service接口类
 * @author LeeDane
 * 2015年11月22日 下午11:54:11
 * Version 1.0
 */
public interface FilePathService <T extends Serializable> extends BaseService<FilePathBean>{
	
	/**
	 * 将base64单个临时保存的文件转化成文件路径保存在FilePath表中
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean saveEachTemporaryBase64ToFilePath(JSONObject jo, UserBean user, HttpServletRequest request)  throws Exception;
	
	/**
	 * 获取单张图片的base64字符串
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String downloadBase64Str(JSONObject jo, UserBean user, HttpServletRequest request) throws Exception;

	/**
	 * 获取单张图片的图片列表信息
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public String getOneMoodImgs(JSONObject jo, UserBean user,
			HttpServletRequest request);
	
	/**
	 * 保存每一个filePath对象
	 * 里面会根据默认的分辨率分别生成多个不同固定大小的图片
	 * @param order
	 * @param base64
	 * @param user
	 * @param uuid
	 * @param tableName 表的名称
	 * @throws Exception
	 */
	public boolean saveEachFile(int order, String base64, UserBean user, String uuid, String tableName) throws Exception;
	
	/**
	 * 保存每一个filePath对象
	 * @param order
	 * @param base64
	 * @param user
	 * @param uuid
	 * @param tableName
	 * @param sourcePath
	 * @return
	 * @throws Exception
	 */
	public boolean saveEachFile(int order, String base64, UserBean user, String uuid, String tableName, String sourcePath) throws Exception;

	/**
	 * 分页获取该用户图片文件的路径列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getUserImageByLimit(JSONObject jo,
			UserBean user, HttpServletRequest request);
	
	/**
	 * 保存源文件和其他不同分辨率大小的文件(filePath最好不要存放在file文件夹下)
	 * @param filePath
	 * @param user
	 * @param uuid
	 * @param tableName
	 * @param order
	 * @param version
	 * @param desc
	 * @return
	 */
	public boolean saveSourceAndEachFile(String filePath, UserBean user, String uuid, String tableName, int order, String version, String desc);
	
	/**
	 * 检验文件是否可以被下载
	 * @param fileOwnerId
	 * @return
	 */
	public boolean canDownload(int fileOwnerId, String fileName);
	
	/**
	 * 分页获取该用户上传的文件的路径列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getUploadFileByLimit(JSONObject jo,
			UserBean user, HttpServletRequest request);
	
	/**
	 * 更新标记该文件已经上传到存储服务器
	 * @param fId
	 * @param qiniuPath
	 * @return
	 */
	public boolean updateUploadQiniu(int fId, String qiniuPath);
}
