package trackyt.android.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpManager {
	
	private static final String TAG = "HttpManager";
	
	private HttpClient httpClient;
	private HttpResponse httpResponse;
	private HttpEntity httpEntity;
	
	public String request(HttpUriRequest requestType) throws HttpException {
		if (MyConfig.DEBUG) Log.d(TAG, "request()");
		if (requestType == null) {
			throw new IllegalArgumentException("requestType can't be null");
		}
		
		httpClient = new DefaultHttpClient();
		try {
			httpResponse = httpClient.execute(requestType); 
			if (MyConfig.DEBUG) Log.d(TAG, "execute() done");
			if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
				if (MyConfig.DEBUG) Log.d(TAG, "Response is OK");
				httpEntity = httpResponse.getEntity();
			
				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent(); 
					String receivedString = convertStreamToString(instream);
					if (MyConfig.DEBUG) Log.d(TAG, "Received string: " + receivedString);
					return receivedString;
				} else throw new HttpException("httpEntity is null");
			} else throw new HttpException("httpReponse is null OR Status Code != 200");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new HttpException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpException();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	private String convertStreamToString(InputStream instream) throws IOException {
		if (MyConfig.DEBUG) Log.d(TAG, "convertStreamToSting()");
		if (instream == null) {
			throw new IllegalArgumentException("instream can't be null");
		}
		
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(instream));	
		StringBuilder stringBuilder = new StringBuilder();
		String readLine;
	    try {
	    	while ((readLine = buffReader.readLine()) != null) {
				stringBuilder.append(readLine + "\n");
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            instream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new IOException();
	        }
	    }
	    return stringBuilder.toString();
	}
	
}