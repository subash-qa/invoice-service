package com.ims.config;

import com.ims.common.AppRestAPIBaseVerticle;
import com.ims.common.util.AddressConstants;
import com.ims.common.util.ConfigConstants;
import com.ims.common.util.Constants;
import com.ims.config.util.SqlQueries;
import com.ims.process.PreProcessor;
import com.ims.process.builder.PreProcessBuilder;

//import com.ems.common.AppRestAPIBaseVerticle;
//import com.ems.common.util.AddressConstants;
//import com.ems.common.util.ConfigConstants;
//import com.ems.common.util.Constants;
//import com.ems.config.util.SqlQueries;
//import com.ems.process.PreProcessor;
//import com.ems.process.builder.PreProcessBuilder;

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


		log.info("ServerVerticle Started");

		SchemaRouter schemaRouter = SchemaRouter.create(vertx, new SchemaRouterOptions());
		schemaParser = SchemaParser.createDraft201909SchemaParser(schemaRouter);

		final Router router = Router.router(vertx);
		enableCorsSupport(router);

		router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.route("/api/*").handler(BodyHandler.create());
		router.get("/api/ims/" + AddressConstants.GET_CONFIG).handler(routingContext -> {
			try {
				JsonObject obj = new JsonObject();
				obj.put("environmentSno" , routingContext.request().getParam("environmentSno"));
				obj.put("moduleSno" , routingContext.request().getParam("moduleSno"));
				obj.put("subModuleSno" , routingContext.request().getParam("subModuleSno"));
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(obj,
								PreProcessBuilder.getPreProcessList(AddressConstants.GET_CONFIG)),
						AddressConstants.GET_CONFIG, SqlQueries.GET_CONFIG);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.GET_CONFIG);
			}
		});
		
		this.httpServer = vertx.createHttpServer();
		int portNo = Integer.parseInt(config().getString(ConfigConstants.HTTP_PORT_NO_KEY,"8081"));
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
