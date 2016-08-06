package com.daxiang.android.ui;

import com.daxiang.android.http.okhttp.OkHttpCallback;

/**
 * 
 *
 * @author daxiang
 * @date 2016年8月6日
 * @time 上午11:14:28
 */
public abstract class BaseOkHttpActivity extends BaseActivity implements OkHttpCallback {

	@Override
	public abstract void onRequestSuccess();

	@Override
	public abstract void onRequestFailed();

}
