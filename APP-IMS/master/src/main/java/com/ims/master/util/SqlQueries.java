package com.ims.master.util;

import com.ims.master.ServerVerticle;

public class SqlQueries {
	
	public static String DB_SCHEMA  = ServerVerticle.DB_SCHEMA;
	
	public static String INSERT_JOB_ROLE = "select * from master.insert_job_role($1)";
	
	public static String GET_JOB_ROLE = "select * from master.get_job_role($1)";
	
	public static String INSERT_DEPARTMENT = "select * from master.insert_department($1)";
	
	public static String GET_DEPARTMENT = "select * from master.get_department($1)";
	
	public static String UPDATE_JOB_ROLE = "select * from master.update_job_role($1)";
	
	public static String UPDATE_DEPARTMENT = "select * from master.update_department($1)";
		
	public static String DELETE_JOB_ROLE = "select * from master.delete_job_role($1)";
		
	public static String DELETE_DEPARTMENT = "select * from master.delete_department($1)";
	
	public static String CREATE_LEAVE_SETTINGS = "select * from master.create_leave_settings($1)";

    public static String GET_LEAVE_SETTINGS = "select * from master.get_leave_settings($1)";

    public static String UPDATE_LEAVE_SETTINGS = "select * from master.update_leave_settings($1)";

    public static String CREATE_LEAVE_DAYS_CONFIG = "select * from master.create_leave_days_config($1)";

    public static String GET_LEAVE_DAYS_CONFIG = "select * from master.get_leave_days_config($1)";

	public static String UPDATE_LEAVE_DAYS_CONFIG = "select * from master.update_leave_days_config($1)";
    
  
	

}
