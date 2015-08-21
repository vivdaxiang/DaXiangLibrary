package com.daxiang.android.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

/**
 * 获取手机相关信息的工具；
 * 
 * @author daxiang
 * @date 2015-6-7
 * 
 */
public class DeviceUtils {

	public static int getDeviceWidth(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// mScreenWidth = metrics.widthPixels;
		// context.getResources().get
		return 0;
	}

	/**
	 * 获取应用的当前versionName；
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentVersionName(Context context) {
		String versionName = null;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);

			versionName = info.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取应用的当前versionCode；
	 * 
	 * @param context
	 * @return
	 */
	public static int getCurrentVersionCode(Context context) {
		int versionCode = 0;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);

			versionCode = info.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * app是否运行在前台；调用该方法需添加权限："android.permission.GET_TASKS"；
	 * 
	 * @param context
	 * @return true，运行在前台；false，不在前台；
	 */
	public static boolean isAppInForeground(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (null != tasks && !tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}

		return false;
	}
}
