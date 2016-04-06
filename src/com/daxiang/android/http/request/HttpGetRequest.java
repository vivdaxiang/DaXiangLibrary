package com.daxiang.android.http.request;

import com.daxiang.android.http.HttpConstants.HttpMethod;

import android.content.Context;

/**
 * http的get请求；
 * 
 * @author daxiang
 * @date 2016年4月6日
 * @time 下午7:26:09
 */
public class HttpGetRequest extends HttpRequest {

	public HttpGetRequest(Context context) {
		super(context);
		method = HttpMethod.GET;
	}

}
