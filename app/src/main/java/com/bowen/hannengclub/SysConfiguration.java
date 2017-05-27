package com.bowen.hannengclub;

/**
 * Created by 肖稳华 on 2017/4/18.
 * 系统配置项
 */
public interface SysConfiguration {
//	public static final String BASE_URL = "https://www.baidu.com/";
//	public static final String BASE_URL = "https://apidev.hannengclub.com/";
	//正式环境
	public static final String BASE_URL = "https://api.hannengclub.com/";
	//正式环境H5配置
	public static final String BASE_WEB_URL = "http://m.hannengclub.com/";
	//H5测试地址 "http://222.171.202.3:7002/"
//	public static final String BASE_URL = "https://kyfw.12306.cn/otn/leftTicket/init/";

	public static final String USER_INFO = "hannengclub_user";

	public static Boolean DEBUG = true;
}
