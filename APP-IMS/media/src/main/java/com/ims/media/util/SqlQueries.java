package com.ims.media.util;

import com.ims.media.ServerVerticle;

public class SqlQueries {
	
	public static String DB_SCHEMA  = ServerVerticle.DB_SCHEMA;
	
	public static String INSERT_MEDIA = "select * from media.insert_media($1)";
	

}
