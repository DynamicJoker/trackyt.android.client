package trackyt.android.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpManager {
	
	private HttpClient httpClient;
	private HttpResponse httpResponse;
	private HttpEntity httpEntity;
	
	public JSONObject request(HttpUriRequest requestType) {
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
			Log.d("Dev", "End of httpManager method");
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
	
}
