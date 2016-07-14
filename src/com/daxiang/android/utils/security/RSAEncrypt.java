package com.daxiang.android.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

/**
 * RSA工具；
 * 
 * @author daxiang
 * @date 2016-7-14
 */
public class RSAEncrypt {
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 生成RSA密钥对：公钥和私钥，生成之后写入指定的目录内；
	 * 
	 * @param outputDir
	 *            密钥写入的目录
	 * @return 生成密钥并写入指定的目录成功，返回true；生成密钥失败或者写入目录失败，返回false
	 */
	public static boolean generateRSAKeyPair(File outputDir) {

		if (outputDir == null || !outputDir.isDirectory()) {
			return false;
		}

		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			// keysize 至少为500位长，而且必须是64的倍数，通常使用1024；
			generator.initialize(1024, new SecureRandom());

			KeyPair keypair = generator.generateKeyPair();
			// 密钥中的公钥、私钥也可以使用Base64编码转换后再保存；
			ObjectOutputStream publicKeyOutput = new ObjectOutputStream(
					new FileOutputStream(new File(outputDir, "publicKey.dat")));
			publicKeyOutput.writeObject(keypair.getPublic());
			publicKeyOutput.flush();
			publicKeyOutput.close();

			ObjectOutputStream privateKeyOutput = new ObjectOutputStream(
					new FileOutputStream(new File(outputDir, "privateKey.dat")));
			privateKeyOutput.writeObject(keypair.getPrivate());
			privateKeyOutput.flush();
			privateKeyOutput.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 从指定文件读取RSA公钥；
	 * 
	 * @param file
	 *            存放密钥的文件；
	 * @return 返回公钥；读取失败返回null
	 */
	public static PublicKey getPublicKeyFromObject(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		PublicKey publicKey;
		try {
			ObjectInputStream publicKeyInput = new ObjectInputStream(new FileInputStream(file));
			publicKey = (PublicKey) publicKeyInput.readObject();
			publicKeyInput.close();
			return publicKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从指定文件读取RSA私钥；
	 * 
	 * @param file
	 *            存放密钥的文件；
	 * @return 返回私钥；读取失败返回null
	 */
	public static PrivateKey getPrivateKeyFromObject(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		PrivateKey privateKey;
		try {
			ObjectInputStream keyInput = new ObjectInputStream(new FileInputStream(file));
			privateKey = (PrivateKey) keyInput.readObject();
			keyInput.close();
			return privateKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用RSA公钥加密；
	 * 
	 * @param publicKey
	 *            公钥；
	 * @param original
	 *            待加密的文本
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] publicKeyEncrypt(PublicKey publicKey, String original) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 加密算法/反馈模式/填充方案
		// Cipher cipher = Cipher.getInstance("RSA");//反馈模式、填充方案使用默认值；
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] resultArray = cipher.doFinal(original.getBytes(CHARSET_NAME));
		return resultArray;
	}

	/**
	 * 使用RSA公钥解密；
	 * 
	 * @param publicKey
	 *            公钥
	 * @param original
	 *            待解密的源数组；
	 * @return 解密后的文本
	 * @throws Exception
	 */
	public static String publicKeyDecrypt(PublicKey publicKey, byte[] original) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 加密算法/反馈模式/填充方案
		// Cipher cipher = Cipher.getInstance("RSA");//反馈模式、填充方案使用默认值；
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] resultArray = cipher.doFinal(original);

		return new String(resultArray, CHARSET_NAME);
	}

	/**
	 * 使用RSA私钥加密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param original
	 *            待加密的文本
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] privateKeyEncrypt(PrivateKey privateKey, String original) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 加密算法/反馈模式/填充方案
		// Cipher cipher = Cipher.getInstance("RSA");//反馈模式、填充方案使用默认值；
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] resultArray = cipher.doFinal(original.getBytes(CHARSET_NAME));
		return resultArray;
	}

	/**
	 * 使用RSA私钥解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param original
	 *            待解密的源数组
	 * @return 解密后的文本
	 * @throws Exception
	 */
	public static String privateKeyDecrypt(PrivateKey privateKey, byte[] original) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 加密算法/反馈模式/填充方案
		// Cipher cipher = Cipher.getInstance("RSA");//反馈模式、填充方案使用默认值；
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] resultArray = cipher.doFinal(original);
		return new String(resultArray, CHARSET_NAME);
	}

}
