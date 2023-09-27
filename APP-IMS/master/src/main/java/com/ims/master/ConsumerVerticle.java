package com.ims.master;

import com.ims.common.util.Constants;
import com.ims.db.DatabaseConnectionVerticle;
import com.ims.master.util.AddressConstants;

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
		
		
		consumer = vertx.eventBus().consumer(AddressConstants.INSERT_JOB_ROLE);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.INSERT_JOB_ROLE, handler -> {
						if (handler.succeeded()) {

							message.reply(succesPostHandler(handler.result()));

						} else {
							log.error("INSERT_JOB_ROLE " + AddressConstants.INSERT_JOB_ROLE + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});	
		
		
		
		consumer = vertx.eventBus().consumer(AddressConstants.GET_JOB_ROLE);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.GET_JOB_ROLE, handler -> {
						if (handler.succeeded()) {

							message.reply(successGetHandler((JsonArray) handler.result()));

						} else {
							log.error("GET_JOB_ROLE " + AddressConstants.GET_JOB_ROLE + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});

		

		consumer = vertx.eventBus().consumer(AddressConstants.INSERT_DEPARTMENT);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.INSERT_DEPARTMENT, handler -> {
						if (handler.succeeded()) {

							message.reply(succesPostHandler(handler.result()));

						} else {
							log.error("INSERT_DEPARTMENT " + AddressConstants.INSERT_DEPARTMENT + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});	
		
		
		
		consumer = vertx.eventBus().consumer(AddressConstants.GET_DEPARTMENT);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.GET_DEPARTMENT, handler -> {
						if (handler.succeeded()) {

							message.reply(successGetHandler((JsonArray) handler.result()));

						} else {
							log.error("GET_DEPARTMENT " + AddressConstants.GET_DEPARTMENT + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});
		
		

        consumer = vertx.eventBus().consumer(AddressConstants.UPDATE_JOB_ROLE);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.UPDATE_JOB_ROLE, handler -> {
				if (handler.succeeded()) {
					message.reply(succesPutHandler((JsonObject) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.UPDATE_JOB_ROLE + " error "
							+ handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.UPDATE_DEPARTMENT);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.UPDATE_DEPARTMENT, handler -> {
				if (handler.succeeded()) {
					message.reply(succesPutHandler((JsonObject) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.UPDATE_DEPARTMENT + " error "
							+ handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.DELETE_JOB_ROLE);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.DELETE_JOB_ROLE, handler -> {
				if (handler.succeeded()) {

					message.reply(succesDeleteHandler((JsonObject) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.DELETE_JOB_ROLE + " error " + handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.DELETE_DEPARTMENT);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.DELETE_DEPARTMENT, handler -> {
				if (handler.succeeded()) {

					message.reply(succesDeleteHandler((JsonObject) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.DELETE_DEPARTMENT + " error " + handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });




        consumer = vertx.eventBus().consumer(AddressConstants.CREATE_LEAVE_SETTINGS);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.CREATE_LEAVE_SETTINGS, handler -> {
				if (handler.succeeded()) {
					message.reply(succesPostHandler((JsonObject) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.CREATE_LEAVE_SETTINGS + " error "
							+ handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.GET_LEAVE_SETTINGS);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.GET_LEAVE_SETTINGS, handler -> {
				if (handler.succeeded()) {

					message.reply(successGetHandler((JsonArray) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.GET_LEAVE_SETTINGS + " error " + handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.UPDATE_LEAVE_SETTINGS);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
        			AddressConstants.UPDATE_LEAVE_SETTINGS, handler -> {
        				if (handler.succeeded()) {
        					message.reply(succesPutHandler((JsonObject) handler.result()));

        				} else {
        					log.error("ConsumerVerticle " + AddressConstants.UPDATE_LEAVE_SETTINGS + " error "
        							+ handler.cause());
        					message.reply(errorJSON(handler.cause().toString()));
        				}
        			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.CREATE_LEAVE_DAYS_CONFIG);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.CREATE_LEAVE_DAYS_CONFIG, handler -> {
				if (handler.succeeded()) {
					message.reply(succesPostHandler((JsonObject) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.CREATE_LEAVE_DAYS_CONFIG + " error "
							+ handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.GET_LEAVE_DAYS_CONFIG);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
			AddressConstants.GET_LEAVE_DAYS_CONFIG, handler -> {
				if (handler.succeeded()) {

					message.reply(successGetHandler((JsonArray) handler.result()));

				} else {
					log.error("ConsumerVerticle " + AddressConstants.GET_LEAVE_DAYS_CONFIG + " error " + handler.cause());
					message.reply(errorJSON(handler.cause().toString()));
				}
			});
        });

        consumer = vertx.eventBus().consumer(AddressConstants.UPDATE_LEAVE_DAYS_CONFIG);
        consumer.handler(message -> {
        	runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
        			AddressConstants.UPDATE_LEAVE_DAYS_CONFIG, handler -> {
		if (handler.succeeded()) {
			message.reply(succesPutHandler((JsonObject) handler.result()));

		} else {
			log.error("ConsumerVerticle " + AddressConstants.UPDATE_LEAVE_DAYS_CONFIG + " error "
					+ handler.cause());
			message.reply(errorJSON(handler.cause().toString()));
		}
        			});
        });


		
		

//		consumer = vertx.eventBus().consumer(AddressConstants.UPDATE_ADDRESS);
//		consumer.handler(message -> {
//			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
//					AddressConstants.UPDATE_ADDRESS, handler -> {
//						if (handler.succeeded()) {
//
//							message.reply(succesPutHandler((JsonObject) handler.result()) );
//
//						} else {
//							log.error("ConsumerVerticle " + AddressConstants.UPDATE_ADDRESS + " error "
//									+ handler.cause());
//							message.reply(errorJSON(handler.cause().toString()));
//						}
//					});
//		});
//
//		consumer = vertx.eventBus().consumer(AddressConstants.DELETE_ADDRESS);
//		consumer.handler(message -> {
//			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
//					AddressConstants.DELETE_ADDRESS, handler -> {
//						if (handler.succeeded()) {
//
//							message.reply( handler.result());
//
//						} else {
//							log.error("ConsumerVerticle " + AddressConstants.DELETE_ADDRESS + " error "
//									+ handler.cause());
//							message.reply(errorJSON(handler.cause().toString()));
//						}
//					});
//		});

	 
		
		super.start();
	}

}
