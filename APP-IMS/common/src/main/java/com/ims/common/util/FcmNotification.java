package com.ims.common.util;

import java.util.Random;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class FcmNotification {

	static Logger log = LoggerFactory.getLogger(FcmNotification.class.getName());

	public static void sendNotification(JsonObject data) throws Exception {
		System.out.println("in");
		System.out.println(data);
		try {
			if (data.getJsonArray("registration_ids") != null) {
				String urlString = "https://fcm.googleapis.com/fcm/send";
				HttpResponse<JsonNode> response = Unirest.post(urlString).header("content-type", "application/json")
						.header("Authorization",
								"key=AAAANag19UA:APA91bHKQlBi0GosGbtAT984EwGq2sPJY2mGChivuHRcncVT3z_ybWgW7oAB3WaTd3U-sSIlTszdPMxHZvToMlaeDk_FAkrWf8ZQ1eFYDOBpq7c623X0MROAjD5ql9uLbjljvpugetVz")
						.body(data.toString()).asJson();
				System.out.println(response.getBody());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			long referenceNumber = new Random().nextLong();
			log.error("Exception Occurred in create order method : " + referenceNumber);
			e.printStackTrace();
		}
	}

}