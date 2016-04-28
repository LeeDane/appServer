package com.cn.leedane.test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.cn.leedane.Utils.Base64ImageUtil;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.observer.ConcreteWatched;
import com.cn.leedane.observer.ConcreteWatcher;
import com.cn.leedane.observer.Watched;
import com.cn.leedane.observer.Watcher;
import com.cn.leedane.observer.template.UpdateMoodTemplate;
import com.cn.leedane.service.MoodService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;

/**
 * 心情相关的测试类
 * @author LeeDane
 * 2015年11月24日 上午11:23:41
 * Version 1.0
 */
public class MoodTest extends BaseTest {
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	@Resource
	private UserService<UserBean> userService;
	
	@Resource
	private MoodService<MoodBean> moodService;

	@Test
	public void sendDraft() throws Exception{
		long start = System.currentTimeMillis();
		for(int i = 0; i < 1000; i++){
			UserBean user = userService.findById(1);	
			String filePath = ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER +"liudehua.jpg";
			String base64 = Base64ImageUtil.convertImageToBase64(filePath, null);
			StringBuffer buffer = new StringBuffer();
			buffer.append(base64);
			String str = "{\"froms\":\"android客户端\", \"content\":\"今天天气不错哦!!!\", \"base64\":\""+buffer.toString()+"\"}";
			JSONObject jsonObject = JSONObject.fromObject(str);
			moodService.saveMood(jsonObject, user, ConstantsUtil.STATUS_NORMAL, null);
		}
		long end = System.currentTimeMillis();
		System.out.println("最终总耗时：" +(end - start) +"毫秒");
	}
	
	@Test
	public void notifyMoodUpdate(){
		Watched watched = new ConcreteWatched();       
        Watcher watcher = new ConcreteWatcher();
        watched.addWatcher(watcher);
        //int i = 10/0;
        watched.notifyWatchers(userService.findById(1), new UpdateMoodTemplate());
	}
	@Test
	public void getMoodByLimit(){
		UserBean user = userService.loadById(1);
		//String str = "{\"uid\": 1, \"pageSize\": 10, \"method\":\"lowloading\", \"last_id\":19, \"pic_size\":\"30x30\"}";
		String str = "{\"account\":\"leedane\",\"uid\":2,\"method\":\"firstloading\",\"login_mothod\":\"android\",\"no_login_code\":\"14480951808066e31568670e51be42bc7978cc2066ea060.521926355594616\",\"pageSize\":10}";
		JSONObject jo = JSONObject.fromObject(str);
		try {
			Map<String, Object> ls = moodService.getMoodByLimit(jo, user, null);
			System.out.println("总数:" +ls.size());
			for(Entry<String, Object> entry :ls.entrySet()){
				System.out.println(entry.getKey() +":" +entry.getValue());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateMoodStatus(){
		UserBean user = userService.loadById(1);
		String str = "{\"mid\":1037, \"login_mothod\":\"android\",\"no_login_code\":\"14480951808066e31568670e51be42bc7978cc2066ea060.521926355594616\",\"pageSize\":10}";
		JSONObject jo = JSONObject.fromObject(str);
		try {
			Map<String, Object> message = moodService.updateMoodStatus(jo, 1, null, user);
			System.out.println("isUpdate:" +message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void query(){
		
	}
}
