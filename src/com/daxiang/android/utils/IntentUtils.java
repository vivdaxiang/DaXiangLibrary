package com.daxiang.android.utils;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 调用系统功能界面的工具；
 *
 * @author daxiang
 * @date 2016年1月8日
 * @time 下午2:37:16
 */
public class IntentUtils {

	/**
	 * 打开网络设置页面；
	 * 
	 * @param context
	 */
	public static void openWirelessSettings(Context context) {
		if (context == null)
			return;

		Intent intent = null;
		// 判断手机系统的版本 即API大于10 就是3.0或以上版本
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(intent);
	}

	/**
	 * 打电话界面；
	 * 
	 * @param context
	 * @param tel
	 */
	public static void goToCall(Context context, String tel) {

		if (context == null)
			return;

		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
		context.startActivity(intent);
	}

	/**
	 * 拨号界面；
	 * 
	 * @param context
	 * @param tel
	 */
	public static void goToDial(Context context, String tel) {

		if (context == null)
			return;

		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 发短信页面；
	 * 
	 * @param context
	 * @param tel
	 * @param smsBody
	 */
	public static void sendSMS(Context context, String tel, String smsBody) {

		if (context == null)
			return;

		Uri uri = Uri.parse("smsto:" + tel);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", smsBody);
		context.startActivity(intent);
	}

	/**
	 * 安装apk；
	 * 
	 * @param context
	 * @param file
	 */
	public static void goToInstallAPK(Context context, File file) {

		if (context == null || file == null)
			return;

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 卸载apk；
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void goToUninstallAPK(Context context, String packageName) {

		if (context == null || TextUtils.isEmpty(packageName))
			return;

		Uri uri = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		context.startActivity(intent);
	}

	/**
	 * 要打开那种未在桌面创建icon的APP，可以使用此方法；
	 * 
	 * @param context
	 * @param packageName
	 * @param className
	 * @param extras
	 */
	public static void startUpApp(Context context, String packageName, String className, Bundle extras) {

		if (context == null || TextUtils.isEmpty(packageName) || TextUtils.isEmpty(className))
			return;

		Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		// ---若一个APP的启动activity不添加此属性，该APP就不会在桌面创建icon；
		ComponentName componentName = new ComponentName(packageName, className);
		intent.setComponent(componentName);
		if (extras != null) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}

}
