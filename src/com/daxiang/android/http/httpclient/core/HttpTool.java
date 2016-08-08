package com.daxiang.android.http.httpclient.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.daxiang.android.bean.BaseRequest;
import com.daxiang.android.http.HttpConstants.HttpMethod;
import com.daxiang.android.http.httpclient.request.HttpFilePostRequest;
import com.daxiang.android.http.httpclient.request.HttpPostRequest;
import com.daxiang.android.http.httpclient.request.HttpRequest;
import com.daxiang.android.utils.FileUtils;
import com.daxiang.android.utils.Logger;
import com.google.gson.Gson;

import android.content.Context;
import android.text.TextUtils;

/**
 * http请求核心类；
 * 
 * @author daxiang
 * 
 *         2015-3-23
 */
@SuppressWarnings("deprecation")
public class HttpTool {
	private static final String TAG = HttpTool.class.getSimpleName();
	public static String COOKIE;
	public static String USER_AGENT = "DaXiangLibraryAndroid";
	private static final String CONTENT_ENCODE = "utf-8";
	private static final String CONTENT_TYPE = "application/json";

	private static final int DEFAULT_MAX_CONNECTIONS = 10;

	public static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;

	public static final int DEFAULT_SOCKET_TIMEOUT_SHORT = 10 * 1000;

	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024;

	private static DefaultHttpClient sHttpClient;

	final static HttpParams httpParams = new BasicHttpParams();

