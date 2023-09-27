package com.ims.util;

import com.ims.portal.ServerVerticle;

public class SqlQueries {

	public static String DB_SCHEMA = ServerVerticle.DB_SCHEMA;
	
	public static String VERIFY_USER = "select * from portal.verify_user($1)";
	
	public static String SIGNIN_USER = "select * from portal.signin_user($1)";
	
	public static String GET_ENUM_NAMES = "select * from portal.get_enum_names($1)";
	
	public static String LOGOUT = "select * from portal.logout($1)";
	
	public static String UPDATE_PASSWORD = "select * from portal.update_password($1)";
	
//	public static String OTP_VERIFY = "select * from portal.otp_verify($1)";
	
	
//	public static String RESEND_OTP = "select * from portal.resend_otp($1)";
	
   

}
