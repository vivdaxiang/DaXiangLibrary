package com.daxiang.android;

import android.app.Application;

/**
 * 
 * @author daxiang
 * 
 *         2015-4-1
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		/*
		 * TaskExecutorConfiguration configuration = new
		 * TaskExecutorConfiguration.Builder( this).threadPoolSize(3).build();
		 * 
		 * TaskExecutor.getInstance().init(configuration);
		 */
	}
}
