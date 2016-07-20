package com.daxiang.android.http.ssl.bothway;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * 双向验证的SSL服务端；
 *
 * @author daxiang
 * @date 2016年7月18日
 * @time 下午6:36:48
 */
public class SSLServer {
	// 模拟数据库数据；
	private String userName = "大象";
	private String password = "222222";
	public static final int serverPort = 2222;

	private SSLServerSocket serverSocket;

	public SSLServer() {
		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
		System.clearProperty("javax.net.ssl.keyStoreType");
		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");
		System.clearProperty("javax.net.ssl.trustStoreType");

		System.setProperty("javax.net.ssl.keyStore", "e:\\ssl\\server.keystore");
		System.setProperty("javax.net.ssl.keyStorePassword", "222222");
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStore", "e:\\ssl\\servertrust.keystore");
		System.setProperty("javax.net.ssl.trustStorePassword", "222222");
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");

		try {
			serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listenClientRequest() {
		while (true) {
			System.out.println("服务端已启动，放马过来吧！");
			try {
				SSLSocket clientSocket = (SSLSocket) serverSocket.accept();

				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String userName = br.readLine();
				String password = br.readLine();

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

				if (userName.equals(this.userName) && password.equals(this.password)) {
					bw.write("登录成功，您的京东账号为：" + userName);
				} else {
					bw.write("登录失败，账号或者密码错误！");
				}
				bw.flush();

				clientSocket.close();
				br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	// 运行之前，先要使用Java的keytool工具生成服务端的数字证书库文件（server.keystore）和已信任的数字证书库文件(servertrust.keystore)，
	// 并把客户端的数字证书导入到服务端的已信任数字证书库文件中

	public static void main(String args[]) {
		SSLServer sslServer = new SSLServer();
		sslServer.listenClientRequest();

	}
}
