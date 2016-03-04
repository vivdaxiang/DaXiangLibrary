package com.daxiang.android.http;

import java.util.List;

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

	public Context mContext;

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
	 * POST请求参数；
	 */
	public List<NameValuePair> requestParameters;
	/**
	 * 缓存模式
	 */
	public int dataAccessMode;

	public Handler handler;

	public BaseRequest bean;

	public HttpRequest() {
		method = HttpMethod.GET;
	}
}
