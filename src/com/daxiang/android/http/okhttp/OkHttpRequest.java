package com.daxiang.android.http.okhttp;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
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
	private CacheControl cacheControl;
	private OkHttpClient client;

	// public OkHttpCallback okHttpCallback;
	public Handler responseHandler;

	public OkHttpRequest(Builder builder) {
		url = builder.url;
		requestCode = builder.requestCode;
		requestMethod = builder.requestMethod;
		requestBody = builder.requestBody;
		cacheControl = builder.cacheControl;
		client = builder.client;
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

	public CacheControl cacheControl() {
		return cacheControl;
	}

	public OkHttpClient client() {
		return client;
	}

	public static class Builder {
		private String url;
		private int requestCode;
		private HttpMethod requestMethod;
		private RequestBody requestBody;
		private CacheControl cacheControl;
		/**
		 * client.newBuilder()构建出来的副本； client.newBuilder()--This returns a
		 * builder that shares the same connection pool, dispatcher, and
		 * configuration with the original client.
		 */
		private OkHttpClient client;

		public Builder() {
			this.requestMethod = HttpMethod.GET;
			this.cacheControl = CacheControl.FORCE_NETWORK;
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

		public Builder cacheControl(CacheControl cacheControl) {
			this.cacheControl = cacheControl;
			return this;
		}

		public Builder client(OkHttpClient client) {
			this.client = client;
			return this;
		}

		public OkHttpRequest build() {
			return new OkHttpRequest(this);
		}
	}

}
