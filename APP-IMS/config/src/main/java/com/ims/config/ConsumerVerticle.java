package com.ims.config;


import com.ims.common.util.AddressConstants;
import com.ims.common.util.Constants;
import com.ims.db.DatabaseConnectionVerticle;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import io.vertx.reactivex.ext.web.client.WebClient;

public class ConsumerVerticle extends DatabaseConnectionVerticle {

	private MessageConsumer<JsonObject> consumer;

	private static Logger log = LoggerFactory.getLogger(ConsumerVerticle.class);

		public void start() throws Exception{
		log.info("ConsumerVerticle started");

		WebClientOptions wcOptions = new WebClientOptions();
		client = WebClient.create(vertx, wcOptions);

		consumer = vertx.eventBus().consumer(AddressConstants.GET_CONFIG);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.GET_CONFIG, handler -> {
						if (handler.succeeded()) {
							if(handler.result()!=null) {
								
								JsonObject dbResponse = (JsonObject)handler.result();
								
								if(dbResponse.getString("data")!=null) {
									message.reply(processConfigJSon(handler.result()));
								}else
								{
									message.reply(new JsonObject().put("data",new JsonObject().put("message", "no config found")));
								}
								
							}else {
//								message.reply("{\"message\":\"no config found\"}");
								message.reply(new JsonObject().put("data",new JsonObject().put("message", "no config found")));
							}
						} else {
							log.error("ConsumerVerticle " + AddressConstants.GET_CONFIG + " error " + handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});

		super.start();
	}

	public JsonObject processConfigJSon(Object jsonObject) {

		String jsonString = ((JsonObject) jsonObject).getJsonArray("data").encode();

		jsonString = jsonString.replaceAll("\\}", "");
		jsonString = jsonString.replaceAll("\\{", "");

		jsonString = jsonString.replaceAll("\\[", "");
		jsonString = jsonString.replaceAll("\\]", "");

		JsonObject outJson = new JsonObject("{" + jsonString + "}");

		return outJson;
	}

}
