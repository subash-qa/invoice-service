package com.ims.company.util;

import com.ims.company.ServerVerticle;

public class SqlQueries {
	
	public static String DB_SCHEMA  = ServerVerticle.DB_SCHEMA;
	
	public static String INSERT_COMPANY = "select * from ims.insert_company($1)";
	
	public static String GET_COMPANY = "select * from ims.get_company($1)";
	
	public static String UPDATE_COMPANY = "select * from ims.update_company($1)";
	
	public static String DELETE_COMPANY = "select * from ims.delete_company($1)";
	
	

}
