package com.ims.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Future;

public class EmailTemplate {

	
	@SuppressWarnings("unused")
	public static void createEmailTemplate(JsonObject data ,Handler<AsyncResult<JsonObject>> handler) throws Exception {
		System.out.println("create eMail Template Started");
		
		String Template1 = "";
		String Template2 = "";
		String link = "";
		String logo = "";
		String address = "";
		JsonObject templateData = new JsonObject();
		
		if (data.getString("applicationStatusCd") != null && data.getString("applicationStatusCd").equals("Schedule")) {

			try {
			
				if(data.getInteger("scheduleTypeCd") != null && data.getInteger("scheduleTypeCd") != 78) {
				
				String date = convert(data.getString("interviewDate"));
				System.out.println(date);

				if (data.getString("url") != null && !data.getString("url").equals("")) {
					link = "<span> Meeting Link: " + data.getString("url") + "</span>";				
				}else {
					
					if(data.getString("address") != null && !data.getString("address").equals("")) {
						address = "<span> Address <br>" + data.getString("address") + "</span>";		
					}else {	
						String add = data.getJsonObject("email").getJsonObject("companyAddress").getString("addressLine1").concat(" " + data.getJsonObject("email").getJsonObject("companyAddress").getString("addressLine2"));
						
						address = "<span> Address: <br>" + add + "<br>"
								+  data.getJsonObject("email").getJsonObject("companyAddress").getString("city") + "<br> "
								+  data.getJsonObject("email").getJsonObject("companyAddress").getString("state") + " <br> "
							    +  data.getJsonObject("email").getJsonObject("companyAddress").getInteger("zipCode") + "</span>";	
					}

				}
				Template1 = "<div style='margin: 10% 10%;'>"
						+ "<div style='width:80%;margin: auto;'>"
						+ "<p style='text-align:left;'> Hi "
						+ data.getJsonObject("email")
								.getString("candidateName")
						+ "</p><br>" + "<h3 style='text-align:left;'>Confirmation from "
						+ data.getJsonObject("email")
								.getString("companyName")
						+ "</h3>"
						+ "<p style='text-align: justify;'>We appreciate your interest in our application and we thank you very much for the invitation to interview for the position of"
						+ " <span>"
						+ data.getJsonObject("email")
								.getString("appliedJobTitle")
						+ "</span> " + "at " + "<span>"
						+ data.getJsonObject("email")
								.getString("companyName")
						+ "</span>."
						+ "we confirm that via this mail and get ready for the interview, and we look forward to meeting you.</p><br>"
						+ " <span> Date :" + date + " </span> <br>" + link + "<br>"
						+  address  + " "
//				+ result.getJsonObject("data").getString("emailOtp")
						+ "<br>" + "<br>" + "<div>Best Regards,</div>" + "<div><span>"
						+ data.getJsonObject("email")
								.getString("companyName")
						+ "</span> </div>" + "</div>" + "</div>" + "</div>";
				
				} 
				if(data.getJsonArray("emailList") != null && data.getJsonArray("emailList").size() > 0) {
					
					 Template2 = "<div style='margin: 10% 10%;'>" 	 
						+ "<p style='text-align:left;'> Hi Team</p><br>" 
						+ "<p style='text-align: justify;'>We appreciate your interest in our application and we thank you very much for the invitation to interview for the position of </p>"
						+ "</div>";
					
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else if (data.getString("applicationStatusCd").equals("Selected")) {
			
			try {

//			  if(message.body().getJsonObject("data").getJsonObject("email").getString("companyLogo") != null && !message.body().getJsonObject("data").getJsonObject("email").getString("companyLogo").equals("")) {
//				  logo = "src=" +   message.body().getJsonObject("data").getJsonObject("email").getString("companyLogo") + " ";
//			  }

//			Format f = new SimpleDateFormat("MM/dd/yy");
//			String todayDate = f.format(new Date()); 

				Template1 = data.getString("template");

//			Template = "<div style='padding:7%'>\n" + 
//					"  <h3 style='text-align:center'>Job Offer Letter</h3>\n" + 
//					"  <div style='display: flex;'>" + 
//					"    <div style='width:100%'><span>" + todayDate + "</span><br>\n" + 
//					"      <span style='text-transform: capitalize;'>" + message.body().getJsonObject("data").getJsonObject("email").getString("candidateName") + "</span><br>\n" + 
//					"      <span>" + message.body().getJsonObject("data").getJsonObject("email").getString("candidateAddress") +"</span><br>\n" + 
//					"      <span>City, State, Zip</span><br>" + 
//					"    </div>" + 
//					"     <div>" +
//					"      <img style='float:right'" + 
//					"     " + logo + " " +
//					"        width='20%' height='100%'>\n" + 
//					"    </div>\n" + 
//					"  </div><br><br>\n" + 
//					"  <p>Hi " + message.body().getJsonObject("data").getJsonObject("email").getString("candidateName") + ",</p>\n" + 
//					"  <p style='text-align: justify;'>" + 
//					"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;We are pleased to offer you the position of" + 
//					"  </span> " + message.body().getJsonObject("data").getJsonObject("email").getString("appliedJobTitle") + "</span>" + " at " + "<span>" + message.body().getJsonObject("data").getJsonObject("email").getString("companyName") + "</span>" + " . You will be reporting directly to " + "<span>" + message.body().getJsonObject("data").getJsonObject("email").getString("companyName") + "</span>"  + ". We believe" + 
//					"    your skills and experience are an excellent match for our company.</p>" + 
//					"  <p>\n" + 
//					"    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
//					"    \n" + 
//					"    The annual starting salary for this position is " + message.body().getJsonObject("data").getJsonObject("email").getString("salaryRange") + " to be paid on a [monthly, semi-monthly, weekly," + 
//					"    etc.] basis by [direct deposit, check, etc.], starting on [first pay period]. In addition to this starting salary,\n" + 
//					"    we’re offering you [discuss stock options, bonuses, commission structures, etc. — if applicable]." + 
//					"\n" + 
//					"    Your employment with " + message.body().getJsonObject("data").getJsonObject("email").getString("companyName") + " will be on an at-will basis, which means you and the company are free to\n" + 
//					"    terminate the employment relationship at any time for any reason. This letter is not a contract or guarantee of" + 
//					"    employment for a definitive period of time." + 
//					"\n" + 
//					"    As an employee of " + message.body().getJsonObject("data").getJsonObject("email").getString("companyName") + ", you are also eligible for our benefits program, which includes [medical insurance,\n" + 
//					"    401(k), vacation time, etc.], and other benefits which will be described in more detail in the [employee handbook," + 
//					"    orientation package, etc.]." + 
//					"\n" + 
//					"    Please confirm your acceptance of this offer by signing and returning this letter by [offer expiration date]." + 
//					"\n" + 
//					"    We are excited to have you join our team! If you have any questions, please feel free to reach out at any time." + 
//					"  </p><br><br>\n" + 
//					"  <p>\n" + 
//					"    Sincerely,<br>\n" + 
//					"   " + message.body().getJsonObject("data").getJsonObject("email")
//					.getString("companyName") + "\n" + 
//					"  </p>\n" + 
//					"</div>" ;
			
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else if( data.getString("applicationStatusCd").equals("Rejected")) {

			Template1 = "<div style='margin: 10% 10%;'>" + "<p> Hi "
					+ data.getJsonObject("email")
							.getString("candidateName")
					+ ", </p> "
					+ " <p style='text-align: justify;'> Thank you for taking time to apply for the role of "
					+ data.getJsonObject("email")
							.getString("appliedJobTitle")
					+ " with us.\n"
					+ "on this occation we decided to move forward with other application.. </p> "
					+ " <p> New oppurtunities arise every day to help us make real what matters so, please don't let this stop you from applying for a different role. </p> "
					+ " </div>";
		}
		
		templateData.put("candidateTemplate", Template1).put("employerTemplate", Template2);
		handler.handle(Future.succeededFuture(templateData));
			
	}
	
	public static String convert(String dateString) throws ParseException {
		System.out.println("Given date is " + dateString);

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(dateString);

		System.out.println(
				"MM/dd/yyyy formatted date : " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss a").format(date));
		return new SimpleDateFormat("EEE, d MMM yyyy HH:mm a").format(date);
	}
	
}
