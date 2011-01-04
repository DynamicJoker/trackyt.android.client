package trackyt.android.client;

import org.json.JSONObject;

import trackyt.android.client.models.AuthResponse;
import trackyt.android.client.models.Credentials;
import trackyt.android.client.utils.HttpManager;
import trackyt.android.client.utils.MyConfig;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	HttpManager httpManager;
	Credentials credentials;
	AuthResponse authResponse;

	EditText loginEditText;
	EditText passwordEditText;
	Button loginButton;
	Button createAccutonButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		httpManager = HttpManager.getInstance();
		credentials = new Credentials();
		
		loginEditText = (EditText) findViewById(R.id.login_edit_text);
		passwordEditText = (EditText) findViewById(R.id.password_edit_text);
		loginButton = (Button) findViewById(R.id.login_button);
		createAccutonButton = (Button) findViewById(R.id.create_account_button);
		
	}

	public void loginOnClick(View view) {
		if (doLogin()) {
			openTasksBoardActivity();
		} else {
			Toast.makeText(this, "Loging wasn't successfull, try again", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void createAccountOnClick(View view) {
		Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
		// TODO: implement createAccountOnClick()
	}

	private boolean doLogin() {
		if (updateCredentials() == false) {
			return false;
		}

		JSONObject json = httpManager.login(credentials);

		if (initAuthResponseObject(json)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean initAuthResponseObject(JSONObject json) {
		String token = json.optString("token");
		boolean login = json.optBoolean("success");
		authResponse = new AuthResponse(token, login);

		if (authResponse.getLogin()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean updateCredentials() {
		if (MyConfig.DEBUG)
			Log.d("Dev", "updateCredentials() invoked");
		if (loginEditText.getText().toString().equals("")
				&& passwordEditText.getText().toString().equals("")) {
			return false;
		}

		credentials.setEmail(loginEditText.getText().toString());
		credentials.setPassword(passwordEditText.getText().toString());

		return true;
	}

	private void openTasksBoardActivity() {
		 Intent myIntent = new Intent(Login.this, TasksBoard.class);
		 Login.this.startActivity(myIntent);
	}
}
