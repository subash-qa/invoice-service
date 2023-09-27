package com.ims.master;

import com.ims.common.AppRestAPIBaseVerticle;
import com.ims.common.util.ConfigConstants;
import com.ims.common.util.Constants;
import com.ims.crypto.Crypto;
import com.ims.process.PreProcessor;
import com.ims.process.builder.PreProcessBuilder;
import com.ims.master.util.AddressConstants;
import com.ims.master.util.SqlQueries;

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
		router.route("/api/ems/*").handler(BodyHandler.create());
		
		
		
		router.post("/api/ems/" + AddressConstants.INSERT_JOB_ROLE).handler(routingContext -> {
			System.out.println(routingContext.getBodyAsJson());
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(routingContext.getBodyAsJson(),
								PreProcessBuilder.getPreProcessList(AddressConstants.INSERT_JOB_ROLE)),
						AddressConstants.INSERT_JOB_ROLE, SqlQueries.INSERT_JOB_ROLE);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.INSERT_JOB_ROLE);
			}
		});
	
	
	router.get("/api/ems/" + AddressConstants.GET_JOB_ROLE).handler(routingContext -> {
		System.out.println(routingContext.getBodyAsJson());
		try {
			processDbEbRequest(routingContext,
					PreProcessor.doProcess(
							new JsonObject().put("projectSno", routingContext.request().getParam("projectSno")),
							PreProcessBuilder.getPreProcessList(AddressConstants.GET_JOB_ROLE)),
					AddressConstants.GET_JOB_ROLE, SqlQueries.GET_JOB_ROLE);
		} catch (Exception e) {
			processException(e, routingContext, AddressConstants.GET_JOB_ROLE);
		}
	});
		
		router.post("/api/ems/" + AddressConstants.INSERT_DEPARTMENT).handler(routingContext -> {
				System.out.println(routingContext.getBodyAsJson());
				try {
					processDbEbRequest(routingContext,
							PreProcessor.doProcess(routingContext.getBodyAsJson(),
									PreProcessBuilder.getPreProcessList(AddressConstants.INSERT_DEPARTMENT)),
							AddressConstants.INSERT_DEPARTMENT, SqlQueries.INSERT_DEPARTMENT);
				} catch (Exception e) {
					processException(e, routingContext, AddressConstants.INSERT_DEPARTMENT);
				}
			});
		
		
		router.get("/api/ems/" + AddressConstants.GET_DEPARTMENT).handler(routingContext -> {
			System.out.println(routingContext.getBodyAsJson());
			try {
				processDbEbRequest(routingContext,
						PreProcessor.doProcess(
								new JsonObject().put("projectSno", routingContext.request().getParam("projectSno")),
								PreProcessBuilder.getPreProcessList(AddressConstants.GET_DEPARTMENT)),
						AddressConstants.GET_DEPARTMENT, SqlQueries.GET_DEPARTMENT);
			} catch (Exception e) {
				processException(e, routingContext, AddressConstants.GET_DEPARTMENT);
			}
		});
		
		
		 router.put("/api/ems/" + AddressConstants.UPDATE_JOB_ROLE).handler(routingContext -> {
				try {
					processDbEbRequest(routingContext,
							PreProcessor.doProcess(routingContext.getBodyAsJson(),
									PreProcessBuilder.getPreProcessList(AddressConstants.UPDATE_JOB_ROLE)),
							AddressConstants.UPDATE_JOB_ROLE, SqlQueries.UPDATE_JOB_ROLE);
				} catch (Exception e) {
					processException(e, routingContext, AddressConstants.UPDATE_JOB_ROLE);
				}
			});

			router.put("/api/ems/" + AddressConstants.UPDATE_DEPARTMENT).handler(routingContext -> {
				try {
					processDbEbRequest(routingContext,
							PreProcessor.doProcess(routingContext.getBodyAsJson(),
									PreProcessBuilder.getPreProcessList(AddressConstants.UPDATE_DEPARTMENT)),
							AddressConstants.UPDATE_DEPARTMENT, SqlQueries.UPDATE_DEPARTMENT);
				} catch (Exception e) {
					processException(e, routingContext, AddressConstants.UPDATE_DEPARTMENT);
				}
			});
			
			router.delete("/api/ems/" + AddressConstants.DELETE_JOB_ROLE).handler(routingContext -> {
				try {
					JsonObject data = new JsonObject();
					data.put("jobRoleSno", routingContext.request().getParam("jobRoleSno"));

					System.out.println(data);
					processDbEbRequest(routingContext,
							PreProcessor.doProcess(data,
									PreProcessBuilder.getPreProcessList(AddressConstants.DELETE_JOB_ROLE)),
							AddressConstants.DELETE_JOB_ROLE, SqlQueries.DELETE_JOB_ROLE);
				} catch (Exception e) {
					processException(e, routingContext, AddressConstants.DELETE_JOB_ROLE);
				}
			});
			
			router.delete("/api/ems/" + AddressConstants.DELETE_DEPARTMENT).handler(routingContext -> {
				try {
					JsonObject data = new JsonObject();
					data.put("departmentSno", routingContext.request().getParam("departmentSno"));

					System.out.println(data);
					processDbEbRequest(routingContext,
							PreProcessor.doProcess(data,
									PreProcessBuilder.getPreProcessList(AddressConstants.DELETE_DEPARTMENT)),
							AddressConstants.DELETE_DEPARTMENT, SqlQueries.DELETE_DEPARTMENT);
				} catch (Exception e) {
					processException(e, routingContext, AddressConstants.DELETE_DEPARTMENT);
				}
			});
			
			

            router.post("/api/ems/" + AddressConstants.CREATE_LEAVE_SETTINGS).handler(routingContext -> {
            		System.out.println(routingContext.getBodyAsJson());
            		try {
            			processDbEbRequest(routingContext,
				PreProcessor.doProcess(routingContext.getBodyAsJson(),
						PreProcessBuilder.getPreProcessList(AddressConstants.CREATE_LEAVE_SETTINGS)),
				AddressConstants.CREATE_LEAVE_SETTINGS, SqlQueries.CREATE_LEAVE_SETTINGS);
            		} catch (Exception e) {
            			processException(e, routingContext, AddressConstants.CREATE_LEAVE_SETTINGS);
            		}
            	});

            router.get("/api/ems/" + AddressConstants.GET_LEAVE_SETTINGS).handler(routingContext -> {
            	try {
            		JsonObject data = new JsonObject();
            		System.out.println(data);
            		processDbEbRequest(routingContext,
				PreProcessor.doProcess(data, PreProcessBuilder.getPreProcessList(AddressConstants.GET_LEAVE_SETTINGS)),
				AddressConstants.GET_LEAVE_SETTINGS, SqlQueries.GET_LEAVE_SETTINGS);
            	} catch (Exception e) {
            		processException(e, routingContext, AddressConstants.GET_LEAVE_SETTINGS);
            	}
            });

            router.put("/api/ems/" + AddressConstants.UPDATE_LEAVE_SETTINGS).handler(routingContext -> {
            	try {
            		processDbEbRequest(routingContext,
				PreProcessor.doProcess(routingContext.getBodyAsJson(),
						PreProcessBuilder.getPreProcessList(AddressConstants.UPDATE_LEAVE_SETTINGS)),
				AddressConstants.UPDATE_LEAVE_SETTINGS, SqlQueries.UPDATE_LEAVE_SETTINGS);
            	} catch (Exception e) {
            		processException(e, routingContext, AddressConstants.UPDATE_LEAVE_SETTINGS);
            	}
            });

            router.post("/api/ems/" + AddressConstants.CREATE_LEAVE_DAYS_CONFIG).handler(routingContext -> {
            	System.out.println(routingContext.getBodyAsJson());
            	try {
            		processDbEbRequest(routingContext,
				PreProcessor.doProcess(routingContext.getBodyAsJson(),
						PreProcessBuilder.getPreProcessList(AddressConstants.CREATE_LEAVE_DAYS_CONFIG)),
				AddressConstants.CREATE_LEAVE_DAYS_CONFIG, SqlQueries.CREATE_LEAVE_DAYS_CONFIG);
            	} catch (Exception e) {
            		processException(e, routingContext, AddressConstants.CREATE_LEAVE_DAYS_CONFIG);
            	}
            });

            router.get("/api/ems/" + AddressConstants.GET_LEAVE_DAYS_CONFIG).handler(routingContext -> {
            	try {
            		JsonObject data = new JsonObject();
            		System.out.println(data);
            	processDbEbRequest(routingContext,
				PreProcessor.doProcess(data, PreProcessBuilder.getPreProcessList(AddressConstants.GET_LEAVE_DAYS_CONFIG)),
				AddressConstants.GET_LEAVE_DAYS_CONFIG, SqlQueries.GET_LEAVE_DAYS_CONFIG);
            	} catch (Exception e) {
            		processException(e, routingContext, AddressConstants.GET_LEAVE_DAYS_CONFIG);
            	}
            });

            router.put("/api/ems/" + AddressConstants.UPDATE_LEAVE_DAYS_CONFIG).handler(routingContext -> {
            	try {
            	processDbEbRequest(routingContext,
				PreProcessor.doProcess(routingContext.getBodyAsJson(),
						PreProcessBuilder.getPreProcessList(AddressConstants.UPDATE_LEAVE_DAYS_CONFIG)),
				AddressConstants.UPDATE_LEAVE_DAYS_CONFIG, SqlQueries.UPDATE_LEAVE_DAYS_CONFIG);
            	} catch (Exception e) {
            		processException(e, routingContext, AddressConstants.UPDATE_LEAVE_DAYS_CONFIG);
            	}
            });



		
		
