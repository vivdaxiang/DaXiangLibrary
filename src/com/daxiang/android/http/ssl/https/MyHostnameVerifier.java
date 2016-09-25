package com.daxiang.android.http.ssl.https;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.daxiang.android.utils.Logger;

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
		
		Logger.i("MyHostnameVerifier", "主机名认证结果=="+good_address);
		
		return good_address;
	}
}
