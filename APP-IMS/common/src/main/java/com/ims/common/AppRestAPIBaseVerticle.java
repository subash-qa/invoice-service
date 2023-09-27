package com.ims.common;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;

import com.ims.common.util.Constants;
import com.ims.common.util.HttpConstants;
import com.ims.common.util.ResponseConstants;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpHeaders;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.handler.CorsHandler;


public class AppRestAPIBaseVerticle extends AbstractVerticle {

	protected String CONTENT_TYPE = HttpConstants.CONTENT_TYPE_APPLICATION_JSON;
	protected String ACCESS_CONTROL_ALLOW_ORIGIN = HttpConstants.ACCESS_CONTROL_ALLOW_ORIGIN_ALL;
	
//	public static final String ACCOUNT_SID = "AC6543002b2237548c9d58f424affe2566";
//	public static final String AUTH_TOKEN = "2e9435237c6451101aa9040dd024ddf1";
//	
//	private static final String EMAIL_FROM = "tocumulus22@gmail.com";
//	private static final String USERNAME = "tocumulus22@gmail.com";
//	private static final String PASSWORD = "tocumulus22@22";

	Logger log = LoggerFactory.getLogger(AppRestAPIBaseVerticle.class.getName());

	protected WebClient client;
	
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		super.start();
		client = WebClient.create(vertx);
	}

	/**
	 * Enable CORS support.
	 *
	 * @param router router instance
	 */
	protected void enableCorsSupport(Router router) {
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add(HttpConstants.HTTP_HEADER_X_REQUESTED_WITH);
		allowHeaders.add(HttpConstants.HTTP_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
		allowHeaders.add(HttpConstants.HTTP_HEADER_ORIGIN);
		allowHeaders.add(HttpConstants.HTTP_HEADER_CONTENT_TYPE);
		allowHeaders.add(HttpConstants.HTTP_HEADER_ACCEPT);
		allowHeaders.add("Authorization");
		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.PUT);
		allowMethods.add(HttpMethod.OPTIONS);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PATCH);

		router.route().handler(CorsHandler.create(HttpConstants.ACCESS_CONTROL_ALLOW_ORIGIN_ALL)
				.allowedHeaders(allowHeaders).allowedMethods(allowMethods));
	}

	protected JsonObject errorJSON(String msg) {

		JsonObject errorObj = new JsonObject();

		errorObj.put(ResponseConstants.JSON_KEY_ERROR_MSG, ResponseConstants.INTERNAL_SERVER_ERROR_MSG);
		errorObj.put(ResponseConstants.JSON_KEY_ERROR, msg);
		errorObj.put(ResponseConstants.JSON_KEY_RESPONSE_STATUS, 500);
		errorObj.put(ResponseConstants.JSON_KEY_IS_SUCCESS, false);

		return errorObj;
	}

	protected JsonObject successGetHandler(JsonArray jsonArray) {

		JsonObject successObj = new JsonObject();

		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_DATA, jsonArray);
		successObj.put(ResponseConstants.JSON_KEY_IS_SUCCESS, true);
		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_STATUS, 200);

		return successObj;
	}

	protected JsonObject succesPostHandler(Object jsonObject) {

		JsonObject successObj = new JsonObject();

		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_DATA, jsonObject);
		successObj.put(ResponseConstants.JSON_KEY_IS_SUCCESS, true);
		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_STATUS, 201);

		return successObj;
	}

	protected JsonObject succesPutHandler(JsonObject jsonObject) {

		JsonObject successObj = new JsonObject();

		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_DATA, jsonObject);
		successObj.put(ResponseConstants.JSON_KEY_IS_SUCCESS, true);
		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_STATUS, 204);

		return successObj;
	}

	protected JsonObject succesDeleteHandler(JsonObject jsonObject) {

		JsonObject successObj = new JsonObject();

		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_DATA, jsonObject);
		successObj.put(ResponseConstants.JSON_KEY_IS_SUCCESS, true);
		successObj.put(ResponseConstants.JSON_KEY_RESPONSE_STATUS, 204);

		return successObj;
	}

	protected void exceptionMailing(long referenceNumber, String requestBody) {

	}

	protected void processSuccessResponse(RoutingContext routingContext, JsonObject object, String methodName) {
		log.info(methodName + " success");
//		routingContext.response().end(Json.encodePrettily(object));
		System.out.println(ACCESS_CONTROL_ALLOW_ORIGIN);
		routingContext.response().setStatusMessage("Success")
//		.putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
//		.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN)
		.end(Json.encodePrettily(object));
//		routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
//				.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN)
//				.setStatusMessage("Success").end(Json.encodePrettily(object));
	}

	protected void processErrorResponse(RoutingContext routingContext, String errorMessage, String methodName) {
		log.info(methodName + " failure");
		log.error(methodName + " failure");
		routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
				.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN)
				.setStatusMessage("Failure").end(Json.encodePrettily(errorJSON(errorMessage)));
	}

	protected void processException(Exception e, RoutingContext routingContext, String methodName) {
		long referenceNumber = new Random().nextLong();
		log.error("Exception Occurred in " + methodName + " method : " + referenceNumber);
		exceptionMailing(referenceNumber, routingContext.getBodyAsString());
		e.printStackTrace();
		processErrorResponse(routingContext, ResponseConstants.INTERNAL_SERVER_ERROR_MSG, methodName);
	}

	protected void processDbEbRequest(RoutingContext routingContext, JsonObject data, String methodAddress,
			String sqlQuery) {
		log.info(methodAddress + " started");
		if (data != null) {
			vertx.eventBus()
					.rxRequest(methodAddress, new JsonObject().put(Constants.QUERY, sqlQuery).put(Constants.DATA, data))
					.subscribe(response -> {
//						System.out.println("---"+response.body());
						JsonObject responseBody = (JsonObject) response.body();
//						routingContext.response().end(Json.encodePrettily(responseBody));
						System.out.println("vijay *********** " + responseBody);
						processSuccessResponse(routingContext, responseBody, methodAddress);
					}, err -> {
						processErrorResponse(routingContext, err.getMessage(), methodAddress);
					});
		} else {
			processErrorResponse(routingContext, ResponseConstants.INPUT_ERROR_MSG, methodAddress);
		}
	}

	protected void processWebClientRequest(JsonObject data, int port, String host, String path,
			Handler<AsyncResult<JsonObject>> wcHandler) {
		client.post(port, host, path).putHeader("Content-Type", "application/json").sendJsonObject(data,
				webClientHandler -> {
					System.out.println(webClientHandler.succeeded());
					if (webClientHandler.succeeded()) {
						System.out.println(webClientHandler.result().bodyAsJsonObject());
						if(webClientHandler.result()!=null) {
							wcHandler.handle(Future.succeededFuture(webClientHandler.result().bodyAsJsonObject()));
						}else {
							wcHandler.handle(Future.failedFuture(webClientHandler.cause()));
						}
					} else {
						wcHandler.handle(Future.failedFuture(webClientHandler.cause()));
					}
				});
	}
	
	protected void uploadMultimedia(BlobContainerPermissions containerPermisson, String containerName,
			String storageReference, JsonArray media, Handler<AsyncResult<JsonArray>> handler) {
		System.out.println("uploadMultimedia Starting...");
		vertx.executeBlocking(future -> {
			JsonArray uploadedMedia = new JsonArray();
			if (media != null && media.size() > 0) {

				AtomicInteger count = new AtomicInteger(0);
				media.forEach(item -> {
					JsonObject data = (JsonObject) item;
					if (data != null && data.getBoolean("isUploaded") == null) {
						try {
//							String BLOB_STORAGE_REFERENCE_URL  = "DefaultEndpointsProtocol=https;AccountName=swombclientblob;AccountKey=TgYekIJiLUfoIFQBDdF1nffA6AGuIbCDVo3OZSGe6K6gUOo2RfunVeMvyDlir3x1/KRJ9nd1WbGp+ASt8o3TLA==;EndpointSuffix=core.windows.net";
							String BLOB_STORAGE_REFERENCE_URL  = "DefaultEndpointsProtocol=https;AccountName=swombclientblob;AccountKey=TgYekIJiLUfoIFQBDdF1nffA6AGuIbCDVo3OZSGe6K6gUOo2RfunVeMvyDlir3x1/KRJ9nd1WbGp+ASt8o3TLA==;EndpointSuffix=core.windows.net";
							String sr = BLOB_STORAGE_REFERENCE_URL;
							getBlobClientReference(sr, resClient -> {
								if (resClient.succeeded()) {
									try {

										String container = "internals01";
										createContainer(resClient.result(), container, resContainer -> {
											if (resContainer.succeeded()) {

												CloudBlockBlob blockBlob = null;
												CloudBlobContainer blobContainer = null;

												blobContainer = resContainer.result();
												try {
													blobContainer.uploadPermissions(containerPermisson);
												} catch (StorageException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}

												String fileType = "";

												// image OR Video OR other files upload started
												String downloadUrl = null;
												if (data.getString("mediaUrl") != null) {
													fileType = data.getString("fileType");
													String blobFileName = createRandomName() + fileType;
													try {
														blockBlob = blobContainer.getBlockBlobReference(blobFileName);
													} catch (URISyntaxException | StorageException e) {
														e.printStackTrace();
													}
													String multimediaParam = data.getString("mediaUrl").split(",")[1];
													multimediaParam = multimediaParam.replaceAll(" ", "+");

													byte[] imageByte = Base64.decodeBase64(multimediaParam.getBytes());

													try {
														blockBlob.uploadFromByteArray(imageByte, 0, imageByte.length);
													} catch (StorageException | IOException e) {
														e.printStackTrace();
													} catch (java.io.IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

													downloadUrl = blockBlob.getUri().toString();
													System.out.println("Download URL is: " + downloadUrl);
												}

												String thumbnailfileType = "";
												String thumbnailDownloadUrl = null;
												if (data.getString("thumbnailUrl") != null) {

													thumbnailfileType = data.getString("fileType");
													String thumbnailFileName = createRandomName() + thumbnailfileType;

													try {
														blockBlob = blobContainer
																.getBlockBlobReference(thumbnailFileName);
													} catch (URISyntaxException | StorageException e) {
														e.printStackTrace();
													}
													String thumbnailImageParam = data.getString("thumbnailUrl")
															.split(",")[1];
													thumbnailImageParam = thumbnailImageParam.replaceAll(" ", "+");

													byte[] thumbnailImageByte = Base64
															.decodeBase64(thumbnailImageParam.getBytes());
													try {
														blockBlob.uploadFromByteArray(thumbnailImageByte, 0,
																thumbnailImageByte.length);
													} catch (StorageException | IOException e) {
														e.printStackTrace();
													} catch (java.io.IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

													thumbnailDownloadUrl = blockBlob.getUri().toString();
													System.out.println(
															"thumbnail Download URL is: " + thumbnailDownloadUrl);
												}
												
												if(data.getString("documentType") != null) {
													uploadedMedia.add(new JsonObject().put("mediaUrl", downloadUrl)
															.put("contentType", data.getString("contentType"))
															.put("mediaType", data.getString("mediaType"))
															.put("thumbnailUrl", thumbnailDownloadUrl)
															.put("mediaSize", data.getString("mediaSize"))
															.put("mediaDetailDescription", data.getString("mediaDetailDescription"))
															.put("mediaSno", data.getString("mediaSno"))
															.put("isUploaded", true)
															.put("documentType", data.getString("documentType")));
												}else {
													uploadedMedia.add(new JsonObject().put("mediaUrl", downloadUrl)
															.put("contentType", data.getString("contentType"))
															.put("mediaType", data.getString("mediaType"))
															.put("thumbnailUrl", thumbnailDownloadUrl)
															.put("mediaSize", data.getString("mediaSize"))
															.put("mediaDetailDescription", data.getString("mediaDetailDescription"))
															.put("mediaSno", data.getString("mediaSno"))
															.put("isUploaded", true));
												}
											} else {
												future.complete();
												System.out.println("Error createContainer " + resContainer.cause());
											}
										});
									} catch (StorageException | RuntimeException | URISyntaxException e) {
										future.complete();
										e.printStackTrace();
									}
								} else {
									future.complete();
									System.out.println("Error getBlobClientReference " + resClient.cause());
								}
							});

							if (media.size() == count.addAndGet(1)) {
								future.complete();
								handler.handle(Future.succeededFuture(uploadedMedia));

							}
						} catch (Exception e) {
							future.complete();
							System.out.println("Exception is with Cloud Upload.");
							e.printStackTrace();
						}
					} else {
						uploadedMedia.add(data);
						if (media.size() == count.addAndGet(1)) {
							future.complete();
							handler.handle(Future.succeededFuture(uploadedMedia));
						}
					}
				});

			} else {
				future.complete();
				handler.handle(Future.succeededFuture(uploadedMedia));

			}
		}, (Handler<AsyncResult<Void>>) event ->

		{
			event.cause();
		});
	}
	
	
	public static void getBlobContainerPermissions(Handler<AsyncResult<BlobContainerPermissions>> handler) {
		BlobContainerPermissions blobContainerPermissions = new BlobContainerPermissions();
		blobContainerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
		handler.handle(Future.succeededFuture(blobContainerPermissions));
	}

	public static void getBlobClientReference(String storageReference, Handler<AsyncResult<CloudBlobClient>> handler)
			throws RuntimeException, IOException, IllegalArgumentException, URISyntaxException, InvalidKeyException, java.security.InvalidKeyException {

		CloudStorageAccount storageAccount;
		try {
			System.out.println("getBlobClientReference");
			storageAccount = CloudStorageAccount.parse(storageReference);
			System.out.println(storageAccount);
			handler.handle(Future.succeededFuture(storageAccount.createCloudBlobClient()));
		} catch (IllegalArgumentException | URISyntaxException e) {
			System.out.println("\nConnection string specifies an invalid URI.");
			System.out.println("Please confirm the connection string is in the Azure connection string format.");
			throw e;
		} catch (InvalidKeyException e) {
			System.out.println("\nConnection string specifies an invalid key.");
			System.out.println("Please confirm the AccountName and AccountKey in the connection string are valid.");
			throw e;
		}

	}
	
	
	@SuppressWarnings("unused")
	private static void createContainer(CloudBlobClient blobClient, String containerName,
			Handler<AsyncResult<CloudBlobContainer>> handler) throws StorageException, RuntimeException, IOException,
			InvalidKeyException, IllegalArgumentException, URISyntaxException, IllegalStateException {

		// Create a new container
		CloudBlobContainer container = blobClient.getContainerReference(containerName);
		try {
			if (container.createIfNotExists() == false) {
				// throw new IllegalStateException(
				// String.format("Container with name \"%s\" already exists.", containerName));
			}
		} catch (StorageException s) {
			if (s.getCause() instanceof java.net.ConnectException) {
				System.out.println(
						"Caught connection exception from the client. If running with the default configuration please make sure you have started the storage emulator.");
			}
			throw s;
		}

		handler.handle(Future.succeededFuture(container));
	}
	
	
	static String createRandomName(String namePrefix) {
		return namePrefix + UUID.randomUUID().toString().replace("-", "");
	}

	static String createRandomName() {
		return UUID.randomUUID().toString();
	}
	
//
//
//public void sendSms(String fromNumber, String toNumber, String countryCode, String msg) {
//		try {
//			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//			System.out.println("+" + countryCode + toNumber);
//			Message message = Message
//					.creator(new PhoneNumber("+" + countryCode + toNumber), "MGa5a1faad6f5624eab20c979a9131eb51", msg)
//					.create();
//
////			Message message = Message.creator(new com.twilio.type.PhoneNumber("+" + countryCode + toNumber),
////					"MGa5a1faad6f5624eab20c979a9131eb51", msg).setStatusCallback(URI.create("http://postb.in/1234abcd")).create();
//
//			System.out.println(message.getStatus());
//			System.out.println(message.getSid());
//
//		} catch (TwilioException e) {
//			System.out.println(e.getMessage());
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public void sendSMS(String toPhone, String otp, String template) throws Exception {
//		String apiKey = "311d5218-7aae-11ea-9fa5-0200cd936042";
//		String urlString = "http://2factor.in/API/V1/" + apiKey + "/SMS/" + toPhone + "/" + otp + "/" + template;
////		String encodedUrl = urlString.replace(" ", "%20");
//		HttpResponse<String> response = (HttpResponse<String>) Unirest.get(urlString)
//				.header("content-type", "application/x-www-form-urlencoded").asString();
//	}
//
//	@SuppressWarnings("unchecked")
//	public void twoFactorSMS(JsonObject body) throws Exception {
//		// jdf
//		vertx.executeBlocking(future -> {
//			try {
//				String apiKey = "311d5218-7aae-11ea-9fa5-0200cd936042";
//				String urlString = "";
//				if (body.getString("template").equalsIgnoreCase("FeastO")) {
//					urlString = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=" + apiKey + "&to="
//							+ body.getString("toPhone") + "&from=FeastO&templatename=" + body.getString("template")
//							+ "&var1=" + body.getString("otp") + "&var2=" + body.getString("hashCode") + "";
//				} else if (body.getString("template").equalsIgnoreCase("Good_one")) {
//					urlString = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=" + apiKey + "&to="
//							+ body.getString("toPhone") + "&from=FeastO&templatename=" + body.getString("template")
//							+ "&var1=" + body.getString("var1") + "&var2=" + body.getString("var2") + "&var3="
//							+ body.getString("var3") + "&var4=" + body.getString("var4");
//				}
//
//				WebClient client = WebClient.create(vertx);
//				client.postAbs(urlString).putHeader("Content-Type", "application/x-www-form-urlencoded")
//						.sendJsonObject(new JsonObject(), ar -> {
//							if (ar.succeeded()) {
//								try {
//									log.info(Json.encodePrettily(ar.result().bodyAsJsonObject()));
//									log.info("sms Sent Success");
//
//								} catch (DecodeException e) {
//									log.info("sms went wrong " + ar.result().statusCode());
//								}
//
//							} else {
//								log.info("Notification Sent Error" + ar.cause());
//							}
//						});
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				long referenceNumber = new Random().nextLong();
//				log.error("Exception Occurred in verifyUser method : " + referenceNumber);
////				exceptionMailing(referenceNumber, null, e.getMessage(), body.toString());
//				e.printStackTrace();
//				future.complete();
//			}
//
//		}, (Handler<AsyncResult<Void>>) exevent -> {
//			exevent.cause();
//		});
//	}
//
//	protected void exceptionMailing(long referenceNumber, String requestBody, String exceptionString,
//			String dataString) {
//
//	}
//	
//	
//	public void sendMail(String message, String toEmail, String attachment, String template) throws Exception {
//		log.debug("Send Mail Started");
//		vertx.executeBlocking(future -> {
//			Properties prop = new Properties();
//			prop.put("mail.smtp.host", "smtp.gmail.com");
//			prop.put("mail.smtp.starttls.enable", "true");
//			prop.put("mail.smtp.auth", "true");
//			prop.put("mail.smtp.port", "587");
//			
//			Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
//				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//					return new javax.mail.PasswordAuthentication(EMAIL_FROM, PASSWORD);
//				}
//			});
//	
//			try {
//	
//				MimeMessage msg = new MimeMessage(session);
//	
//				// from
//				msg.setFrom(new InternetAddress(EMAIL_FROM, "Tocumulus"));
//	
//				// to
//				System.out.println(toEmail);
//				msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(toEmail));
//	
//				// subject
//				msg.setSubject("tocumulus");
//	
//				System.out.println(message);
//				msg.setText(message);
//				if (template != null) {
//					msg.setContent(template, "text/html; charset=utf-8");
//				}
//	
//				Transport.send(msg);
//	
//				System.out.println("Email sent successfully...");
//				log.error("Email sent successfully...");
//			} catch (AddressException e) {
//				log.error(e.getMessage());
//			} catch (MessagingException e) {
//				log.error(e.getMessage());
//			} catch (UnsupportedEncodingException e) {
//				log.error(e.getMessage());
//			}
//		}, (Handler<AsyncResult<Void>>) exevent -> {
//			exevent.cause();
//		});
//	}
};