package com.ims.config;

import com.ims.common.util.ConfigConstants;
import com.ims.crypto.Crypto;
import com.ims.crypto.PCrypt;
import com.ims.db.DatabaseConnectionVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

	Logger log = LoggerFactory.getLogger(MainVerticle.class.getName());

	public void start() throws Exception {
  
		super.start();

//		System.out.println(PCrypt.getPcrypt("fpRIthhCr4TuTAvE1JhVP5rUd4%%T68J").encrypt("./src/main/resources/napid-vendor.jceks"));
		System.out.println("./src/main/resources/swomb.jceks: "
				+ PCrypt.getPcrypt("fpRIthhCr4TuTAvE1JhVP5rUd4%%T68J").encrypt("./src/main/resources/swomb.jceks"));

		boolean cryptoApplied = config().getBoolean("crypto.applied");
		Crypto crypto = Crypto.getCryptoInstance();
		if (cryptoApplied) {
			crypto.initializeCrypto(config().getString(ConfigConstants.MASTER_KEY_FILE_NAME_KEY),
					config().getString(ConfigConstants.KEYSTORE_FILE_NAME_KEY),
					config().getString(ConfigConstants.KEYSTORE_TYPE_KEY),
					config().getString(ConfigConstants.CRYPTOGRAPHY_ALGORITHM_KEY),
					config().getString(ConfigConstants.FILE_KEY_PASSWORD_KEY),
					config().getString(ConfigConstants.FILE_KEY_ALIAS_NAME_KEY));
		}
		// System.out.println(Crypto.decryptData("bxByk0du/VBHNRQrsuU2LWSm1OySa3hsRX1KyEhG9ouCpYBiYKB8TQ2efYuAmiZ4zaOk3p4d0tRykKbbUutHiQ=="));

//		System.out.println("thV1IVCyNqvpprMtsvSh1GYCu0vkkElAro7uXZlOdPc=: "+Crypto.decryptData("thV1IVCyNqvpprMtsvSh1GYCu0vkkElAro7uXZlOdPc="));
//		System.out.println("10.0.0.6 : "+Crypto.encryptData("10.0.0.6"));
//		System.out.println("20.204.99.217 : "+Crypto.encryptData("20.204.99.217"));
		/*
		 * System.out.println("napid-pgsql11-dev-vendor1.postgres.database.azure.com : "
		 * +Crypto.encryptData("napid-pgsql11-dev-vendor1.postgres.database.azure.com"))
		 * ; System.out.
		 * println("jdbc:postgresql://napid-pgsql11-dev-vendor1.postgres.database.azure.com:5432/napid-db : "
		 * +Crypto.encryptData(
		 * "jdbc:postgresql://napid-pgsql11-dev-vendor1.postgres.database.azure.com:5432/napid-db"
		 * ));
		 * System.out.println("napadmin@napid-pgsql11-dev-vendor1 : "+Crypto.encryptData
		 * ("napadmin@napid-pgsql11-dev-vendor1"));
		 * System.out.println("jdbc:postgresql://localhost:5432/napid_db : "+Crypto.
		 * encryptData("jdbc:postgresql://localhost:5432/napid_db"));
		 * System.out.println("localhost : "+Crypto.encryptData("localhost"));
		 * System.out.println("napadmin : "+Crypto.encryptData("napadmin"));
		 * System.out.println("napid@123 : "+Crypto.encryptData("napid@123"));
		 * //System.out.println("napid-config : "+Crypto.encryptData("napid-config"));
		 */
		System.out.println("**************");
		System.out.println("localhost : " + Crypto.encryptData("localhost"));
		System.out.println("5432 : " + Crypto.encryptData("5432"));
		System.out.println("ims_admin : " + Crypto.encryptData("ims_admin"));
		System.out.println("ims123 : " + Crypto.encryptData("ims123"));
		System.out.println("ims_db : " + Crypto.encryptData("ims_db"));
		
		
		
		System.out.println("portal : " + Crypto.encryptData("portal"));
		System.out.println("8031 : " + Crypto.encryptData("8031"));
		
		System.out.println("master : " + Crypto.encryptData("master"));
		System.out.println("8032 : " + Crypto.encryptData("8032"));
		
		System.out.println("company : " + Crypto.encryptData("company"));
		System.out.println("8033 : " + Crypto.encryptData("8033"));
		
		System.out.println("customer : " + Crypto.encryptData("customer"));
		System.out.println("8034 : " + Crypto.encryptData("8034"));
		
		System.out.println("ims : " + Crypto.encryptData("ims"));
		System.out.println("8035 : " + Crypto.encryptData("8035"));
		
		System.out.println("media : " + Crypto.encryptData("media"));
		System.out.println("8036 : " + Crypto.encryptData("8036"));
		
		System.out.println("notification : " + Crypto.encryptData("notification"));
		System.out.println("8037 : " + Crypto.encryptData("8037"));


		/*
		 * System.out.println("napid-vendor : "+Crypto.encryptData("napid-vendor"));
		 * System.out.println("napid_db : "+Crypto.encryptData("napid_db"));
		 * System.out.println("napid_vendor : "+Crypto.encryptData("napid_vendor"));
		 * System.out.println("napid_authenticator : "+Crypto.encryptData(
		 * "napid_authenticator"));
		 */

//		System.out.println("10.0.0.6 : "+Crypto.encryptData("192.168.15.164"));
//		System.out.println("@@@@@"+Crypto.encryptData("192.168.15.164"));
//		System.out.println("10.0.0.6 : "+Crypto.decryptData(config().getString("db.host")));
//		System.out.println(PCrypt.getPcrypt("fpRIthhCr4TuTAvE1JhVP5rUd4%%T68J").decrypt("EzvxzEy7/EGAEgV728ADNxZ8gazuRAgW0Nf8ktRiRx0ae6pWPy543A=="));

		DeploymentOptions options = new DeploymentOptions();
		options.setConfig(config());
		// options.setInstances(Runtime.getRuntime().availableProcessors()).setConfig(config());

		vertx.rxDeployVerticle(ServerVerticle.class.getName(), options).subscribe();
		vertx.rxDeployVerticle(ConsumerVerticle.class.getName(), options).subscribe();
		vertx.rxDeployVerticle(DatabaseConnectionVerticle.class.getName(), options).subscribe();

	}
}
