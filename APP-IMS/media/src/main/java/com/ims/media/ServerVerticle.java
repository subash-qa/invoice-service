package com.ims.media;

import com.ims.common.AppRestAPIBaseVerticle;
import com.ims.common.util.ConfigConstants;
import com.ims.common.util.Constants;
import com.ims.crypto.Crypto;
import com.ims.media.util.AddressConstants;
import com.ims.media.util.SqlQueries;
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
		

		router.post("/api/ims/" + AddressConstants.INSERT_MEDIA).handler(routingContext -> { 
			try {
				getBlobContainerPermissions(blobContainerPermissions -> {

					JsonObject data = routingContext.getBodyAsJson();
//					System.out.println(blobContainerPermissions.succeeded());
					if (blobContainerPermissions.succeeded()) {
						try {
							vertx.executeBlocking(future -> {
							uploadMultimedia(blobContainerPermissions.result(), null, null, data.getJsonArray("mediaList"),
									response -> {
										if (response.succeeded()) {
											System.out.println("****"+response.result());
											if (response.result().size() > 0) {
												data.put("mediaList", response.result());
											} else {
												data.put("mediaList", null);
											}
//											System.out.println(data.getJsonArray("media"));
											try {
												processDbEbRequest(routingContext,
														PreProcessor.doProcess(data,
																PreProcessBuilder.getPreProcessList(
																		AddressConstants.INSERT_MEDIA)),
														AddressConstants.INSERT_MEDIA, SqlQueries.INSERT_MEDIA);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}

									});
							}, (Handler<AsyncResult<Void>>) event ->

							{
								event.cause();
							});
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				});
//				processDbEbRequest(routingContext,
//						PreProcessor.doProcess(routingContext.getBodyAsJson(),
//								PreProcessBuilder.getPreProcessList(AddressConstants.INSERT_MEDIA)),
//						AddressConstants.INSERT_MEDIA, SqlQueries.INSERT_MEDIA);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.INSERT_MEDIA);
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
	public void mediaUpload(JsonObject jsonObject,Handler<AsyncResult<JsonObject>> mediaHandler) {
		processWebClientRequest(jsonObject, 8068, "localhost", "/api/insert_media", result ->{
//			System.out.println("mediaUpload"+result.result());
			if(result.succeeded()) {
				mediaHandler.handle(Future.succeededFuture(result.result()));
			}else {
				mediaHandler.handle(Future.failedFuture(result.cause()));
			}
		});
	}
}
