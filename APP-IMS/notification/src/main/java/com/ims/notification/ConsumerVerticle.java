package com.ims.notification;


import com.ims.common.util.Constants;
import com.ims.common.util.TwoFactorSMS;
import com.ims.db.DatabaseConnectionVerticle;
import com.ims.util.AddressConstants;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
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


		consumer = vertx.eventBus().consumer(AddressConstants.GET_NOTIFICATION_COUNT);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.GET_NOTIFICATION_COUNT, handler -> {
						if (handler.succeeded()) {

							message.reply(handler.result());

						} else {
							log.error("ConsumerVerticle " + AddressConstants.GET_NOTIFICATION_COUNT + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});

		consumer = vertx.eventBus().consumer(AddressConstants.GET_ALL_NOTIFICATION);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.GET_ALL_NOTIFICATION, handler -> {
						if (handler.succeeded()) {

							message.reply(handler.result());

						} else {
							log.error("ConsumerVerticle " + AddressConstants.GET_ALL_NOTIFICATION + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});

		consumer = vertx.eventBus().consumer(AddressConstants.UPDATE_NOTIFICATION_STATUS);
		consumer.handler(message -> {
			runQuery(message.body().getString(Constants.QUERY), message.body().getJsonObject(Constants.DATA),
					AddressConstants.UPDATE_NOTIFICATION_STATUS, handler -> {
						if (handler.succeeded()) {

							message.reply(handler.result());

						} else {
							log.error("ConsumerVerticle " + AddressConstants.UPDATE_NOTIFICATION_STATUS + " error "
									+ handler.cause());
							message.reply(errorJSON(handler.cause().toString()));
						}
					});
		});

		consumer = vertx.eventBus().consumer("send_sms");
		consumer.handler(message -> {
			try {
				System.out.println(message.body());
				new TwoFactorSMS().sendSms(message.body());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

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
