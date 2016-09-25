package com.daxiang.android.http.okhttp;

import okhttp3.RequestBody;

import android.os.Handler;

import com.daxiang.android.http.HttpConstants.HttpMethod;

/**
 * 
 * @author daxiang
 * @date 2016-8-16
 */
public class OkHttpRequest {

	private String url;
	private int requestCode;
	private HttpMethod requestMethod;
	private RequestBody requestBody;

	// public OkHttpCallback okHttpCallback;
	public Handler responseHandler;

	public OkHttpRequest(Builder builder) {
		url = builder.url;
		requestCode = builder.requestCode;
		requestMethod = builder.requestMethod;
		requestBody = builder.requestBody;
	}

	public String url() {
		return url;
	}

	public int requestCode() {
		return requestCode;
	}

	public HttpMethod requestMethod() {
		return requestMethod;
	}

	public RequestBody requestBody() {
		return requestBody;
	}

	public static class Builder {
		private String url;
		private int requestCode;
		private HttpMethod requestMethod;
		private RequestBody requestBody;

		public Builder() {
			this.requestMethod = HttpMethod.GET;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder requestCode(int requestCode) {
			this.requestCode = requestCode;
			return this;
		}

		public Builder requestMethod(HttpMethod httpMethod) {
			this.requestMethod = httpMethod;
			return this;
		}

		public Builder requestBody(RequestBody requestBody) {
			this.requestBody = requestBody;
			return this;
		}

		public OkHttpRequest build() {
			return new OkHttpRequest(this);
		}
	}

}
