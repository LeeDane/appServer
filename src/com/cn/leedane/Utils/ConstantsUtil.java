package com.cn.leedane.Utils;

/**
 * 常量工具类
 * @author LeeDane
 * 2015年9月2日 下午5:28:00
 * Version 1.0
 */
public class ConstantsUtil {

	private ConstantsUtil(){
		
	}
	/**
	 * 网站的名称
	 */
	public static final String WEBSIT_NAME = "LeeDane精品网";
	
	/**
	 * 系统服务器的链接地址
	 */
	public static final String SYSTEM_SERVER_URL = "http://leedane.tunnel.mobi";
	
	/**
	 * 七牛云存储服务器的链接地址
	 */
	public static final String QINIU_SERVER_URL = "http://7xnv8i.com1.z0.glb.clouddn.com/";
	
	//状态相关
	/**
	 * 草稿状态
	 */
	public static final int STATUS_DRAFT = -1;
	
	/**
	 * 禁用状态
	 */
	public static final int STATUS_DISABLE = 0;
	
	/**
	 * 正常状态
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 * 删除状态
	 */
	public static final int STATUS_DELETE = 2;
	//状态相关结束
	//session相关
	/**
	 * 用户账号session中的常量
	 */
	public static String USER_ACCOUNT_SESSION = "account";
	
	/**
	 * 用户对象session中的常量
	 */
	public static String USER_SESSION = "user";
	
	/**
	 * 我的好友对象在session中的常量
	 */
	public static String MY_FRIENDS = "myfriends";
	//session相关结束
	//图像相关
	/**
	 * base64图片的头字符串，不加上这个在页面上显示不了
	 */
	public static final String BASE64_JPG_IMAGE_HEAD = "data:image/jpg;base64,";
	
	/**
	 * base64图片的头字符串，不加上这个在页面上显示不了
	 */
	public static final String BASE64_PNG_IMAGE_HEAD = "data:image/jpeg;base64,";
	
	//图像相关的结束
	
	/**
	 * 页面所有的标签列表
	 */
	public static final String[] HTML_TAG_LISTS = {/*"!DOCTYPE",*/ "a", "abbr", "acronym", "address", "applet", "area"
			, "b", "base", "basefont", "bdo", "big", "blockquote", "body", "br", "button", "caption", "center"
			, "cite", "code", "col", "colgroup", "dd", "del", "dfn", "dir", "div", "dl", "dt", "em", "fieldset"
			, "font", "form", "frame", "frameset", "head", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "html"
			, "i", "iframe", "img", "input", "ins", "kbd", "label", "legend", "li", "link", "map", "menu", "meta"
			, "noframes", "noscript", "object", "ol", "optgroup", "option", "p", "param", "pre", "q", "s"
			, "samp", "script", "select", "small", "span", "strike", "strong", "style", "sub", "sup", "table"
			, "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "tt", "u", "ul", "var", "isindex"
			//html5相关的标签
			, "xmp", "article", "header", "nav", "section", "aside", "hgroup", "figure", "figcaption", "footer"
			, "dialog", "video", "audio", "source", "canvas", "embed", "menu", "menuitem", "command", "meter"
			, "progress", "datalist", "details", "ruby", "rp", "rt", "keygen", "mark", "output"}; 
 
	/**
	 * 系统语言(中文)
	 */
	public static final String SYSTEM_LANGUAGE_CN = "CN";
	
	/**
	 * 系统语言(英文)
	 */
	public static final String SYSTEM_LANGUAGE_EN = "EN";
	
	/**
	 * 发送邮箱用户默认的账号
	 */
	public static final String DEFAULT_USER_FROM_ACCOUNT = "leedane";
	
	/**
	 * 发送邮箱用户默认的中文名
	 */
	public static final String DEFAULT_USER_FROM_CHINANAME = "LeeDane官方";
	
	/**
	 * 发送邮箱用户默认的邮件地址
	 */
	public static final String DEFAULT_USER_FROM_EMAIL = "642034701@qq.com";
	
