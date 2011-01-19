package trackyt.android.client.utils;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import trackyt.android.client.models.AuthResponse;
import trackyt.android.client.models.Credentials;
import trackyt.android.client.models.Task;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HttpManager {
	
	private static HttpManager httpManager = new HttpManager(); 
	
	private HttpClient httpClient;
	private HttpResponse httpResponse;
	private HttpEntity httpEntity;
	private List<NameValuePair> params;
	private Converter converter;
	private AuthResponse auth;
	
	private HttpManager() {
		converter = new Converter();
		if (MyConfig.DEBUG) Log.d("Dev", "HttpManager created");
	}

	public static HttpManager getInstance() {
		return httpManager;
	}
	
	public void initAuth(AuthResponse auth) {
		if (auth == null) {
			throw new NullPointerException("auth is null");
		}
		this.auth = auth;
	}

	/* Login */
	public AuthResponse login(Credentials credentials) {
		params = new ArrayList<NameValuePair>();
		URI uri = urlComposer(MyConfig.POST_AUTH_URL);
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
	    
		JSONObject receivedJSON = request(httpPost);
		return converter.toAuthResponse(receivedJSON);
	}
	
	/* Get tasks */
	public ArrayList<Task> getTasks() {
		if (auth == null) {
			throw new NullPointerException("auth is null");
		}
		
		Converter converter = new Converter();

		URI uri = urlComposer(MyConfig.GET_TASKS_URL, auth.getToken());
		HttpGet httpGet = new HttpGet(uri);
		
		JSONObject receivedJSON = request(httpGet);
		return converter.toTasks(receivedJSON);
	}
	
	/* Add task */
	public boolean createTask(Task task) {
		if (auth == null) {
			throw new NullPointerException();
		}
		
		params = new ArrayList<NameValuePair>();
		URI uri = urlComposer(MyConfig.POST_ADD_TASK_URL, auth.getToken()); 
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
	    
		JSONObject receivedJSON = request(httpPost);
		
		if (receivedJSON == null) {
			return false;
		} 
		
		return true;
	}
	
	public boolean deleteTask(Task task) {
		
		return true; 
	}
	
	private JSONObject request(HttpUriRequest requestType) {
		httpClient = new DefaultHttpClient();
		if (MyConfig.DEBUG) Log.d("Dev", "HttpManager's getRequest() invoked");
		try {
			httpResponse = httpClient.execute(requestType); 

			if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
				httpEntity = httpResponse.getEntity();
			
				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent(); 
					String convertedString = convertStreamToString(instream);
					return convertToJSON(convertedString);
				} else return null;
				
			} else return null;
		} catch (ClientProtocolException e) {
			if (MyConfig.DEBUG) Log.d("Dev", "ClientProtocolException in getRequest()");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			if (MyConfig.DEBUG) Log.d("Dev", "IOException in getRequst()");
			e.printStackTrace();
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	/*Reads data from InputStream and put it in String*/
	private String convertStreamToString(InputStream instream) {
		if (MyConfig.DEBUG) Log.d("Dev", "convertStreamToString() invoked");
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(instream));	
		StringBuilder stringBuilder = new StringBuilder();
		String readLine;
        try {
        	while ((readLine = buffReader.readLine()) != null) {
				stringBuilder.append(readLine + "\n");
				if (MyConfig.DEBUG) Log.d("Dev", "Read response " + readLine);
			}
        } catch (IOException e) {
        	if (MyConfig.DEBUG) Log.d("Dev", "converStreamToSting() unsuccessfull");
            e.printStackTrace();
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
            	if (MyConfig.DEBUG) Log.d("Dev", "Stream closure was unsuccessfull");
                e.printStackTrace();
                return null;
            }
        }
        return stringBuilder.toString();
	}
	
	/*Converts String to JSON*/
	private JSONObject convertToJSON(String string) {
		if (MyConfig.DEBUG) Log.d("Dev", "convertToJSON() invoked");
		if (string.equals("")) {
			if (MyConfig.DEBUG) Log.d("Dev", "Sting is null, nothing to be converted");
			return null;
		}
		
		try {
			JSONObject json = new JSONObject(string);
			return json;
		} catch (JSONException e) {
			if (MyConfig.DEBUG) Log.d("Dev", "Converting String to JSON was't successfull"); 
			e.printStackTrace();
			return null;
		}
	}
	
    private URI urlComposer(String apiUri) {
    	URI uri = null;
    	try {
			uri = URIUtils.createURI(null, MyConfig.WEB_SERVER, -1, apiUri, null, null);
			if (MyConfig.DEBUG) Log.d("Dev", "Constructed url " + uri);
			return uri;	
		} catch (URISyntaxException e) {
			if (MyConfig.DEBUG) Log.d("Dev", "urlComposer was unable to construct a URL"); 
			e.printStackTrace();
		}
    	return null;
    }
    
    private URI urlComposer(String apiUri, String token) {
    	URI uri = null;
    	try {
    		String tmp = apiUri.toString();
    		String[] array = tmp.split("<token>");
    		tmp = array[0] + token + array[1];
    		
    		uri = URIUtils.createURI(null, MyConfig.WEB_SERVER, -1, tmp, null, null);
			if (MyConfig.DEBUG) Log.d("Dev", "Constructed url " + uri);
			return uri;	
		} catch (URISyntaxException e) {
			if (MyConfig.DEBUG) Log.d("Dev", "urlComposer was unable to construct a URL"); 
			e.printStackTrace();
		}
    	return null;
    }
	
}