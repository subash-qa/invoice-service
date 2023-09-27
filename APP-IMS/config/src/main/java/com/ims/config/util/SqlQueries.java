package com.ims.config.util;

import com.ims.config.ServerVerticle;

public class SqlQueries {
	
	public static String DB_SCHEMA  = ServerVerticle.DB_SCHEMA;
	
	public static String GET_CONFIG = "select * from "+DB_SCHEMA+".get_config($1)";
	
}
