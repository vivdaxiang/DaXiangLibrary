package com.daxiang.android.http.okhttp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.os.Message;

import com.daxiang.android.http.HttpConstants;
import com.daxiang.android.http.HttpConstants.HttpMethod;
import com.daxiang.android.http.ssl.https.MyHostnameVerifier;
import com.daxiang.android.http.ssl.https.X509TrustManagerImpl;

/**
 * 
 * 
 * @author daxiang
 * @date 2016年8月6日
 * @time 下午3:07:04
 */
public class OkHttpManager {
	//遗留问题：在handleSuccess（）中如何处理响应体是字节数据或者是输入流的情况，比如下载文件？
	private static OkHttpClient mInstance;
	private static boolean mUseHttps;
	private static String httpsHostName;
	private static InputStream keystoreFile;
	private static String keystorePwd;

	private OkHttpManager() {

	}

	public static void useHttps(boolean useHttps) {
		mUseHttps = useHttps;
	}

	public static void httpsHostName(String hostName) {
		httpsHostName = hostName;
	}

	public static void keystoreFile(String file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		keystoreInput(input);
	}

	public static void keystoreInput(InputStream input) {
		keystoreFile = input;
	}

	public static void keystorePwd(String pwd) {
		keystorePwd = pwd;
	}

	public static OkHttpClient init() {
		if (mInstance == null) {
			synchronized (OkHttpManager.class) {
				if (mInstance == null) {
					if (!mUseHttps) {
						mInstance = new OkHttpClient.Builder().build();
					} else {
						mInstance = initHttpsClient();
					}

				}
			}
		}

		return mInstance;
	}

	public static OkHttpClient initHttpsClient() {
		X509TrustManager trustManager = null;
		SSLSocketFactory sslSocketFactory = null;
		try {
			trustManager = new X509TrustManagerImpl(keystoreFile, keystorePwd);
			TrustManager[] tms = { trustManager };
			// Use factory
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tms, null);
			sslSocketFactory = ctx.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		OkHttpClient client = new OkHttpClient.Builder()
				.hostnameVerifier(new MyHostnameVerifier(httpsHostName))
				.sslSocketFactory(sslSocketFactory, trustManager).build();
		return client;
	}

	public static Call sendRequest(OkHttpRequest okHttpRequest) {
		if (HttpMethod.GET == okHttpRequest.requestMethod()) {
			return getAsync(okHttpRequest);
		} else if (HttpMethod.POST == okHttpRequest.requestMethod()) {
			return postAsync(okHttpRequest);
		}
		return null;
	}

	private static Call postAsync(final OkHttpRequest okHttpRequest) {
		Request request = new Request.Builder().url(okHttpRequest.url())
				.post(okHttpRequest.requestBody()).build();
		Call call = mInstance.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// 这里试一试反射+注解的方式如何？就像EventBus那样；
				handleSuccess(okHttpRequest, call, response);
				// okHttpRequest.okHttpCallback.onSuccess(call, response,
				// okHttpRequest.requestCode());
			}

			@Override
			public void onFailure(Call call, IOException e) {
				handleFailed(okHttpRequest, call, e);
				// okHttpRequest.okHttpCallback.onFailed(call, e,
				// okHttpRequest.requestCode());
			}
		});
		return call;
	}

	private static Call getAsync(final OkHttpRequest okHttpRequest) {
		Request request = new Request.Builder().url(okHttpRequest.url())
				.build();
		Call call = mInstance.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// 这里试一试反射+注解的方式如何？就像EventBus那样；
				handleSuccess(okHttpRequest, call, response);
				// okHttpRequest.okHttpCallback.onSuccess(call, response,
				// okHttpRequest.requestCode());
			}

			@Override
			public void onFailure(Call call, IOException e) {
				handleFailed(okHttpRequest, call, e);
				// okHttpRequest.okHttpCallback.onFailed(call, e,
				// okHttpRequest.requestCode());
			}
		});

		return call;
	}

	private static void handleSuccess(OkHttpRequest okHttpRequest, Call call,
			Response response) {
		OkHttpResponse result = new OkHttpResponse();
		result.setCall(call);
		result.setHeaders(response.headers());
		try {
			result.setResponse(response.body().string());//如何处理响应体是字节数据或者是输入流的情况，比如下载文件？
		} catch (IOException e) {
			e.printStackTrace();
			result.setResponse("");
		}
		Message msg = okHttpRequest.responseHandler.obtainMessage();
		msg.what = HttpConstants.NetDataProtocol.LOAD_SUCCESS;
		msg.arg1 = okHttpRequest.requestCode();
		msg.obj = result;
		okHttpRequest.responseHandler.sendMessage(msg);
	}

	private static void handleFailed(OkHttpRequest okHttpRequest, Call call,
			IOException e) {
		OkHttpResponse result = new OkHttpResponse();
		result.setCall(call);
		result.setException(e);
		Message msg = okHttpRequest.responseHandler.obtainMessage();
		msg.what = HttpConstants.NetDataProtocol.LOAD_FAILED;
		msg.arg1 = okHttpRequest.requestCode();
		msg.obj = result;
		okHttpRequest.responseHandler.sendMessage(msg);
	}
}
