package com.daxiang.android.http.request;

import java.util.Map;

import com.daxiang.android.bean.BaseRequest;
import com.daxiang.android.http.HttpConstants;
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
	}

	/**
	 * 
	 */
	protected Context context;

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
	protected HttpMethod method;

	/**
	 * 请求头参数；
	 */
	public Map<String, String> headParams;

	/**
	 * 缓存模式；also see {@link HttpConstants.NetDataProtocol}
	 */
	public int dataAccessMode;

	protected Handler responseHandler;

	private BaseRequest bean;// 暂时忽略该选项；

	/**
	 * 是否缓存请求结果
	 */
	protected boolean isCache;

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

	public HttpMethod getMethod() {
		return method;
	}
}
