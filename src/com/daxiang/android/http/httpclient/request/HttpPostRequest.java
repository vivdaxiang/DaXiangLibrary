package com.daxiang.android.http.httpclient.request;

import java.util.List;

import org.apache.http.NameValuePair;

import com.daxiang.android.http.HttpConstants.HttpMethod;

import android.content.Context;

/**
 * http的post请求；
 * 
 * @author daxiang
 * @date 2016年4月6日
 * @time 下午7:27:11
 */
public class HttpPostRequest extends HttpRequest {

	public HttpPostRequest(Context context) {
		super(context);
		method = HttpMethod.POST;
	}

	/**
	 * 请求实体参数；
	 */
	public List<NameValuePair> bodyParams;

}
