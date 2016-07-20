package com.daxiang.android.http.ssl.oneway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


/**
 * 单向验证的SSL客户端
 *
 * @author daxiang
 * @date 2016年7月18日
 * @time 下午6:37:40
 */
public class SSLClient {

	private String userName = "大象";
	private String password = "222222";

	private SSLSocket clientSocket;

	public SSLClient() {
		// 清除以前的Java系统环境变量的值；
		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");
		System.clearProperty("javax.net.ssl.trustStoreType");

		// 设置Java系统的环境变量的值；
		System.setProperty("javax.net.ssl.trustStore", "e:\\ssl\\clienttrust.keystore");
		System.setProperty("javax.net.ssl.trustStorePassword", "vivdaxiang");
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");

		try {
			clientSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("localhost", SSLServer.serverPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void requestServer() {
		try {
			PrintWriter bw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			bw.println(userName);
			bw.println(password);
			bw.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String temp;
			String message = "";
			while ((temp = br.readLine()) != null) {
				message = message + temp;
			}

			System.out.println("---这是客户端输出的信息----");
			System.out.println(message);

			clientSocket.close();
			bw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 运行之前，先要使用Java的keytool工具生成客户端的数字证书库文件（client.keystore）和已信任的数字证书库文件(clienttrust.keystore)，
	// 并把服务端的数字证书导入到客户端的已信任数字证书库文件中
	public static void main(String[] args) {

		SSLClient sslClient = new SSLClient();
		sslClient.requestServer();
	}
}
