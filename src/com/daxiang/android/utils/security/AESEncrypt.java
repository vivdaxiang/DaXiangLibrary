package com.daxiang.android.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES工具；
 * 
 * @author daxiang
 * @date 2016年7月14日
 * @time 下午6:17:07
 */
public class AESEncrypt {

	/**
	 * 创建AES加密Key，并将其保存到指定目录下；
	 * 
	 * @param file
	 *            保存Key的目录
	 * @return 创建Key并写入指定目录成功，返回true；否则返回false；
	 */
	public static boolean generateKeyAndSave(File file) {
		if (file == null || !file.isDirectory()) {
			return false;
		}
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			// AES算法可以使用128、192、256位密钥；
			keyGenerator.init(128);
			SecretKey secretKey = keyGenerator.generateKey();

			ObjectOutputStream output = new ObjectOutputStream(
					new FileOutputStream(new File(file, "AESKey.dat")));
			output.writeObject(secretKey);
			output.flush();
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 从指定目录读取Key文件；
	 * 
	 * @param file
	 *            读取的目录；
	 * @return 成功返回{@link SecretKey}，否则返回null；
	 */
	public static SecretKey getSecretKeyFromFile(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream(file));
			SecretKey secretKey = (SecretKey) input.readObject();
			input.close();
			return secretKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES加密；
	 * 
	 * @param secretKey
	 *            加密用的Key；
	 * @param original
	 *            待加密的文本；
	 * @return 加密后的字节数组；
	 * @throws Exception
	 */
	public static byte[] encrypt(SecretKey secretKey, String original)
			throws Exception {
		SecretKeySpec spec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, spec);
		byte[] resultArray = cipher.doFinal(original.getBytes("UTF-8"));
		return resultArray;
	}

	/**
	 * AES解密；
	 * 
	 * @param secretKey
	 *            解密用的Key；
	 * @param original
	 *            待解密的字节数组；
	 * @return 解密后的文本；
	 * @throws Exception
	 */
	public static String decrypt(SecretKey secretKey, byte[] original)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] resultArray = cipher.doFinal(original);

		return new String(resultArray, "UTF-8");
	}

}
