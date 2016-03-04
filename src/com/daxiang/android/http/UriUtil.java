package com.daxiang.android.http;

/**
 * Url生成工具；
 * 
 * @author daxiang
 * @date 2015-5-29
 * 
 */
public class UriUtil {
	/**
	 * 开发、测试的内网地址；
	 */
	public static final String DEBUG_SERVER_URI = "";

	/**
	 * 开发、测试的外网地址；
	 */
	public static final String WWW_DEBUG_SERVER_URI = "";
	/**
	 * 生产地址；
	 */
	public static final String OFFCIAL_SERVER_URI = "";

	/**
	 * 主机域名；
	 */
	public static final String DOMAIN = DEBUG_SERVER_URI;
	/**
	 * URL前缀；
	 */
	public static final String SERVER_URI = DOMAIN + "/api/";

	/**
	 * 登录，POST请求；
	 * 
	 * @return
	 */
	public static String getLoginUri() {
		return SERVER_URI + "login";
	}

	/**
	 * 解股页搜索问题；
	 * 
	 * @return
	 */
	public static String getJieGuQuestionSearchListUri(int page, int pageSize, String keywords) {
		return String.format(SERVER_URI + "question/list?pageNo=%d&pageSize=%d&keywords=%s", page, pageSize, keywords);
	}

}
