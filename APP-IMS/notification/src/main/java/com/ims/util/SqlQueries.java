package com.ims.util;

public class SqlQueries {

	
	public static String GET_NOTIFICATION_COUNT = "select * from notification.get_notification_count($1)";

	public static String GET_ALL_NOTIFICATION = "select * from notification.get_all_notification($1)";

	public static String UPDATE_NOTIFICATION_STATUS = "select * from notification.update_notification_status($1)";

}
