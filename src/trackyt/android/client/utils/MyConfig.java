package trackyt.android.client.utils;

public class MyConfig {
	
	public static final boolean DEBUG = true;
	
	public static final String WEB_SERVER = "http://trackyt.net";
	public static final String POST_AUTH_URL = "/api/v1.1/authenticate/";
	public static final String GET_TASKS_URL = "/api/v1.1/<token>/tasks/all/";
	public static final String POST_ADD_TASK_URL = "/api/v1.1/<token>/tasks/add/";
	public static final String DELETE_TASK_URL = "/api/v1.1/<token>/tasks/delete/";
	public static final String PUT_START_TASK_URL = "/api/v1.1/<token>/start/";
	public static final String PUT_STOP_TASK_URL = "/api/v1.1/<token>/stop/";
	public static final String PUT_START_ALL_TASK_URL = "/api/v1.1/<token>/start/all/";
	public static final String PUT_STOP_ALL_TASK_URL = "/api/v1.1/<token>/stop/all/";
}
