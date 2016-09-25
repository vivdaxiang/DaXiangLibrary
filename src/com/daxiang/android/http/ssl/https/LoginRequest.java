package com.daxiang.android.http.ssl.https;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * 
 * @author daxiang
 * @date 2016-7-25
 */
public class LoginRequest {

	public static void main(String[] args) throws Exception {
		String url = "https://127.0.0.1:8443/HttpsServer/loginHttpServlet";
		String keystoreFile = "E:/HTTPS/clienttrust.keystore";
		String keystorePwd = "222222";
		TrustManager[] tms = { new X509TrustManagerImpl(keystoreFile,
				keystorePwd) };
		// Use factory
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, tms, null);
		SSLSocketFactory sf = ctx.getSocketFactory();

		URL serverUrl = new URL(url);
		HttpsURLConnection httpsURLConnection = (HttpsURLConnection) serverUrl
				.openConnection();

		httpsURLConnection.setHostnameVerifier(new MyHostnameVerifier(
				"127.0.0.1"));
		httpsURLConnection.setSSLSocketFactory(sf);

		httpsURLConnection.addRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");

		httpsURLConnection.setDoInput(true);
		httpsURLConnection.setDoOutput(true);
		httpsURLConnection.setReadTimeout(30000);
		httpsURLConnection.setRequestMethod("POST");
		httpsURLConnection.setUseCaches(false);

		OutputStream outputStream = httpsURLConnection.getOutputStream();
		outputStream.write("userName=daxiang&userPwd=222222".getBytes());
		outputStream.flush();

		InputStream inputStream = httpsURLConnection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream, "UTF-8"));
		String temp;
		StringBuffer responseStr = new StringBuffer();
		while ((temp = br.readLine()) != null) {

			responseStr.append(temp);
		}

		int statusCode = httpsURLConnection.getResponseCode();
		System.out.println("服务端返回的状态码：" + statusCode);
		System.out.println("服务端返回的响应：" + responseStr.toString());

		outputStream.close();
		br.close();
		inputStream.close();

	}

}
