package com.daxiang.android.http;

/**
 * Http相关的常量类；
 * 
 * @author daxiang
 * 
 *         2015-3-24
 */
public class HttpConstants {
	/**
	 * 网络请求的数据获取模式：from net or from cache；
	 * 
	 * @author daxiang
	 * 
	 *         2015-3-24
	 */
	public static class NetDataProtocol {
		// 直接从网络取数据不需要本地存储
		public final static int DATA_FROM_NET_NO_CACHE = 10000;
		// 直接从网络取数据然后本地存储
		public final static int DATA_FROM_NET_AND_CACHE = 10001;

		// 直接从本地存储拿数据
		public final static int DATA_FROM_CACHE = 10002;
		// 先从本地存储取数据填充界面，然后从网络取数据更新界面并本地存储
		public final static int DATA_FROM_CACHE_THEN_NET = 10003;
		public final static int LOAD_MISTAKE = 10004;
		public final static int LOAD_SUCCESS = 10005;
	}

	/**
	 * 网络请求类型；
	 * 
	 * @author daxiang
	 * 
	 *         2015-3-24
	 */
	public enum HttpMethod {
		GET, POST, DELETE
	}

	// 状态变量
	public static class NetState {
		/**
		 * 默认只能wifi下载
		 */
		public static boolean downloadOnlyWifi = false;
		public static boolean isWifiState = true;
	}

}
