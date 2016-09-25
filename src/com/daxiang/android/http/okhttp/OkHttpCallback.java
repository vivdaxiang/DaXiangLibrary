package com.daxiang.android.http.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 
 * 
 * @author daxiang
 * @date 2016年8月6日
 * @time 上午11:13:33
 */
public interface OkHttpCallback {

	public void onFailed(Call call, IOException e, int requestCode);

	public void onSuccess(Call call, Response response, int requestCode);

}
