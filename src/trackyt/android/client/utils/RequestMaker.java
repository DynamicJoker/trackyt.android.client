package trackyt.android.client.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import trackyt.android.client.models.ApiToken;
import android.util.Log;

public class RequestMaker {
	
	private static final RequestMaker REQUEST_MAKER = new RequestMaker(); 
	private static final String TAG = "RequestMaker";
	
	private HttpManager httpManager;
	private List<NameValuePair> params;
	private UrlComposer urlComposer;
	
	private RequestMaker() {
		if (MyConfig.DEBUG) Log.d(TAG, "Instance created");
		httpManager = new HttpManager();
		urlComposer = new UrlComposer();
	}

	public static RequestMaker getInstance() {
		return REQUEST_MAKER;
	}
	
	public String authenticate(String email, String password) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "authenticate()");
		params = new ArrayList<NameValuePair>();
		URI uri = urlComposer.composeUrl(MyConfig.POST_AUTH_URL);
		HttpPost httpPost = new HttpPost(uri);
		
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		
		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			httpPost.setEntity(ent);
		} catch (UnsupportedEncodingException e) {
			// TODO: do something about it
			e.printStackTrace();
		}
	    
		String receivedString = httpManager.request(httpPost);
		return receivedString;
	}
	
	public String getAllTasks(ApiToken token) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "getAllTasks()");

		URI uri = urlComposer.composeUrl(MyConfig.GET_TASKS_URL, token);
		HttpGet httpGet = new HttpGet(uri);
		
		String receivedString =  httpManager.request(httpGet);
		Log.d("Dev", "Received string from RequestMaker: " + receivedString);
		return receivedString;
	}
	
	public String addTask(ApiToken token, String description) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "addTask()");
		params = new ArrayList<NameValuePair>();
		URI uri = urlComposer.composeUrl(MyConfig.POST_ADD_TASK_URL, token); 
		HttpPost httpPost = new HttpPost(uri);
		
		params.add(new BasicNameValuePair("description", description));
		
		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			httpPost.setEntity(ent);
		} catch (UnsupportedEncodingException e) {
			// TODO: do something
			e.printStackTrace();
		}
	    
		String receivedString = httpManager.request(httpPost);
		return receivedString;
	}
	
	public String deleteTask(ApiToken token, int taskId) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "deleteTask()");
		URI uri = urlComposer.composeUrl(MyConfig.DELETE_TASK_URL, token); 
		String urlToSend = MyConfig.WEB_SERVER + uri.getPath() + taskId;
		HttpDelete httpDelete = new HttpDelete(urlToSend);
		
		String receivedString = httpManager.request(httpDelete);
		return receivedString;
	}
	
	public String startTask(ApiToken token, int taskId) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "startTask()");
		URI uri = urlComposer.composeUrl(MyConfig.PUT_START_TASK_URL, token); 
		String urlToSend = MyConfig.WEB_SERVER + uri.getPath() + taskId;
		HttpPut httpPut = new HttpPut(urlToSend);
		
		String receivedString = httpManager.request(httpPut);
		return receivedString;
	}
	
	public String stopTask(ApiToken token, int taskId) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "stopTask()");
		URI uri = urlComposer.composeUrl(MyConfig.PUT_STOP_TASK_URL, token); 
		String urlToSend = MyConfig.WEB_SERVER + uri.getPath() + taskId;
		HttpPut httpPut = new HttpPut(urlToSend);
		
		String receivedString = httpManager.request(httpPut);
		return receivedString;
	}
    
	public String startAllTasks(ApiToken token) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "startAllTasks()");
		
		URI uri = urlComposer.composeUrl(MyConfig.PUT_START_ALL_TASK_URL, token); 
		HttpPut httpPut = new HttpPut(uri);
		
		String receivedString = httpManager.request(httpPut);
		return receivedString;
	}
	
	public String stopAllTasks(ApiToken token) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "stopAllTasks()");
		
		URI uri = urlComposer.composeUrl(MyConfig.PUT_STOP_ALL_TASK_URL, token); 
		HttpPut httpPut = new HttpPut(uri);
		
		String receivedString = httpManager.request(httpPut);
		return receivedString;
	}
	
	public String doneTask(ApiToken token, int taskId) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "doneTask()");
		
		URI uri = urlComposer.composeUrl(MyConfig.GET_DONE_TASK_URL, token);
		String urlToSend = MyConfig.WEB_SERVER + uri.getPath() + taskId;
		HttpGet httpGet = new HttpGet(urlToSend);
		
		String receivedString = httpManager.request(httpGet);
		Log.d(TAG, "test: receivedString: " + receivedString);
		return receivedString;
		
	}
}