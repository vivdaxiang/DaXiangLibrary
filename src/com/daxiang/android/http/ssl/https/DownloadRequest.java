package com.daxiang.android.http.ssl.https;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
public class DownloadRequest {

	public static void main(String[] args) throws Exception {
		String url = "https://127.0.0.1:8443/HttpsServer/downloadServlet?img=demo.png";
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

		httpsURLConnection.setDoInput(true);
		httpsURLConnection.setDoOutput(true);
		httpsURLConnection.setReadTimeout(30000);
		httpsURLConnection.setRequestMethod("GET");
		httpsURLConnection.setUseCaches(false);

		InputStream inputStream = httpsURLConnection.getInputStream();
		BufferedInputStream bi = new BufferedInputStream(inputStream);

		BufferedOutputStream bo = new BufferedOutputStream(
				new FileOutputStream(new File("E:/demo.png")));

		byte[] temp = new byte[1024 * 4];

		while (bi.read(temp) != -1) {
			bo.write(temp, 0, temp.length);
		}
		bo.flush();

		int statusCode = httpsURLConnection.getResponseCode();
		System.out.println("服务端返回的状态码：" + statusCode);
		System.out.println("图片下载完毕");
		bi.close();
		inputStream.close();
		bo.close();

	}

}
