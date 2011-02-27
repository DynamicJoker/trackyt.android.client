package trackyt.android.client.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import trackyt.android.client.models.Credentials;
import trackyt.android.client.models.Task;
import trackyt.android.client.reponses.AuthResponse;
import android.util.Log;

public class RequestMaker {
	
	private static final RequestMaker REQUEST_MAKER = new RequestMaker(); 
	
	private HttpManager httpManager;
	private List<NameValuePair> params;
	private Converter converter;
	private AuthResponse auth;
	private UrlComposer urlComposer;
	
	private RequestMaker() {
		converter = new Converter();
		httpManager = new HttpManager();
		urlComposer = new UrlComposer();
		if (MyConfig.DEBUG) Log.d("Dev", "RequestMaker created");
	}

	public static RequestMaker getInstance() {
		return REQUEST_MAKER;
	}
	
	public void initAuth(AuthResponse auth) {
		if (auth == null) {
			throw new NullPointerException("auth is null");
		}
		this.auth = auth;
	}

	public AuthResponse login(Credentials credentials) {
		params = new ArrayList<NameValuePair>();
		URI uri = urlComposer.composeUrl(MyConfig.POST_AUTH_URL);
		HttpPost httpPost = new HttpPost(uri);
		
		params.add(new BasicNameValuePair("email", credentials.getEmail()));
		params.add(new BasicNameValuePair("password", credentials.getPassword()));
		
		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			httpPost.setEntity(ent);
			if (MyConfig.DEBUG) Log.d("Dev", "Entity has been set with email and password from credentials object");
			if (MyConfig.DEBUG) Log.d("Dev", "httpPost's entity is " + httpPost.getEntity().toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    
		JSONObject receivedJSON = httpManager.request(httpPost);
		return converter.toAuthResponse(receivedJSON);
	}
	
	public ArrayList<Task> getTasks() {
		if (auth == null) {
			throw new NullPointerException("auth is null");
		}
		
		Converter converter = new Converter();

		URI uri = urlComposer.composeUrl(MyConfig.GET_TASKS_URL, auth.getToken());
		HttpGet httpGet = new HttpGet(uri);
		
		JSONObject receivedJSON = httpManager.request(httpGet);
		return converter.jsonToTasks(receivedJSON);
	}
	
	public boolean addTask(Task task) {
		if (auth == null) {
			throw new NullPointerException();
		}
		
		params = new ArrayList<NameValuePair>();
		URI uri = urlComposer.composeUrl(MyConfig.POST_ADD_TASK_URL, auth.getToken()); 
		HttpPost httpPost = new HttpPost(uri);
		
		params.add(new BasicNameValuePair("description", task.getDescription()));
		
		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			httpPost.setEntity(ent);
			if (MyConfig.DEBUG) Log.d("Dev", "Entity has been set with task description");
			if (MyConfig.DEBUG) Log.d("Dev", "httpPost's entity is " + httpPost.getEntity().toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	    
		JSONObject receivedJSON = httpManager.request(httpPost);
		
		if (receivedJSON == null) {
			return false;
		} 
		
		return true;
	}
	
	public boolean deleteTask(Task task) {
		if (auth == null) {
			throw new NullPointerException();
		}
		
		URI uri = urlComposer.composeUrl(MyConfig.DELETE_TASK_URL, auth.getToken()); 
		String urlToSend = MyConfig.WEB_SERVER + uri.getPath() + task.getId();
		HttpDelete httpDelete = new HttpDelete(urlToSend);
		
		JSONObject temp = httpManager.request(httpDelete);
		
		if (temp == null)  
			return false; 
		
		return true; 
	}
	
	public boolean startTask(Task task) {
		if (auth == null) {
			throw new NullPointerException();
		}
		
		Log.d("Dev", "startTask() invoked");
		
		URI uri = urlComposer.composeUrl(MyConfig.PUT_START_TASK_URL, auth.getToken()); 
		String urlToSend = MyConfig.WEB_SERVER + uri.getPath() + task.getId();
		Log.d("Dev", "Final URL to use: " + urlToSend);
		HttpPut httpPut = new HttpPut(urlToSend);
		
		JSONObject temp = httpManager.request(httpPut);
		
		if (temp == null)  
			return false; 
		
		return true; 
		
		// TODO: A lot of double code among methods, encapsulation to be considered.
	}
	
	public boolean stopTask(Task task) {
		if (auth == null) {
			throw new NullPointerException();
		}
		
		Log.d("Dev", "stopTask() invoked");
		
		URI uri = urlComposer.composeUrl(MyConfig.PUT_STOP_TASK_URL, auth.getToken()); 
		String urlToSend = MyConfig.WEB_SERVER + uri.getPath() + task.getId();
		Log.d("Dev", "Final URL to use: " + urlToSend);
		HttpPut httpPut = new HttpPut(urlToSend);
		
		JSONObject temp = httpManager.request(httpPut);
		
		if (temp == null)  
			return false; 
		
		return true; 
	}
    
	public boolean startAllTasks() {
		if (auth == null) {
			throw new NullPointerException();
		}
		
		Log.d("Dev", "startAllTask() invoked");
		
		URI uri = urlComposer.composeUrl(MyConfig.PUT_START_ALL_TASK_URL, auth.getToken()); 
		HttpPut httpPut = new HttpPut(uri);
		
		JSONObject temp = httpManager.request(httpPut);
		
		if (temp == null)  
			return false; 
		
		return true;
	}
	
	public boolean stopAllTasks() {
		if (auth == null) {
			throw new NullPointerException();
		}
		
		Log.d("Dev", "stopAllTask() invoked");
		
		URI uri = urlComposer.composeUrl(MyConfig.PUT_STOP_ALL_TASK_URL, auth.getToken()); 
		HttpPut httpPut = new HttpPut(uri);
		
		JSONObject temp = httpManager.request(httpPut);
		
		if (temp == null)  
			return false; 
		
		return true;
	}
}