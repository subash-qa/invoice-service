package com.ims.ims;

import com.ims.common.util.ConfigConstants;
import com.ims.crypto.Crypto;
import com.ims.db.DatabaseConnectionVerticle;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

	Logger log = LoggerFactory.getLogger(MainVerticle.class.getName());

	public void start() throws Exception {

		super.start();

		DeploymentOptions options = new DeploymentOptions();

		ConfigStoreOptions httpStore = new ConfigStoreOptions().setType("http")
				.setConfig(new JsonObject().put("host", config().getString("host"))
						.put("port", config().getInteger("port"))
						.put("path",
								config().getString("path") + "?environmentSno=" + config().getInteger("environmentSno")
										+ "&moduleSno=" + config().getInteger("moduleSno") + "&subModuleSno="
										+ config().getInteger("subModuleSno")));

		ConfigRetrieverOptions configOptions = new ConfigRetrieverOptions().addStore(httpStore);
		configOptions.setScanPeriod(24 * 60 * 60 * 1000);

		ConfigRetriever retriever = ConfigRetriever.create(vertx, configOptions);

		retriever.getConfig(ar -> {
			if (ar.failed()) {
				log.error("Error in Configuration Loading");System.out.println(ar.cause());
			} else {
				JsonObject config = ar.result();
				System.out.println(config);
				options.setConfig(config);
				boolean cryptoApplied = config().getBoolean("crypto.applied");
				Crypto crypto = Crypto.getCryptoInstance();
				try {
					if (cryptoApplied) {
						crypto.initializeCrypto(config.getString(ConfigConstants.MASTER_KEY_FILE_NAME_KEY),
								config.getString(ConfigConstants.KEYSTORE_FILE_NAME_KEY),
								config.getString(ConfigConstants.KEYSTORE_TYPE_KEY),
								config.getString(ConfigConstants.CRYPTOGRAPHY_ALGORITHM_KEY),
								config.getString(ConfigConstants.FILE_KEY_PASSWORD_KEY),
								config.getString(ConfigConstants.FILE_KEY_ALIAS_NAME_KEY));
					}

					log.info("MainVerticle started");

					//options.setInstances(Runtime.getRuntime().availableProcessors()).setConfig(config);

					vertx.rxDeployVerticle(DatabaseConnectionVerticle.class.getName(), options).subscribe();
					vertx.rxDeployVerticle(ServerVerticle.class.getName(), options).subscribe();
					vertx.rxDeployVerticle(ConsumerVerticle.class.getName(), options).subscribe();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
