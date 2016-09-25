package com.daxiang.android.http.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * 
 * @author daxiang
 * @date 2016-8-21
 */
public class OkHttpResponse {

	private Call call;
	private Response response;
	private String responseStr;
	private IOException exception;
	private Headers headers;

	public Headers getHeaders() {
		return headers;
	}

	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	public Call getCall() {
		return call;
	}

	public void setCall(Call call) {
		this.call = call;
	}

	public Response getResponse() {
		return response;
	}

	public String getResponseStr() {
		return responseStr;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void setResponse(String response) {
		this.responseStr = response;
	}

	public IOException getException() {
		return exception;
	}

	public void setException(IOException e) {
		this.exception = e;
	}

}
