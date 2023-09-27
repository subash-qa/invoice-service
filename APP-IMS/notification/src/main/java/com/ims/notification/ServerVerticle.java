package com.ims.notification;


import com.ims.common.AppRestAPIBaseVerticle;
import com.ims.common.util.ConfigConstants;
import com.ims.common.util.Constants;
import com.ims.crypto.Crypto;
import com.ims.process.PreProcessor;
import com.ims.process.builder.PreProcessBuilder;
import com.ims.util.AddressConstants;
import com.ims.util.SqlQueries;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.json.schema.Schema;
import io.vertx.reactivex.json.schema.SchemaParser;
import io.vertx.reactivex.json.schema.SchemaRouter;

public class ServerVerticle extends AppRestAPIBaseVerticle {

	Logger log = LoggerFactory.getLogger(MainVerticle.class.getName());

	private HttpServer httpServer;

	SchemaParser schemaParser;
	Schema schema;

	public static String DB_SCHEMA = Constants.DEFAULT_SCHEMA;

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		super.start();

		DB_SCHEMA = Crypto.decryptData(config().getString(ConfigConstants.DB_SCHEMA_KEY));
		CONTENT_TYPE = config().getString(ConfigConstants.CONTENT_TYPE_KEY);
		ACCESS_CONTROL_ALLOW_ORIGIN = config().getString(ConfigConstants.ACCESS_CONTROL_ALLOW_ORIGIN_KEY);

		log.info("ServerVerticle Started");

		SchemaRouter schemaRouter = SchemaRouter.create(vertx, new SchemaRouterOptions());
		schemaParser = SchemaParser.createDraft201909SchemaParser(schemaRouter);

		final Router router = Router.router(vertx);
		enableCorsSupport(router);

		router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.route("/api/*").handler(BodyHandler.create());

		router.get("/api/" + AddressConstants.GET_NOTIFICATION_COUNT).handler(routingContext -> {
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(
								new JsonObject().put("appUserSno", routingContext.request().getParam("appUserSno")),
								PreProcessBuilder.getPreProcessList(AddressConstants.GET_NOTIFICATION_COUNT)),
						AddressConstants.GET_NOTIFICATION_COUNT, SqlQueries.GET_NOTIFICATION_COUNT);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.GET_NOTIFICATION_COUNT);
			}
		});

		router.get("/api/" + AddressConstants.GET_ALL_NOTIFICATION).handler(routingContext -> {
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(new JsonObject().put("appUserSno", routingContext.request().getParam("appUserSno")),
								PreProcessBuilder.getPreProcessList(AddressConstants.GET_ALL_NOTIFICATION)),
						AddressConstants.GET_ALL_NOTIFICATION, SqlQueries.GET_ALL_NOTIFICATION);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.GET_ALL_NOTIFICATION);
			}
		});

		router.put("/api/" + AddressConstants.UPDATE_NOTIFICATION_STATUS).handler(routingContext -> {
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(routingContext.getBodyAsJson(),
								PreProcessBuilder.getPreProcessList(AddressConstants.UPDATE_NOTIFICATION_STATUS)),
						AddressConstants.UPDATE_NOTIFICATION_STATUS, SqlQueries.UPDATE_NOTIFICATION_STATUS);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.UPDATE_NOTIFICATION_STATUS);
			}
		});

		this.httpServer = vertx.createHttpServer();
		int portNo = Integer.parseInt(Crypto.decryptData(config().getString(ConfigConstants.HTTP_PORT_NO_KEY)));
		if (this.httpServer != null && portNo > 0) {
			this.httpServer.requestHandler(router).requestStream().toFlowable().subscribe();
			this.httpServer.rxListen(portNo);
			this.httpServer.requestHandler(router).rxListen(portNo).subscribe();
		}
	}

	public void sendSms(JsonObject data) {
		log.info("sendSms started");
		vertx.eventBus().send("send_sms", data);
	}

	public void sendPublish(JsonObject data) {
		log.info("sendPublish started");
		vertx.eventBus().send("publish_msg", data);
	}

	public void sendNotification(JsonObject data) {
		log.info("sendNotification started");
		vertx.eventBus().send("send_notification", data);
	}
}
