package com.daxiang.android.http.ssl.https;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 
 * @author daxiang
 * @date 2016-7-25
 */
public class MyHostnameVerifier implements HostnameVerifier {
	private String mHostName;

	public MyHostnameVerifier(String hostName) {
		mHostName = hostName;
	}

	public boolean verify(String hostname, SSLSession session) {
		// pop up an interactive dialog box
		// or insert additional matching logic
		boolean good_address;
		if (mHostName.equals(hostname)) {
			good_address = true;
		} else {
			good_address = false;
		}
		return good_address;
	}
}
