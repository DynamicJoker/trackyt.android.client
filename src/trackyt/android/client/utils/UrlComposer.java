package trackyt.android.client.utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIUtils;

import trackyt.android.client.models.ApiToken;

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

	public URI composeUrl(String apiUri, ApiToken token) { 
	    	URI uri = null;
	    	try {
	    		
	//    		if(apiUri.equals("POST_AUTH_URL")) {
	//    			uri = URIUtils.createURI(null, MyConfig.WEB_SERVER, -1, apiUri, null, null);
	//    		}
	    		
	    		String tmp = apiUri.toString();
				String[] array = tmp.split("<token>");
				tmp = array[0] + token.getToken() + array[1];
				
				uri = URIUtils.createURI(null, MyConfig.WEB_SERVER, -1, tmp, null, null);
	    		
				return uri;	
			} catch (URISyntaxException e) {
				// TODO: do something
				e.printStackTrace();
			}
	    	return null;
	    }

}
