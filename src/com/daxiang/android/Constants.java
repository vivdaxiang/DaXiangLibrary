package com.daxiang.android;

/**
 * 常量类；
 * 
 * @author daxiang
 * @date 2015-5-8
 * 
 */
public class Constants {

	/**
	 * SharedPreferences的名字；
	 */
	public static final String GLOBAL_SHARED_PREFERS = "global_config";

	/**
	 * 服务器端保存最新apk版本名的文件的名字；
	 */
	public static final String WEB_VERSION = "webVersion.json";
	/**
	 * 用户的头像的存储文件名；
	 */
	public static final String USER_PHOTO = "userphoto.png";

	/**
	 * 分页加载每页的条目数；
	 */
	public static final int PAGE_SIZE = 10;

	// 项目中使用到的颜色，在application中初始化；
	public static int RED;
	public static int GREEN;
	public static int ORANGE;
	/**
	 * 深蓝色，比如title背景色；
	 */
	public static int DEEP_BLUE;
	/**
	 * 浅蓝色，比如文本的蓝色；
	 */
	public static int LIGHT_BLUE;
	public static int GRAY;
	/**
	 * 布局的背景色；
	 */
	public static int LAYOUT_BG;

}