	static {
		ConnManagerParams.setTimeout(httpParams, 3000);// 从连接池中取连接的超时时间；
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(10));// 每个路由的最大连接数；
		ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);// 连接池的总最大连接数；
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, CONTENT_ENCODE);
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpClientParams.setRedirecting(httpParams, false);// 重定向；
		// HttpProtocolParams.setUserAgent(httpParams, USER_AGENT);
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);// socket的超时时间；
		HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);// 创建连接的超时时间；
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
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

		ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
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
		sHttpClient.getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, true);
	}

	private HttpTool() {

	}

	public static String sendRequest(HttpRequest httpRequest) {

		if (httpRequest.getMethod() == HttpMethod.GET) {
			return HttpTool.httpGet(httpRequest.path, httpRequest.headParams);

		} else if (httpRequest.getMethod() == HttpMethod.POST && httpRequest instanceof HttpFilePostRequest) {

			HttpFilePostRequest filePostRequest = (HttpFilePostRequest) httpRequest;
			return httpPost(filePostRequest.path, filePostRequest.headParams, filePostRequest.bodyParams,
					filePostRequest.file, filePostRequest.fileBodyName);

		} else if (httpRequest.getMethod() == HttpMethod.POST) {
			HttpPostRequest postRequest = (HttpPostRequest) httpRequest;
			return HttpTool.httpPost(postRequest.path, postRequest.headParams, postRequest.bodyParams);

		} else if (httpRequest.getMethod() == HttpMethod.DELETE) {

		}

		return "";
	}

	/**
	 * HTTP的GET请求；
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private static String httpGet(String url, Map<String, String> headParams) {
		HttpGet request = new HttpGet(url);
		setCookie(request);
		addAgent(request);
		if (null != headParams) {
			for (Map.Entry<String, String> entry : headParams.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			HttpResponse response = sHttpClient.execute(request);
			getCookie(response);
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

	/**
	 * Post请求
	 * 
	 * @param url
	 * @param headParams
	 * @param bodyParams
	 * @return
	 */
	private static String httpPost(String url, Map<String, String> headParams, List<NameValuePair> bodyParams) {

		HttpPost request = new HttpPost(url);
		setCookie(request);
		addAgent(request);

		if (null != headParams) {
			for (Map.Entry<String, String> entry : headParams.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(bodyParams);
			request.setEntity(formEntity);
			HttpResponse response = sHttpClient.execute(request);
			getCookie(response);
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

	/**
	 * 文件上传；
	 * 
	 * @param url
	 * @param headParams
	 *            Http请求头参数，如果没有，请置为null；
	 * @param bodyParams
	 *            Http请求实体；
	 * @param file
	 *            文件对象；
	 * @param fileBodyName
	 *            文件上传参数名；
	 * @return
	 */
	private static String httpPost(String url, Map<String, String> headParams, List<NameValuePair> bodyParams,
			File file, String fileBodyName) {
		HttpPost request = new HttpPost(url);
		setCookie(request);
		addAgent(request);
		if (null != headParams) {
			for (Map.Entry<String, String> entry : headParams.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			MultipartEntity entity = new MultipartEntity();
			if (null != bodyParams) {
				for (NameValuePair nameValuePair : bodyParams) {
					entity.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue()));
				}
			}

			if (file != null && file.exists()) {
				// entity.addPart("file", new InputStreamBody(new
				// FileInputStream(
				// file), "audio/x-caf", "abc"));
				// entity.addPart("file", new FileBody(file));
				entity.addPart(fileBodyName, new FileBody(file));
			}

			request.setEntity(entity);
			HttpResponse response = sHttpClient.execute(request);
			getCookie(response);
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

	// -----------------------------------------暂时不用start------------------------------------------------

	/**
	 * Http的Delete请求；
	 * 
	 * @param url
	 * @return
	 */
	public static String httpDelete(String url) {
		return httpDelete(url, null);
	}

	/**
	 * Http的Delete请求；
	 * 
	 * @param url
	 * @param headParams
	 * @return
	 */
	public static String httpDelete(String url, Map<String, String> headParams) {
		return httpDelete(url, headParams, null);
	}

	/**
	 * Http的Delete请求；
	 * 
	 * @param url
	 * @param headParams
	 * @param bean
	 * @return
	 */
	public static String httpDelete(String url, Map<String, String> headParams, BaseRequest bean) {
		HttpDelete request = new HttpDelete(url);
		setCookie(request);
		addAgent(request);
		if (null != headParams) {
			for (Map.Entry<String, String> entry : headParams.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			if (null != bean) {
				request.addHeader("Content-Type", CONTENT_TYPE);
				Gson gson = new Gson();
				String json = gson.toJson(bean);
				if (json.contains("list")) {
					JSONObject object = new JSONObject(json);
					String list = object.getString("list");
					if (!TextUtils.isEmpty(list)) {
						json = list;
					}
				}

				StringEntity entity = new StringEntity(json, CONTENT_ENCODE);
				request.setEntity(entity);
			}

			HttpResponse response = sHttpClient.execute(request);
			getCookie(response);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(response.getEntity());
				return content;
			}
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
		} finally {
		}
		return "";
	}

	public static String httpPost(String url, BaseRequest bean) {

		HttpPost request = new HttpPost(url);
		setCookie(request);
		addAgent(request);
		request.addHeader("Content-Type", CONTENT_TYPE);
		Gson gson = new Gson();
		String json = gson.toJson(bean);
		try {
			if (json.contains("list")) {
				JSONObject object = new JSONObject(json);
				String list = object.getString("list");
				if (!TextUtils.isEmpty(list)) {
					json = list;
				}
			}

			StringEntity entity = new StringEntity(json, CONTENT_ENCODE);
			request.setEntity(entity);
			HttpResponse response = sHttpClient.execute(request);
			getCookie(response);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(response.getEntity());
				return content;
			}
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
			return "HttpTool Exception:" + e.toString();
		} finally {
		}
		return "";

	}

	// ----------------------------------------暂时不用end-------------------------------------------------

	/**
	 * 图片上传；
	 * 
	 * @param url
	 * @param params
	 *            Http请求头参数，如果没有，请置为null；
	 * @param data
	 *            Bitmap字节数组；
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> headParams, byte[] data) {
		HttpPost request = new HttpPost(url);
		setCookie(request);
		addAgent(request);
		if (null != headParams) {
			for (Map.Entry<String, String> entry : headParams.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			ByteArrayEntity entity = new ByteArrayEntity(data);
			request.setEntity(entity);
			HttpResponse response = sHttpClient.execute(request);
			getCookie(response);
			// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			// {
			String content = EntityUtils.toString(response.getEntity());
			return content;
			// }
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
		} finally {
		}

		return "";
	}

	/**
	 * 文件下载；
	 * 
	 * @param context
	 * @param url
	 *            要下载文件的URL；
	 * @param fileName
	 *            保存到本地时的名字；
	 * @return 下载并保存成功，返回true；否则，返回false；
	 */
	public static boolean downloadFile(Context context, String url, String fileName) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = sHttpClient.execute(httpget);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			File file = new File(FileUtils.getExternalCacheDirs(context), fileName);
			fos = new FileOutputStream(file);
			/**
			 * 根据实际运行效果 设置缓冲区大小
			 */
			byte[] buffer = new byte[1024 * 4];
			int ch = 0;
			while ((ch = is.read(buffer, 0, buffer.length)) != -1) {
				fos.write(buffer, 0, ch);
			}
			is.close();
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (null != is) {
					is.close();
				}
				if (null != fos) {
					fos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			return false;
		}
		return true;
	}

	private static void addAgent(HttpRequestBase request) {
		request.setHeader("User-Agent", USER_AGENT);
	}

	private static void setCookie(HttpRequestBase request) {
		if (TextUtils.isEmpty(COOKIE)) {
			// COOKIE = CookieUtils.getCookie(MyApplication.getInstance());
		}
		if (!TextUtils.isEmpty(COOKIE)) {
			request.setHeader("Cookie", COOKIE);
		}

	}

	private static void getCookie(HttpResponse response) {
		Header[] headers = response.getAllHeaders();

		if (headers != null) {
			int len = headers.length;
			Header header;
			for (int i = 0; i < len; i++) {
				header = headers[i];
				if (header.getName().indexOf("Cookie") != -1) {
					if (header.getValue().indexOf("touba_id=") != -1) {
						String toubaId = header.getValue().split(";")[0];
						// 登录成功后返回的头字段里有两个Set-Cookie，其中有一个的内容为touba_id=""
						if (!TextUtils.isEmpty(toubaId)) {
							COOKIE = toubaId;
							// CookieUtils.saveCookie(MyApplication.getInstance(),
							// toubaId);
						}
					}
				}
			}
		}
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
