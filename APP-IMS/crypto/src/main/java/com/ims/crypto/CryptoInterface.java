package com.ims.crypto;

public interface CryptoInterface {
	public void initializeCrypto(String keyFile,
			String keyStoreFile,
			String keyStoreType,
			String algorithm,
			String fileKeyPassword,
			String aliasKey)throws Exception;
}
