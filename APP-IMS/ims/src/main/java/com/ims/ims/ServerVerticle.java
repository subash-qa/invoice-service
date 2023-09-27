package com.ims.ims;

import com.ims.common.AppRestAPIBaseVerticle;
import com.ims.common.util.ConfigConstants;
import com.ims.common.util.Constants;
import com.ims.crypto.Crypto;
import com.ims.ims.util.AddressConstants;
import com.ims.ims.util.SqlQueries;
import com.ims.process.PreProcessor;
import com.ims.process.builder.PreProcessBuilder;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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
		router.route("/api/ims/*").handler(BodyHandler.create());

		router.post("/api/ims/" + AddressConstants.INSERT_INVOICE).handler(routingContext -> {
			System.out.println(routingContext.getBodyAsJson());
			try {
				JsonObject data = routingContext.getBodyAsJson();
						System.out.println(data);
						processDbEbRequest(routingContext,
								PreProcessor.doProcess(data,
										PreProcessBuilder.getPreProcessList(AddressConstants.INSERT_INVOICE)),
								AddressConstants.INSERT_INVOICE, SqlQueries.INSERT_INVOICE);
				 
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.INSERT_INVOICE);
			}
		});

		router.get("/api/ims/" + AddressConstants.GET_INVOICE).handler(routingContext -> {

			JsonObject data = new JsonObject().put("employeeSno", routingContext.request().getParam("employeeSno"))
					.put("isEqual", routingContext.request().getParam("isEqual"));
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(data,
								PreProcessBuilder.getPreProcessList(AddressConstants.GET_INVOICE)),
						AddressConstants.GET_INVOICE, SqlQueries.GET_INVOICE);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.GET_INVOICE);
			}
		});



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

	@SuppressWarnings("unchecked")
	public void mediaUpload(JsonObject jsonObject, Handler<AsyncResult<JsonObject>> mediaHandler) {
		System.out.println(jsonObject);
		if (jsonObject != null) {
			processWebClientRequest(jsonObject, 8036, "localhost", "/api/ims/insert_media", result -> {
				System.out.println("mediaUpload" + result.result());
				if (result.succeeded()) {
					mediaHandler.handle(Future.succeededFuture(result.result()));
				} else {
					mediaHandler.handle(Future.failedFuture(result.cause()));
				}
			});
		} else {
			mediaHandler.handle(Future.succeededFuture(new JsonObject().put("mediaSno", null)));
		}

	}
}
