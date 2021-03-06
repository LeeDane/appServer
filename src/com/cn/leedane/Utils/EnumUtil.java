package com.cn.leedane.Utils;

/**
 * 枚举工具类
 * @author LeeDane
 * 2016年1月18日 下午12:26:31
 * Version 1.0
 */
public class EnumUtil {
	
	/**
	 * 网络抓取类型
	 * @author LeeDane
	 * 2016年3月22日 上午10:16:46
	 * Version 1.0
	 */
	public enum WebCrawlType {
		全部(""),网易新闻("网易新闻"), 散文网("散文网"), 散文网短篇小说("散文网短篇小说");
	
		private WebCrawlType(String value) {
			this.value = value;
		}
	
		public final String value;
	
	}
	
	/**
	 * 消息通知类型
	 * @author LeeDane
	 * 2016年3月22日 上午10:16:30
	 * Version 1.0
	 */
	public enum NotificationType {
		全部("全部"),艾特我("@我"), 评论("评论"),转发("转发"),赞过我("赞过我"),私信("私信"),通知("通知");
	
		private NotificationType(String value) {
			this.value = value;
		}
	
		public final String value;
	
	}
	
	/**
	 * 数据库表类型
	 * @author LeeDane
	 * 2016年3月22日 上午10:16:30
	 * Version 1.0
	 */
	public enum DataTableType {
		博客("t_blog"),心情("t_mood"), 评论("t_comment"),转发("t_transmit"),赞("t_zan"),用户("t_user"),通知("t_notification"), 
		聊天("t_chat"), 聊天背景("t_chat_bg"), 聊天背景与用户("t_chat_bg_user"), 私信("t_private_chat"),文件("t_file_path"), 
		粉丝("t_fan"), 收藏("t_collection"),关注("t_attention"), 好友("t_friend"), 图库("t_gallery"), 操作日志("t_operate_log"),
		举报("t_report"), 积分("t_score"), 签到("t_sign_in"), 上传("t_upload");
	
		private DataTableType(String value) {
			this.value = value;
		}
		public final String value;
	
	}
	
	/**
	 * 举报类型
	 * @author LeeDane
	 * 2016年3月25日 上午10:18:18
	 * Version 1.0
	 */
	public enum ReportType {
		未知(0),色情低俗(1), 广告骚扰(2),政治敏感(3),违法(4),倾诉投诉(5);
	
		private ReportType(int value) {
			this.value = value;
		}
	
		public final int value;
	
	}
	
	/**
	 * 时间类型
	 * @author LeeDane
	 * 2016年3月22日 上午10:17:01
	 * Version 1.0
	 */
	public enum TimeScope {
		昨日("1"), 当日("2"), 本周("3"), 本月("4"), 本年("5");

		private TimeScope(String value) {
			this.value = value;
		}

		public final String value;

	}

	/**
	 * 获取TimeScope对象
	 * @param type
	 * @return
	 */
	public static TimeScope getTimeScope(String type){
		for(TimeScope ts: TimeScope.values()){
			if(ts.value.equals(type)){
				return ts;
			}
		}
		return null;
	}
	
	/**
	 * 服务器返回码
	 * @author LeeDane
	 * 2016年1月20日 上午10:17:54
	 * Version 1.0
	 */
	public enum ResponseCode{
		请先登录(1001),
		邮件已发送成功(1002),
		不是未注册状态邮箱不能发注册码(1003),
		邮件发送失败(1004),
		暂时不支持手机找回密码功能(1005),
		暂时不支持邮箱找回密码功能(1006),
		未知的找回密码类型(1007),
		注销成功(1008),
		账号已被禁用(1009),
		账号未被激活(1010),
		请先完善账号信息(1011),
		账号已被禁言(1012),
		账号已被注销(1013),
		服务器处理异常(500),
		链接不存在(400),
		文件不存在(2001),
		操作文件失败(2002),
		某些参数为空(2003),
		缺少参数(2004),
		JSON数据解析失败(2005),
		禁止访问(2006),
		系统维护时间(2007),
		文件上传失败(2008),
		没有操作实例(2009),
		没有操作权限(2010),
		没有下载码(2011),
		下载码失效(2012),
		数据库保存失败(2013),
		添加的记录已经存在(2014),
		数据库删除数据失败(2015),
		操作对象不存在(2016),
		参数信息不符合规范(2017),
		缺少请求参数(2018),
		该博客不存在(2019),
		标签添加成功(2020),
		标签长度不能超过5位(2021),
		用户不存在或请求参数不对(3001),
		用户已经注销(3002),
		请先验证邮箱(3003),
		恭喜您登录成功(3004),
		账号或密码不匹配(3005),
		注册失败(3006),
		该邮箱已被占用(3007),
		该用户已被占用(3008),
		该手机号已被注册(3009),
		操作过于频繁(3010),
		手机号为空或者不是11位数(3011),
		操作成功(3012),
		操作失败(3013),
		该通知类型不存在(3014),
		用户名不能为空(3015),
		密码不能为空(3016),
		两次密码不匹配(3017),
		检索关键字不能为空(3018),
		原密码错误(3019),
		要修改的密码跟原密码相同(3020),
		新密码修改成功(3021),
		系统检测到有敏感词(3022),
		请填写每次用户每次下载扣取的积分(3023),
		自己上传的聊天背景资源(3023),
		聊天内容不能为空(3024),
		您还没有绑定电子邮箱(3025),
		对方还没有绑定电子邮箱(3026),
		该用户不存在(3027),
		邮件发送成功(3028),
		参数不存在或为空(3029),
		该资源现在不支持评论(3030),
		该资源现在不支持转发(3031),
		更新评论状态成功(3032),
		更新转发状态成功(3033),
		更新评论状态失败(3034),
		更新转发状态失败(3035),
		删除通知成功(3036),
		删除通知失败(3037),
		私信内容不能为空(3038),
		好友关系不存在(3039),
		好友关系不是待确认状态(3040),
		关注成功(3041),
		解除好友关系成功(3042),
		添加好友失败(3043),
		不能添加自己为好友(3044),
		删除的通知不存在(3045),
		删除的聊天记录不存在(3046),
		删除聊天记录成功(3047),
		删除聊天记录失败(3048),
		心情图片链接处理失败(3049),
		发表心情成功(3050)
		;
		
		private ResponseCode(int value) {
			this.value = value;
		}

		public final int value;
	}
	
	/**
	 * 根据值获取ResponseCode的key值
	 * @param code
	 * @return
	 */
	public static String getResponseValue(int code){
		for(ResponseCode rc: ResponseCode.values()){
			if(rc.value == code){
				return rc.name();
			}
		}
		return "";
	}
	
	/**
	 * 获取NotificationType对象
	 * @param type
	 * @return
	 */
	public static NotificationType getNotificationType(String type){
		for(NotificationType nt: NotificationType.values()){
			if(nt.value.equals(type)){
				return nt;
			}
		}
		return null;
	}
	
	/**
	 * 获取ReportType对象的key值
	 * @param type
	 * @return
	 */
	public static String getReportType(int type){
		for(ReportType nt: ReportType.values()){
			if(nt.value == type){
				return nt.name();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getResponseValue(1001));
	}
}
