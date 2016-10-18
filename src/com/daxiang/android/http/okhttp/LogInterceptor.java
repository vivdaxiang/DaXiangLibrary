package com.daxiang.android.http.okhttp;

import java.io.IOException;

import com.daxiang.android.utils.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 日志拦截器；
 * <p>
 * Interceptors can be chained.OkHttp uses lists to track interceptors, and
 * interceptors are called in order.
 * <p>
 * OKHttp的拦截器包含两个级别：Application Interceptors 和Network
 * Interceptors，详情参见官方wiki：https://github.com/square/okhttp/wiki/Interceptors。
 * 
 * @author daxiang
 * @date 2016年8月2日
 */
public class LogInterceptor implements Interceptor {

	private static final String TAG = LogInterceptor.class.getSimpleName();

	@Override
	public Response intercept(Interceptor.Chain chain) throws IOException {
		Request request = chain.request();

		long t1 = System.nanoTime();
		Logger.i(TAG, String.format("Sending request %s on %s%n%s",
				request.url(), chain.connection(), request.headers()));

		Response response = chain.proceed(request);

		long t2 = System.nanoTime();
		Logger.i(TAG, String.format("Received response for %s in %.1fms%n%s",
				response.request().url(), (t2 - t1) / 1e6d, response.headers()));
		// ResponseBody的原版注释：Never null, must be closed after consumption, can
		// be consumed only once；只能被消耗一次，如果这里使用了，UI层就会收不到响应体的内容。
		// Logger.i(TAG, "Received response content: " +
		// response.body().string());

		return response;
	}
}
