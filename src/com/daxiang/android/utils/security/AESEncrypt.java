package com.daxiang.android.utils.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 
 *
 * @author daxiang
 * @date 2016年7月14日
 * @time 下午6:17:07
 */
public class AESEncrypt {

	public static boolean generateKeyAndSave(File file) {
		if (file == null || !file.isDirectory()) {
			return false;
		}
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			// AES算法可以使用128、192、256位密钥；
			keyGenerator.init(128);
			SecretKey secretKey = keyGenerator.generateKey();

			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File(file, "AESKey.dat")));
			output.writeObject(secretKey);
			output.flush();
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
