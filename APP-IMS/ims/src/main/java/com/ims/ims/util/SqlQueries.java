package com.ims.ims.util;

import com.ims.ims.ServerVerticle;

public class SqlQueries {
	
	public static String DB_SCHEMA  = ServerVerticle.DB_SCHEMA;
	
	public static String INSERT_INVOICE = "select * from ims.insert_invoice($1)";
	
	public static String GET_INVOICE = "select * from ims.get_invoice($1)";
	
	 
	

}
