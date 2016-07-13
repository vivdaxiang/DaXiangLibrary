package com.daxiang.android.utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

/**
 * 加解密工具；
 *
 * @author daxiang
 * @date 2016年7月13日
 * @time 下午6:23:09
 */
public class EncryptUtils {

	public static String base64Encode(byte[] src) {
		byte[] result = Base64.encode(src, Base64.DEFAULT);

		return new String(result);

	}

	public static byte[] base64Decode(byte[] src) {
		return Base64.decode(src, Base64.DEFAULT);
	}

	public static String md5Encrypt(String originalStr) {
		String resultStr = "";
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(originalStr.getBytes());
			byte[] resultArr = messageDigest.digest();

			resultStr = base64Encode(resultArr);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return resultStr;
	}
}
