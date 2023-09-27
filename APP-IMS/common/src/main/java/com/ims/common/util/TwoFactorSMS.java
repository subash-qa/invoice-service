package com.ims.common.util;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import io.jsonwebtoken.io.IOException;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class TwoFactorSMS  {
	public static final String ACCOUNT_SID = "AC6543002b2237548c9d58f424affe2566";
	public static final String AUTH_TOKEN = "2e9435237c6451101aa9040dd024ddf1";
	
	Logger log = LoggerFactory.getLogger(TwoFactorSMS.class.getName());
	
	public  void sendSms(JsonObject body) throws Exception {
		System.out.println("in");
		System.out.println(body);
//		vertx.executeBlocking(future -> {
			System.out.println("executeBlocking");
//			try {
//				HttpURLConnection connection;
//				String apiKey = "311d5218-7aae-11ea-9fa5-0200cd936042";
//				String urlString = "";
//					urlString = "https://2factor.in/API/R1/?module=TRANS_SMS&apikey=" + apiKey + "&to="
//							+ body.getString("toPhone") + "&from=FeastO&templatename=" + body.getString("template")
//							+ "&var1=" + body.getString("otp") + "&var2=" + body.getString("hashCode") + "";
//					
//				connection = (HttpURLConnection) new URL(urlString).openConnection();
//				connection.setRequestMethod("GET");
//				connection.setDoOutput(true);
//			    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
//			    wr.flush();
//				
//				int responseCode = connection.getResponseCode();
//				if(responseCode == 200){
//					System.out.println("GET was successful.");
//				}
//				else if(responseCode == 401){
//					System.out.println("Wrong password.");
//				}
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				long referenceNumber = new Random().nextLong();
//				log.error("Exception Occurred in verifyUser method : " + referenceNumber);
//				e.printStackTrace();
//			}

	}
	
	
	public static void setPersonData(String name, String birthYear, String about) throws IOException, ProtocolException{
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://localhost:8080/people/" + name).openConnection();
			connection.setRequestMethod("POST");
			
			String postData = "name=" + URLEncoder.encode(name);
			postData += "&about=" + URLEncoder.encode(about);
			postData += "&birthYear=" + birthYear;
			
			connection.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		    wr.write(postData);
		    wr.flush();
			
			int responseCode = connection.getResponseCode();
			if(responseCode == 200){
				System.out.println("POST was successful.");
			}
			else if(responseCode == 401){
				System.out.println("Wrong password.");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
}