//		router.put("/api/" + AddressConstants.UPDATE_ADDRESS).handler(routingContext -> {
//			try {
//				processDbEbRequest(routingContext,
//						PreProcessor.doProcess(routingContext.getBodyAsJson(),
//								PreProcessBuilder.getPreProcessList(AddressConstants.UPDATE_ADDRESS)),
//						AddressConstants.UPDATE_ADDRESS, SqlQueries.UPDATE_ADDRESS);
//			} catch (Exception e) {
//				processException(e, routingContext, AddressConstants.UPDATE_ADDRESS);
//			}
//		});
//
//		router.delete("/api/" + AddressConstants.DELETE_ADDRESS).handler(routingContext -> {
//			try {
//				System.out.println(routingContext.request().getParam("addressSno"));
//				processDbEbRequest(routingContext,
//						PreProcessor.doProcess(
//								new JsonObject().put("addressSno", routingContext.request().getParam("addressSno")),
//								PreProcessBuilder.getPreProcessList(AddressConstants.DELETE_ADDRESS)),
//						AddressConstants.DELETE_ADDRESS, SqlQueries.DELETE_ADDRESS);
//			} catch (Exception e) {
//				processException(e, routingContext, AddressConstants.DELETE_ADDRESS);
//				
//			}
//		});



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
