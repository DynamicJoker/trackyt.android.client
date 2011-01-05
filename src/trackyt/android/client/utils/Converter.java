package trackyt.android.client.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import trackyt.android.client.models.AuthResponse;
import trackyt.android.client.models.Task;
import android.util.Log;

public class Converter {
	
	// Converting JSON to authResponse model
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
	
	public ArrayList<Task> convertJsonInTasks(JSONObject json) {
		ArrayList<Task> list = new ArrayList<Task>();
		
		if(MyConfig.DEBUG) {
			Log.d("Dev", "convertJsonInTasks() invoked");
			Log.d("Dev", "received json: " + json.toString());
		}
		
		JSONObject tmp;
		try {
			tmp = json.getJSONObject("data");
			if(MyConfig.DEBUG) Log.d("Dev", "Json object obtained: " + tmp.toString());
			
			JSONArray array = tmp.getJSONArray("tasks");
			if(MyConfig.DEBUG) Log.d("Dev", "Json tasks obtained: " + array.toString());

			for (int i = 0; i < array.length(); i++) {
				Task task = new Task();
				task.setId(((JSONObject)array.get(i)).optInt("id"));
				task.setDescription(((JSONObject)array.get(i)).optString("description"));
				task.setStatus(((JSONObject)array.get(i)).optInt("status"));
				task.setCreatedDate(((JSONObject)array.get(i)).optInt("createdDate"));
				task.setStartedDate(((JSONObject)array.get(i)).optInt("startedDate"));
				task.setStoppedDate(((JSONObject)array.get(i)).optInt("stoppedDate"));
				task.setSpent(((JSONObject)array.get(i)).optInt("spent"));
				
				list.add(task);
			}
		} catch (JSONException e) {
			Log.d("Dev", "Converter class, the convertJsonInTasks()");
			e.printStackTrace();
		}
		
		return list;
	}
}
