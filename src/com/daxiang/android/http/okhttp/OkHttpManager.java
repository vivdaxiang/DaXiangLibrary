package com.daxiang.android.http.okhttp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.content.Context;
import android.os.Message;

import com.daxiang.android.http.HttpConstants;
import com.daxiang.android.http.HttpConstants.HttpMethod;
import com.daxiang.android.http.ssl.https.MyHostnameVerifier;
import com.daxiang.android.http.ssl.https.X509TrustManagerImpl;
import com.daxiang.android.utils.FileUtils;

/**
 * OKHttp核心管理类；
 * 
 * @author daxiang
 * @date 2016年8月6日
 * @time 下午3:07:04
 */
public class OkHttpManager {

	private static OkHttpClient mInstance;

	private static boolean mUseHttps;
	private static String httpsHostName;
	private static InputStream keystoreFile;
	private static String keystorePwd;

	private static Context mContext;
	/**
	 * 是否使用Application Interceptors；
	 */
	private static boolean mUseApplicationInterceptors = false;
	/**
	 * 是否使用Network Interceptors；
	 */
	private static boolean mUseNetworkInterceptors = false;

	// ****************************常量*************************************
	private static final int CACHE_SIZE = 50 * 1024 * 1024; // 50 MiB
	private static final long CONNECTION_TIMEOUT = 30_000;// 30s;
	private static final long WRITE_TIMEOUT = 30_000;// 30s;
	private static final long READ_TIMEOUT = 30_000;// 30s;

	// *******************************************************************
	private OkHttpManager() {

	}

	public static void useHttps(boolean useHttps) {
		mUseHttps = useHttps;
	}

	public static void httpsHostName(String hostName) {
		httpsHostName = hostName;
	}

	/**
	 * 是否使用Application Interceptors；
	 * 
	 * @param useApplicationInterceptors
	 */
	public static void useApplicationInterceptors(
			boolean useApplicationInterceptors) {
		if (useApplicationInterceptors) {
			mUseNetworkInterceptors = false;
		}
		mUseApplicationInterceptors = useApplicationInterceptors;
	}

	/**
	 * 是否使用Network Interceptors；
	 * 
	 * @param useNetworkInterceptors
	 */
	public static void useNetworkInterceptors(boolean useNetworkInterceptors) {
		// 如果使用了Network Interceptors，就不再使用Application Interceptors，避免log重复；
		if (useNetworkInterceptors) {
			mUseApplicationInterceptors = false;
		}
		mUseNetworkInterceptors = useNetworkInterceptors;
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

	public static OkHttpClient init(Context context) {
		mContext = context;
		if (mInstance == null) {
			synchronized (OkHttpManager.class) {
				if (mInstance == null) {
					if (!mUseHttps) {
						mInstance = initHttpClient();
					} else {
						mInstance = initHttpsClient();
					}

				}
			}
		}

		return mInstance;
	}

	/**
	 * 先初始化{@link OkHttpManager.init}
	 * ，再调用该方法，否则空指针；主要用于单个call的配置，client.newBuilder()--This returns a builder
	 * that shares the same connection pool, dispatcher, and configuration with
	 * the original client.
	 * 
	 * @return
	 */
	public static OkHttpClient getInstance() {

		return mInstance;
	}

	private static Cache cache() {
		File cacheDirectory = new File(FileUtils.getExternalCacheDirs(mContext)
				.getAbsolutePath() + "/okhttp_cache");
		Cache cache = new Cache(cacheDirectory, CACHE_SIZE);
		return cache;
	}

	/**
	 * 初始化OKHttpClient；
	 * 
	 * @return
	 */
	public static OkHttpClient initHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.cache(cache());
		configurationClient(builder);
		return builder.build();
	}

	/**
	 * 初始化使用HTTPS的OkHttpClient；
	 * 
	 * @return
	 */
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
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.hostnameVerifier(new MyHostnameVerifier(httpsHostName))
				.sslSocketFactory(sslSocketFactory, trustManager);

		configurationClient(builder);
		return builder.build();
	}

	public static void configurationClient(OkHttpClient.Builder builder) {
		builder.cache(cache())
				.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
				.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
				.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
		if (mUseApplicationInterceptors) {
			builder.addInterceptor(new LogInterceptor());
		}
		if (mUseNetworkInterceptors) {
			builder.addNetworkInterceptor(new LogInterceptor());
		}
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
				.post(okHttpRequest.requestBody())
				.cacheControl(okHttpRequest.cacheControl()).build();

		Call call = getCall(okHttpRequest, request);
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
				.cacheControl(okHttpRequest.cacheControl()).build();

		Call call = getCall(okHttpRequest, request);
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

	private static Call getCall(OkHttpRequest okHttpRequest, Request request) {
		Call call = null;
		if (okHttpRequest.client() != null) {
			call = okHttpRequest.client().newCall(request);
			return call;
		}
		call = mInstance.newCall(request);
		return call;
	}

	private static void handleSuccess(OkHttpRequest okHttpRequest, Call call,
			Response response) {
		OkHttpResponse result = new OkHttpResponse();
		result.setCall(call);
		result.setHeaders(response.headers());
		try {
			result.setResponse(response.body().string());// 如何处理响应体是字节数据或者是输入流的情况，比如下载文件？
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
