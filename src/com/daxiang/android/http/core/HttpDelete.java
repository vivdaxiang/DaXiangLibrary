package com.daxiang.android.http.core;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * 
 * @author daxiang
 * @date 2015-7-15
 * 
 */
public class HttpDelete extends HttpEntityEnclosingRequestBase {

	public static final String METHOD = "DELETE";

	@Override
	public String getMethod() {
		return METHOD;
	}

	public HttpDelete(String url) {
		super();
		setURI(URI.create(url));
	}

	public HttpDelete(URI url) {
		super();
		setURI(url);
	}
}
