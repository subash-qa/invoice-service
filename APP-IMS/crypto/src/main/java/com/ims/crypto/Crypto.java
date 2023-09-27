package com.ims.crypto;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Crypto implements CryptoInterface {

	////// private static Logger LOGGER =
	////// LogManager.getLogger(Crypto.class.getName());
	private Crypto() {

	}

	private static Crypto crypto;

	private static boolean cryptoApplied = false;

	public static Crypto getCryptoInstance() {
		if (crypto == null) {
			crypto = new Crypto();
			// LOGGER.debug("Singleton Created");
		}
		return crypto;
	}

	@Override
	public void initializeCrypto(String keyFile, String keyStoreFile, String keyStoreType, String algorithm,
			String fileKeyPassword, String aliasKey) throws Exception {

		cryptoApplied = true;

		// LOGGER.debug("Crypto staterd");
		cryptoAlgorithm = algorithm;
		String password = PCrypt.getPcrypt(CryptoUtilities.readKeyFile(keyFile)).decrypt(fileKeyPassword);
		//// LOGGER.debug(password);
		String alias = PCrypt.getPcrypt(CryptoUtilities.readKeyFile(keyFile)).decrypt(aliasKey);
		//// LOGGER.debug(alias);
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		String keyDeStoreFile = PCrypt.getPcrypt(CryptoUtilities.readKeyFile(keyFile)).decrypt(keyStoreFile);
		//// LOGGER.debug(keyDeStoreFile);
		keyStore.load(new FileInputStream(keyDeStoreFile), password.toCharArray());
		key = keyStore.getKey(alias, password.toCharArray());
		// LOGGER.debug("Crypto Initializeds");
	}

	public static String encryptData(String data) throws Exception {

		if (cryptoApplied) {
			if (key != null) {
				SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), cryptoAlgorithm);
				Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
				data = new String(data.getBytes(), StandardCharsets.UTF_8.name());
				byte[] byteData = cipher.doFinal(data.getBytes());
				return new String(Base64.encodeBase64(byteData), StandardCharsets.UTF_8.name());
			}
		}
		return data;
	}

	public static String decryptData(String data) throws Exception {
		if (cryptoApplied) {
			if (key != null) {
				Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] encrypted = Base64.decodeBase64(data.getBytes());
				return new String(cipher.doFinal(encrypted));
			}
		}
		return data;
	}

	private static Key key = null;
	private static String cryptoAlgorithm = null;
}
