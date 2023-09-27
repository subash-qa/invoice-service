package com.ims.media;

import com.ims.common.util.Constants;
import com.ims.db.DatabaseConnectionVerticle;
import com.ims.media.util.AddressConstants;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import io.vertx.reactivex.ext.web.client.WebClient;

public class ConsumerVerticle extends DatabaseConnectionVerticle {

	private MessageConsumer<JsonObject> consumer;

	private static Logger log = LoggerFactory.getLogger(ConsumerVerticle.class);

	public void start() throws Exception {

		WebClientOptions wcOptions = new WebClientOptions();
		client = WebClient.create(vertx, wcOptions);

		

		consumer = vertx.eventBus().consumer(AddressConstants.INSERT_MEDIA);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.INSERT_MEDIA, handler -> {
						if (handler.succeeded()) {

							message.reply(handler.result());

						} else {
							log.error("ConsumerVerticle " + AddressConstants.INSERT_MEDIA + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});
		
		
		
		super.start();
	}

}
