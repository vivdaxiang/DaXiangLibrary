package com.daxiang.android.http;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.daxiang.android.bean.BaseRequest;
import com.daxiang.android.http.HttpConstants.HttpMethod;

import android.content.Context;
import android.os.Handler;

/**
 * Http请求参数的实体
 *
 * @author daxiang
 * @date 2016年3月4日
 * @time 下午5:57:36
 */
public class HttpRequest {

	public HttpRequest(Context context) {
		this.context = context;
		method = HttpMethod.GET;
	}

	/**
	 * 
	 */
	private Context context;

	/**
	 * 资源路径；
	 */
	public String path;
	/**
	 * 请求码；
	 */
	public int requestCode;
	/**
	 * 请求方式；also see {@link HttpConstants.HttpMethod}
	 */
	public HttpMethod method;

	/**
	 * 请求头参数；
	 */
	public Map<String, String> headParams;

	/**
	 * 请求实体参数；
	 */
	public List<NameValuePair> bodyParams;
	/**
	 * 缓存模式
	 */
	public int dataAccessMode;

	private Handler responseHandler;

	public BaseRequest bean;// 暂时忽略该选项；

	/**
	 * 是否缓存请求结果
	 */
	private boolean isCache;

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}

	public Context getContext() {
		return context;
	}

	public Handler getResponseHandler() {
		return responseHandler;
	}

	public void setResponseHandler(Handler responseHandler) {
		this.responseHandler = responseHandler;
	}
}
