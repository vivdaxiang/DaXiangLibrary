package com.daxiang.android.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * 文件操作工具；
 * 
 * @author daxiang
 * @date 2015-5-27
 * 
 */
public class FileUtils {

	/**
	 * SdCard是否可以使用；
	 * 
	 * @return true，可以使用；false，不能使用；
	 */
	public static boolean getSdCardState() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取内存储中的应用文件目录；
	 * 
	 * @param context
	 * @return
	 */
	public static File getInternalFileDirs(Context context) {

		return context.getFilesDir();
	}

	/**
	 * 获取内存储中的应用缓存目录；
	 * 
	 * @param context
	 * @return
	 */
	public static File getInternalCacheDirs(Context context) {

		return context.getCacheDir();
	}

	/**
	 * 获取SdCard中的应用文件目录；如果SdCard不可用，返回内存储中的应用文件目录；also see
	 * {@link getInternalFileDirs}
	 * 
	 * @param context
	 * @return
	 */
	public static File getExternalFileDirs(Context context) {
		if (getSdCardState()) {
			return context.getExternalFilesDir(null);
		}
		return getInternalFileDirs(context);

	}

	/**
	 * 获取SdCard中的应用缓存目录；如果SdCard不可用，返回内存储中的应用缓存目录；also see
	 * {@link getInternalCacheDirs}
	 * 
	 * @param context
	 * @return
	 */
	public static File getExternalCacheDirs(Context context) {
		if (getSdCardState()) {
			return context.getExternalCacheDir();
		}
		return getInternalCacheDirs(context);
	}
}
