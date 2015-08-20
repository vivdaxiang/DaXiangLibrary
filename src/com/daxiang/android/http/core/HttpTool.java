package com.daxiang.android.http.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

import com.daxiang.android.util.Logger;

/**
 * http请求核心类；
 * 
 * @author daxiang
 * 
 *         2015-3-23
 */
public class HttpTool {
	private static final String TAG = HttpTool.class.getSimpleName();
	public static String SESSION_ID;
	public static String USER_AGENT = "DaXiangLibrary Android";

	private static final int DEFAULT_MAX_CONNECTIONS = 30;

	public static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;

	public static final int DEFAULT_SOCKET_TIMEOUT_SHORT = 10 * 1000;

	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024;

	private static DefaultHttpClient sHttpClient;

	final static HttpParams httpParams = new BasicHttpParams();
	static {
		ConnManagerParams.setTimeout(httpParams, 1000);// 从连接池中取连接的超时时间；
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(10));// 每个路由的最大连接数；
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_CONNECTIONS);// 连接池的总最大连接数；
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpClientParams.setRedirecting(httpParams, false);// 重定向；
		HttpProtocolParams.setUserAgent(httpParams, USER_AGENT);
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);// socket的超时时间；
		HttpConnectionParams.setConnectionTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT);// 创建连接的超时时间；
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		// https相关的，暂时注释掉；2015年4月1日22:27:58；
		// try {
		// KeyStore trustStore = KeyStore.getInstance(KeyStore
		// .getDefaultType());
		// trustStore.load(null, null);
		// SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
		// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		// schemeRegistry.register(new Scheme("https", sf, 443));
		// } catch (Exception ex) {
		// }

		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);
		sHttpClient = new DefaultHttpClient(manager, httpParams);
		// 暂时注释掉；2015年4月1日22:28:40；
		// BasicCookieStore cookieStore = new BasicCookieStore();
		// sHttpClient.setCookieStore(cookieStore);
		// CookieSpecFactory csf = new CookieSpecFactory() {
		// public CookieSpec newInstance(HttpParams params) {
		// return new BrowserCompatSpec() {
		// @Override
		// public void validate(Cookie cookie, CookieOrigin origin)
		// throws MalformedCookieException {
		//
		// }
		// };
		// }
		// };
		// sHttpClient.getCookieSpecs().register("oschina", csf);
		// sHttpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
		// "oschina");
		sHttpClient.getParams().setParameter(
				CookieSpecPNames.SINGLE_COOKIE_HEADER, true);
	}

	private HttpTool() {

	}

	public static void httpNoResult(String url) {
		HttpURLConnection url_con = null;
		try {
			url_con = (HttpURLConnection) new URL(url).openConnection();
			url_con.connect();
			url_con.getResponseCode();
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
		} finally {
			if (null != url_con)
				url_con.disconnect();
		}
	}

	public static String httpGet(String url) {

		HttpGet request = new HttpGet(url);
		setSessionId(request);
		// addAgent(request);
		try {
			HttpResponse response = sHttpClient.execute(request);
			getSessionId(response);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(response.getEntity());
				return content;
			}
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
		} finally {
		}
		return "";

	}

	public static String httpPost(String url, List<NameValuePair> postParameters) {

		HttpPost request = new HttpPost(url);
		setSessionId(request);
		// addAgent(request);
		try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			request.setEntity(formEntity);
			HttpResponse response = sHttpClient.execute(request);
			getSessionId(response);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(response.getEntity());
				return content;
			}
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
		} finally {
		}
		return "";

	}

	// *****************************************************
	/* 缺少图片上传、文件上传、文件下载处理的方法，需完善； */
	// *****************************************************

	private static void addAgent(HttpRequestBase request) {
		request.setHeader("User-Agent", USER_AGENT);
	}

	private static void setSessionId(HttpRequestBase request) {
		if (!TextUtils.isEmpty(SESSION_ID)) {
			request.setHeader("Cookie", /* "JSESSIONID=" + */SESSION_ID);
		}
		Logger.i("SESSION_ID", SESSION_ID);
	}

	private static void getSessionId(HttpResponse response) {
		Header[] headers = response.getAllHeaders();
		if (headers != null) {
			int len = headers.length;
			Header header;
			for (int i = 0; i < len; i++) {
				header = headers[i];
				if (header.getName().indexOf("Cookie") != -1) {
					if (header.getValue().indexOf("ID=") != -1) {
						SESSION_ID = header.getValue().split(";")[0];
						break;
					}
				}
			}
		}
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