	/**
	 * 发送邮箱用户默认的QQ账号
	 */
	public static final String DEFAULT_USER_FROM_QQ = "642034701";
	
	/**
	 * 发送邮箱用户默认的邮件地址
	 */
	public static final String DEFAULT_USER_FROM_QQPSW = "irqcnluikwnubejg";
	
	/**
	 * 极光推送Master Secret
	 */
	public static final String JPUSH_MASTER_SECRET = "8cf0c594b18e376c667f61a8";
	
	/**
	 * 极光推送AppKey
	 */
	public static final String JPUSH_APPKEY = "52a273ed4a53533801809f1b";
	
	/**
	 * 支持的图片类型
	 */
	public final static String[] SUPPORTIMAGESUFFIXS = {"png","jpg","bmp","gif","psd","jpeg"};
	
	/**
	 * 本系统默认保存文件的根目录
	 */
	public final static String DEFAULT_SAVE_FILE_FOLDER = "C://webroot//";
	
	/**
	 * 本系统默认每页获取的大小
	 */
	public final static int DEFAULT_PAGE_SIZE = 5;
	
	/**
	 * 本系统默认获取图片的大小
	 */
	public final static String DEFAULT_PIC_SIZE = "source";
	
	/**
     * 上传标记名称
     */
    public static final String UPLOAD_FILE_TABLE_NAME = "my_upload_file";
    
    /**
     * 上传app版本标记名称
     */
    public static final String UPLOAD_APP_VERSION_TABLE_NAME = "app_version";
    
    /**
     * 默认博客请求获取的条数
     */
    public static final int DEFAULT_BLOG_SEARCH_ROWS = 5;
    
    /**
     * 默认博客请求获取的条数
     */
    public static final int DEFAULT_MOOD_SEARCH_ROWS = 5;
    
    /**
     * 默认博客请求获取的条数
     */
    public static final int DEFAULT_USER_SEARCH_ROWS = 10;
    /**
     * 好友在redis存储的前缀
     */
    public static final String FRIEND_REDIS = "t_friend_";
    
    /**
     * 好友ID在redis存储的前缀
     */
    public static final String FRIEND_ID_REDIS = "t_friend_id_";
    /**
     * 赞在redis存储的前缀
     */
    public static final String ZAN_REDIS = "t_zan_";
    /**
     * 赞用户在redis存储的前缀
     */
    public static final String ZAN_USER_REDIS = "t_zan_user_";
    
    /**
     * 评论在redis存储的前缀
     */
    public static final String COMMENT_REDIS = "t_comment_";
    /**
     * 转发在redis存储的前缀
     */
    public static final String TRANSMIT_REDIS = "t_transmit_";
    
    /**
     * 心情在redis存储的前缀
     */
    public static final String MOOD_REDIS = "t_mood_";
    
    /**
     * 心情图像在redis存储的前缀
     */
    public static final String MOOD_IMGS_REDIS = "t_mood_imgs_";
    
    /**
     * 心情单张图像在redis存储的前缀
     */
    public static final String MOOD_IMG_REDIS = "t_mood_img_";
    
    /**
     * 用户是否第一次签到在redis存储的前缀
     */
    public static final String FIRST_SIGN_IN_REDIS = "first_sign_in_";
    
    /**
     * 博客在redis存储的前缀
     */
    public static final String BLOG_REDIS = "t_blog_";
    
    /**
     * 用户所有好友(包括自己)时间线在redis存储的前缀
     */
    public static final String TIME_LINE = "time_line_";
    
    /**
     * 用户个人(没有好友)时间线在redis存储的前缀
     */
    public static final String TIME_LINE_SELF = "time_line_self_";
    
    /**
     * 用户下载资源记录在redis存储的前缀
     */
    public static final String CHAT_BG_USER = "chat_bg_user_";
    
    /**
     * 微信当前用户在redis存储的前缀
     */
    public static final String WECHAT_USER_REDIS = "wechat_user_";
    
    /**
     * 资源找不到后的提示信息
     */
    public static final String SOURCE_DELETE_TIP = "该资源已经不存在,有异议,请联系管理员核实";
}
