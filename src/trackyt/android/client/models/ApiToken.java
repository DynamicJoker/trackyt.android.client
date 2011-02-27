package trackyt.android.client.models;

public class ApiToken {
	
	private String token;
	
	public ApiToken() {
		
	}
	
	public ApiToken(String str) {
		if (str == null) {
			throw new IllegalArgumentException("token can't be empty");
		}
		
		token = str;
	}

	public String getToken() {
		return token;
	}

}
