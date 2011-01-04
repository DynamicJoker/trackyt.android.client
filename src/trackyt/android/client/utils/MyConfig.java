package trackyt.android.client.utils;

public class MyConfig {
	
//	Debug turn on/off
	public static final boolean DEBUG = true;
	
//	APIs 
	public static final String WEB_SERVER = "http://trackyt.net";
	public static final String POST_AUTH_URL = "/api/v1.1/authenticate";
	public static final String GET_TASKS_URL = "/api/v1.1/0fa63a5107942a4045e21272a04b0be3/tasks/all";
	public static final String POST_ADD_TASK_URL = "/api/v1.1/<token>/tasks/add";
	public static final String DELETE_TASK_URL = "/api/v1.1/<token>/delete/15";
	public static final String PUT_START_TASK_URL = "/api/v1.1/<token>/start/14";
	public static final String PUT_STOP_TASK_URL = "/api/v1.1/<token>/stop/15";
	public static final String PUT_START_ALL_TASK_URL = "/api/v1.1/<token>/start/all";
	public static final String PUT_STOP_ALL_TASK_URL = "/api/v1.1/<token>/stop/all";
}
