package com.ims.ims;

import com.ims.common.util.Constants;
import com.ims.common.util.Mail;
import com.ims.db.DatabaseConnectionVerticle;
import com.ims.ims.util.AddressConstants;

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

		consumer = vertx.eventBus().consumer(AddressConstants.INSERT_INVOICE);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.INSERT_INVOICE, handler -> {
						
						if (handler.succeeded()) {

							message.reply(succesPostHandler(handler.result()));

						} else {
							log.error("ConsumerVerticle " + AddressConstants.INSERT_INVOICE + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});

		consumer = vertx.eventBus().consumer(AddressConstants.GET_INVOICE);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.GET_INVOICE, handler -> {
						if (handler.succeeded()) {

							message.reply(successGetHandler((JsonArray) handler.result()));

						} else {
							log.error("GET_ALL_EMPLOYEE " + AddressConstants.GET_INVOICE + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});


		consumer = vertx.eventBus().consumer("send_email");
		consumer.handler(message -> {
			try {
				Mail.sendMail("", message.body().getString("email"), null, message.body().getString("template"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		super.start();
	}

	public void sendEmail(JsonObject data) {
		log.info("sendEmail started");
		vertx.eventBus().send("send_email", data);
	};

}
