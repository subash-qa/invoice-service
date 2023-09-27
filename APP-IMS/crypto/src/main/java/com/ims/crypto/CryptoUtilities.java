package com.ims.crypto;

import java.io.File;
import java.util.Scanner;

public class CryptoUtilities {

	@SuppressWarnings("resource")
	public static String readKeyFile(String fileName) throws Exception {
		Scanner scanner = new Scanner(new File(fileName)).useDelimiter("\\Z");
		String key = scanner.next();
		scanner.close();
		return key.substring((key.length()/2)-16,(key.length()/2)+16);
	}

}
