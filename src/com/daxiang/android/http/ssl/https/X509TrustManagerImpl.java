package com.daxiang.android.http.ssl.https;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.daxiang.android.utils.Logger;

/**
 * 自己实现的X509信任管理器
 * 
 * @author daxiang
 * @date 2016年7月21日
 * @time 下午4:07:26
 */
public class X509TrustManagerImpl implements X509TrustManager {
	private X509TrustManager defaultTrustManager;

	public X509TrustManagerImpl(String keystoreFile, String keystorePwd)
			throws Exception {

		this(new FileInputStream(new File(keystoreFile)), keystorePwd);
	}

	public X509TrustManagerImpl(InputStream input, String keystorePwd)
			throws Exception {
		// http://stackoverflow.com/questions/11117486/wrong-version-of-keystore-on-android-call;
		// Note:Android works with differents BKS version: for instance, API 15
		// will
		// require BKS-1 contrary to API 23 which require BKS, so you may need
		// to put both files in your app.
		//
		// Edit: Replaced Build.VERSION_CODES.JELLY_BEAN by
		// Build.VERSION_CODES.JELLY_BEAN_MR1, thanks to L3K0V
		//
		// Note 2: You can use this code:
		//
		// int bks_version;
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
		// bks_version = R.raw.publickey; //The BKS file
		// } else {
		// bks_version = R.raw.publickey_v1; //The BKS (v-1) file
		// }
		// KeyStore ks = KeyStore.getInstance("BKS");
		// InputStream in = getResources().openRawResource(bks_version);
		// ks.load(in, "mypass".toCharArray());

		KeyStore keyStore = KeyStore.getInstance("BKS");// Java中证书库格式是"JKS"、“PKS”，Android中要求“BKS”；
		keyStore.load(input, keystorePwd.toCharArray());

		// TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509",
		// "SunJSSE");//纯Java环境的写法，不适合Android环境；
		// TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX",
		// "BC");// algorithm：PKIX；provider：BC；//此写法始终爆异常：该provider不支持“XX”算法；
		Logger.i("X509TrustManagerImpl", "默认算法:" + TrustManagerFactory.getDefaultAlgorithm());// PKIX;
		TrustManagerFactory tmf = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);
		TrustManager[] tms = tmf.getTrustManagers();

		// Iterate over the returned trust managers, looking
		// for an instance of X509TrustManager. If found,
		// use that as the default trust manager.

		for (int i = 0; i < tms.length; i++) {
			if (tms[i] instanceof X509TrustManager) {
				defaultTrustManager = (X509TrustManager) tms[i];
				return;
			}
		}

		// Find some other way to initialize, or else the
		// constructor fails.

		throw new Exception("Couldn't initialize");
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// 单向验证可以不处理这里
		try {
			defaultTrustManager.checkClientTrusted(chain, authType);
		} catch (Exception e) {
			e.printStackTrace();
			// do any special handling here, or rethrow exception.
		}
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		try {
			defaultTrustManager.checkServerTrusted(chain, authType);
		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * Possibly pop up a dialog box asking whether to trust the cert
			 * chain.
			 */
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return defaultTrustManager.getAcceptedIssuers();
	}

}
