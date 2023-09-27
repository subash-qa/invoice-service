package com.ims.customer.util;

import com.ims.customer.ServerVerticle;

public class SqlQueries {
	
	public static String DB_SCHEMA  = ServerVerticle.DB_SCHEMA;
	
	public static String INSERT_CUSTOMER = "select * from ims.insert_customer($1)";
	
	public static String GET_CUSTOMER = "select * from ims.get_customer($1)";
	
	public static String UPDATE_CUSTOMER = "select * from ims.update_customer($1)";
	
	public static String DELETE_CUSTOMER = "select * from ims.delete_customer($1)";
	
	

}
