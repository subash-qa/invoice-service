package com.ims.portal;

import com.ims.common.util.Constants;
import com.ims.db.DatabaseConnectionVerticle;
import com.ims.util.AddressConstants;

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
		
		
		consumer = vertx.eventBus().consumer(AddressConstants.GET_ENUM_NAMES);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.GET_ENUM_NAMES, handler -> {
						if (handler.succeeded()) {

							message.reply(successGetHandler((JsonArray) handler.result()));

						} else {
							log.error("ConsumerVerticle " + AddressConstants.GET_ENUM_NAMES + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});
		
		consumer = vertx.eventBus().consumer(AddressConstants.SIGNIN_USER);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.SIGNIN_USER, handler -> {
						if (handler.succeeded()) {
							message.reply(succesPostHandler(handler.result()));

						} else {
							log.error("ConsumerVerticle " + AddressConstants.SIGNIN_USER + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});
		
		
		consumer = vertx.eventBus().consumer(AddressConstants.UPDATE_PASSWORD);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.UPDATE_PASSWORD, handler -> {
						if (handler.succeeded()) {

							message.reply(handler.result());

						} else {
							log.error("ConsumerVerticle " + AddressConstants.UPDATE_PASSWORD + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});
		
		
//
//		consumer = vertx.eventBus().consumer(AddressConstants.VERIFY_USER);
//		consumer.handler(message -> {
//			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
//					AddressConstants.VERIFY_USER, handler -> {
//						if (handler.succeeded()) {
//							message.reply(succesPostHandler(handler.result()));
//							JsonObject result = (JsonObject) handler.result();
////							if (result.getJsonObject("data") != null) {
////								JsonObject otpdata = new JsonObject();
////								otpdata.put("toPhone",
////										message.body().getJsonObject(Constants.DATA).getString("mobileNumber"))
////										.put("otp", result.getJsonObject("data").getString("simOtp"))
////										.put("hashCode", message.body().getJsonObject(Constants.DATA).getString("hashCode"))
////										.put("template", "FeastO");
////								System.out.println("otp data   "+otpdata);
////								sendSMS(otpdata);
////							}
//
//						} else {
//							log.error("ConsumerVerticle " + AddressConstants.VERIFY_USER + " error "
//									+ handler.cause());
//							message.reply(errorJSON(handler.cause().toString()));
//						}
//					});
//		});
//		
//		consumer = vertx.eventBus().consumer(AddressConstants.OTP_VERIFY);
//		consumer.handler(message -> {
//			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
//					AddressConstants.OTP_VERIFY, handler -> {
//						if (handler.succeeded()) {
//							message.reply(succesPostHandler(handler.result()));
////							message.reply(handler.result());
//
//						} else {
//							log.error("ConsumerVerticle " + AddressConstants.OTP_VERIFY + " error "
//									+ handler.cause());
//							message.reply(errorJSON(handler.cause().toString()));
//						}
//					});
//		});
//		
		
		
		consumer = vertx.eventBus().consumer(AddressConstants.LOGOUT);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.LOGOUT, handler -> {
						if (handler.succeeded()) {

							message.reply(succesPutHandler((JsonObject) handler.result()));

						} else {
							log.error("ConsumerVerticle " + AddressConstants.LOGOUT + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

//		consumer = vertx.eventBus().consumer(AddressConstants.RESEND_OTP);
//		consumer.handler(message -> {
//			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
//					AddressConstants.RESEND_OTP, handler -> {
//						if (handler.succeeded()) {
//							message.reply(handler.result());
//							JsonObject result = (JsonObject) handler.result();
//							if (result != null) {
//								JsonObject otpdata = new JsonObject();
//								otpdata.put("toPhone",
//										result.getString("mobileNumber"))
//										.put("otp", result.getString("simOtp"))
//										.put("hashCode", message.body().getJsonObject(Constants.DATA).getString("hashCode"))
//										.put("template", "FeastO");
//								System.out.println("otp data   "+otpdata);
//								sendSMS(otpdata);
//							}
//						} else {
//							log.error("ConsumerVerticle " + AddressConstants.RESEND_OTP + " error "
//									+ handler.cause());
//							message.reply(errorJSON(handler.cause().toString()));
//						}
//					});
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
//		consumer = vertx.eventBus().consumer("send_sms");
//		consumer.handler(message -> {
//			try {
//				System.out.println(message.body());
//				new TwoFactorSMS().sendSms(message.body());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});

		super.start();
	}

	public void sendEmail(JsonObject data) {
		log.info("sendEmail started");
		vertx.eventBus().send("send_email", data);
	}

	public void sendSMS(JsonObject data) {
		log.info("sendSMS started");
		vertx.eventBus().send("send_sms", data);
	}

	public void sendNotification(JsonObject data) {
		log.info("sendNotification started");
		vertx.eventBus().send("send_notification", data);
	}
//	public void sendEmailUser(JsonObject result){
//		String Template = "<div style='margin: 0% 10%;'>" + "<h3> Hi Sir/Madam</h3>"
//				+ "<p>Your One Time Password (OTP) for signing into The SWOMB application is <h1><b>"
//				+ result.getString("emailOtp") + "</b></h1>Kindly do NOT reply to this.</p>"
//				+ "<p>Click on the below link to access the web app. </p>"
//				+ "<a href='/'>link</a>" + "<br>" + "<br>"
//				+ "<p>or download the mobile app </p>"
//				+ "<img src='https://firebasestorage.googleapis.com/v0/b/savvybidders-1.appspot.com/o/icon%2Fplay2.png?alt=media&token=06b98a6c-4f45-4c91-8972-85631fcf99fd' style='width: 10%;' />"
//				+ "<br>" + "<p>Best wishes,</p>" + "<p>The SWOMB Team</p>" + "</div>";
//		result.put("template", Template);
//		result.put("email",result.getString("email"));
//		sendEmail(null,);
//	}

}
