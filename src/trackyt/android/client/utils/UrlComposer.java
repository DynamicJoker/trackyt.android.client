package trackyt.android.client.utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIUtils;

import android.util.Log;

public class UrlComposer {

	public URI composeUrl(String apiUri) {
		URI uri = null;
		try {
			uri = URIUtils.createURI(null, MyConfig.WEB_SERVER, -1, apiUri, null, null);
			return uri;	
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public URI composeUrl(String apiUri, String token) { 
	    	URI uri = null;
	    	try {
	    		
	//    		if(apiUri.equals("POST_AUTH_URL")) {
	//    			uri = URIUtils.createURI(null, MyConfig.WEB_SERVER, -1, apiUri, null, null);
	//    		}
	    		
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
