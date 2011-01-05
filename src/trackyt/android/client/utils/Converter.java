package trackyt.android.client.utils;

import org.json.JSONException;
import org.json.JSONObject;

import trackyt.android.client.models.AuthResponse;

public class Converter {
	public AuthResponse convertToAuthResponse(JSONObject json) {
		AuthResponse authResponse;
		String token = null;
		if (json == null) return null;

		boolean login = json.optBoolean("success");
		
		try {
			JSONObject tmp = json.getJSONObject("data");
			token = tmp.optString("apiToken");
			authResponse = new AuthResponse(token, login);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		if (authResponse.getLogin() && (authResponse.getToken() != null)) {
			return authResponse;
		} else {
			return null;
		}		
	}
}
