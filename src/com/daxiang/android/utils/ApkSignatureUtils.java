package com.daxiang.android.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

/**
 * 获取APK签名字符串工具
 * 
 * @author daxiang
 * @date 2016年3月25日
 * @time 下午8:44:42
 */
public class ApkSignatureUtils {

	/**
	 * 比较两个签名的md5串是否一致；
	 * 
	 * @param ctx
	 * @param packageName
	 *            已安装的包的完整包名；
	 * @param srcSignature
	 *            要跟已安装包的签名比较的签名；
	 * @return 相同，返回true；否则，返回false；
	 */
	public static boolean checkSignature(Context ctx, String packageName, String srcSignature) {
		
		PackageManager pm = ctx.getPackageManager();
		try {
			PackageInfo pack = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			Signature[] signatures = pack.signatures;
			String destSignature = getApkSignatureMD5String(signatures[0]);
			return destSignature.equalsIgnoreCase(srcSignature);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取apk签名的MD5串；
	 * 
	 * @param signature
	 * @return
	 */
	public static String getApkSignatureMD5String(Signature signature) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");

			md.update(signature.toByteArray());

			byte[] digest = md.digest();

			return toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * 
	 * Converts a byte array to hex string
	 * 
	 */
	private static String toHexString(byte[] block) {

		StringBuffer buf = new StringBuffer();

		int len = block.length;

		for (int i = 0; i < len; i++) {

			byte2hex(block[i], buf);

			// if (i < len - 1) {
			//
			// buf.append(":");
			//
			// }

		}

		return buf.toString();

	}

	private static void byte2hex(byte b, StringBuffer buf) {

		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',

				'9', 'A', 'B', 'C', 'D', 'E', 'F' };

		int high = ((b & 0xf0) >> 4);

		int low = (b & 0x0f);

		buf.append(hexChars[high]);

		buf.append(hexChars[low]);

	}
}
