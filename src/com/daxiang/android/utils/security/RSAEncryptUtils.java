package com.daxiang.android.utils.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

/**
 * RSA工具；
 * 
 * @author daxiang
 * @date 2016-7-14
 */
public class RSAEncryptUtils {

	public static boolean generateRSAKeyPair(File outputDir) {

		if (outputDir == null || !outputDir.isDirectory()) {
			return false;
		}

		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

			generator.initialize(1024, new SecureRandom());

			KeyPair keypair = generator.generateKeyPair();

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
}
