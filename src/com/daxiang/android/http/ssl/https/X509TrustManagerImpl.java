package com.daxiang.android.http.ssl.https;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 自己实现的X509信任管理器
 * 
 * @author daxiang
 * @date 2016年7月21日
 * @time 下午4:07:26
 */
public class X509TrustManagerImpl implements X509TrustManager {
	private X509TrustManager defaultTrustManager;

	public X509TrustManagerImpl() throws Exception {
		String keystoreFile = "E:/HTTPS/clienttrust.keystore";
		String keystorePwd = "222222";

		FileInputStream input = new FileInputStream(new File(keystoreFile));

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(input, keystorePwd.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509",
				"SunJSSE");
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
