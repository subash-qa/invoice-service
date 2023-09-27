package com.ims.portal;


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
import io.vertx.core.json.Json;
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
		router.route("/api/ems/*").handler(BodyHandler.create());
		
		router.get("/api/ems/" + AddressConstants.GET_ENUM_NAMES).handler(routingContext -> {
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(
								new JsonObject().put("codeType", routingContext.request().getParam("codeType"))
								.put("filter1", routingContext.request().getParam("filter1")),
								PreProcessBuilder.getPreProcessList(AddressConstants.GET_ENUM_NAMES)),
						AddressConstants.GET_ENUM_NAMES, SqlQueries.GET_ENUM_NAMES);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.GET_ENUM_NAMES);
			}
		});

		router.post("/api/ems/" + AddressConstants.SIGNIN_USER).handler(routingContext -> {
			try { 
				
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(routingContext.getBodyAsJson(),
								PreProcessBuilder.getPreProcessList(AddressConstants.SIGNIN_USER)),
						AddressConstants.SIGNIN_USER, SqlQueries.SIGNIN_USER);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.SIGNIN_USER);
			}
		});
		

		router.put("/api/ems/" + AddressConstants.LOGOUT).handler(routingContext -> {
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(routingContext.getBodyAsJson(),
								PreProcessBuilder.getPreProcessList(AddressConstants.LOGOUT)),
						AddressConstants.LOGOUT, SqlQueries.LOGOUT);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.LOGOUT);
			}
		});
		
		router.put("/api/ems/" + AddressConstants.UPDATE_PASSWORD).handler(routingContext -> {
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(routingContext.getBodyAsJson(),
								PreProcessBuilder.getPreProcessList(AddressConstants.UPDATE_PASSWORD)),
						AddressConstants.UPDATE_PASSWORD, SqlQueries.UPDATE_PASSWORD);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.UPDATE_PASSWORD);
			}
		});
		
		
		
//		router.post("/api/" + AddressConstants.OTP_VERIFY).handler(routingContext -> {
//			System.out.println(routingContext.getBodyAsJson());
//			try {
//				processDbEbRequest(routingContext,
//						PreProcessor.doProcess(routingContext.getBodyAsJson(),
//								PreProcessBuilder.getPreProcessList(AddressConstants.OTP_VERIFY)),
//						AddressConstants.OTP_VERIFY, SqlQueries.OTP_VERIFY);
//			} catch (Exception e) {
//				processException(e, routingContext, AddressConstants.OTP_VERIFY);
//			}
//		});
//		
//		
		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		router.post("/api/" + AddressConstants.RESEND_OTP).handler(routingContext -> {
//			System.out.println(routingContext.getBodyAsJson());
//			try {
//				processDbEbRequest(routingContext,
//						PreProcessor.doProcess(routingContext.getBodyAsJson(),
//								PreProcessBuilder.getPreProcessList(AddressConstants.RESEND_OTP)),
//						AddressConstants.RESEND_OTP, SqlQueries.RESEND_OTP);
//			} catch (Exception e) {
//				processException(e, routingContext, AddressConstants.RESEND_OTP);
//			}
//		});
//		
		
		
		

		this.httpServer = vertx.createHttpServer();
//		int portNo = Integer.parseInt(config().getString(ConfigConstants.HTTP_PORT_NO_KEY, "8082")); 
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
