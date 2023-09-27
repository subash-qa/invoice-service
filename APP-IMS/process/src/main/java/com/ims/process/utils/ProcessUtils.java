package com.ims.process.utils;

import com.ims.crypto.Crypto;

import io.vertx.core.json.JsonObject;

public class ProcessUtils {

	public static String encryptInput(String data) {
		if (data != null && data.length() > 0) {
			try {
				data = Crypto.encryptData(data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}

	public static String decryptInput(String data) {
		if (data != null && data.length() > 0) {
			try {
				data = Crypto.decryptData(data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}

	public static String maskData(String data, int type) {
		if (type == 1)
			return maskFixedLength(data, 8);
		else
			return maskAlternateData(data);
	}

	public static String maskFixedLength(String data, int maxLimit) {

		String maskedData = "";
		if (data != null && data.length() > 0) {

			if (data.length() == 1) {
				for (int i = 0; i < maxLimit - 1; i++) {
					data += "*";
				}
			} else if (data.length() == 2) {
				String tempData = data.substring(0, 1);
				for (int i = 0; i < maxLimit - 2; i++) {
					tempData += "*";
				}
				tempData += data.substring(1, 2);
				data = tempData;
			} else if (data.length() == 3) {
				String tempData = data.substring(0, 1);
				for (int i = 0; i < maxLimit - 2; i++) {
					tempData += "*";
				}
				tempData += data.substring(2, 3);
				data = tempData;
			} else {
				String tempData = data.substring(0, 2);
				for (int i = 0; i < maxLimit - 4; i++) {
					tempData += "*";
				}
				data = tempData + data.substring(data.length() - 2, data.length());
			}

			maskedData = data;

		}
		return maskedData;
	}

	public static String maskAlternateData(String data) {

		String maskedData = "";
		if (data != null && data.length() > 0) {
			try {

				for (int i = 0; i < data.length(); i++) {
					if (i % 2 == 0)
						maskedData += data.substring(i, i + 1);
					else
						maskedData += "*";
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return maskedData;
	}

	public static JsonObject encryptInputWithSalt(JsonObject data) {
		return data;
	}

	public static JsonObject decryptInputWithSalt(JsonObject data) {
		return data;
	}

}
