package com.daxiang.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络判断工具；
 * 
 * @author daxiang
 * @date 2015-6-9
 * 
 */
public class NetworkUtils {

	/**
	 * 网络连接是否可用；
	 * 
	 * @param context
	 * @return true，可用；false，不可用；
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (null != info) {
			return info.isConnected();
		}
		return false;
	}

	/**
	 * WIFI连接是否可用；
	 * 
	 * @param context
	 * @return true，可用；false，不可用；
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiInfo != null && wifiInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 移动网络连接是否可用；
	 * 
	 * @param context
	 * @return true，可用；false，不可用；
	 */
	public static boolean isMobileConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetInfo != null && mobileNetInfo.isConnected()) {
			return true;
		}
		return false;
	}
}
