package trackyt.android.client.models;

import java.io.Serializable;

// Make it serializable to pass from Login to TasksBoard activity
public class AuthResponse implements Serializable {
	private String token;
	private boolean login;
	
	public AuthResponse(String token, boolean login) {
		this.token = token;
		this.login = login;
	}
	
	public String getToken() {
		return token;
	}
	
	public boolean getLogin() {
		return login;
	}
}
