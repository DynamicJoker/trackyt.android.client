package trackyt.android.client.reponses;


public class AuthenticationResponse extends BaseResponse {
	
	public AuthData data;
	
	public String getApiToken() {
		return data.getApiToken();
	}
}

class AuthData {
	public String apiToken;

	public AuthData() {
		
	}
	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
}