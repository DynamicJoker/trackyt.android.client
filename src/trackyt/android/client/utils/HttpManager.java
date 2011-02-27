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

public class HttpManager {
	
	private HttpClient httpClient;
	private HttpResponse httpResponse;
	private HttpEntity httpEntity;
	
	public String request(HttpUriRequest requestType) throws HttpException {
		if (requestType == null) {
			throw new IllegalArgumentException("requestType can't be null");
		}
		
		httpClient = new DefaultHttpClient();
		try {
			httpResponse = httpClient.execute(requestType); 
	
			if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
				httpEntity = httpResponse.getEntity();
			
				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent(); 
					String receivedString = convertStreamToString(instream);
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