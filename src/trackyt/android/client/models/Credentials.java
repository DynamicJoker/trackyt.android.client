package trackyt.android.client.models;

import trackyt.android.client.utils.MyConfig;
import android.util.Log;

public class Credentials {
	
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if (email.equals("")) {
			if (MyConfig.DEBUG) Log.d("Credentials", "Empty string for email");
			    throw new IllegalArgumentException();
		}
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if (email.equals("")) {
			if (MyConfig.DEBUG) Log.d("Credentials", "Empty string for password");
			throw new IllegalArgumentException();
		}
		this.password = password;
	}
	
}
