package com.daxiang.android.http.okhttp;

import okhttp3.OkHttpClient;

/**
 * 
 *
 * @author daxiang
 * @date 2016年8月6日
 * @time 下午3:07:04
 */
public class OkHttpManager {

	private static OkHttpClient mInstance;

	private OkHttpManager() {

	}

	public static OkHttpClient getInstance() {
		if (mInstance == null) {
			synchronized (OkHttpManager.class) {
				if (mInstance == null) {
					mInstance = new OkHttpClient.Builder().build();
				}
			}
		}

		return mInstance;
	}
}
