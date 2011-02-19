package trackyt.android.client;

import trackyt.android.client.models.AuthResponse;
import trackyt.android.client.models.Credentials;
import trackyt.android.client.utils.RequestMaker;
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

	RequestMaker requestMaker;
	Credentials credentials;
	AuthResponse authResponse;

	EditText loginEditText;
	EditText passwordEditText;
	Button loginButton;
	Button createAccountButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		requestMaker = RequestMaker.getInstance();
		credentials = new Credentials();
		loginEditText = (EditText) findViewById(R.id.login_edit_text);
		passwordEditText = (EditText) findViewById(R.id.password_edit_text);
		loginButton = (Button) findViewById(R.id.login_button);
		createAccountButton = (Button) findViewById(R.id.create_account_button);

		loginEditText.setText("ebeletskiy@gmail.com");
		passwordEditText.setText("mikusya");
	}

	public void loginOnClick(View view) {
		if (doLogin()) {
			openTasksBoardActivity();
		} else {
			Toast.makeText(this, "Loging wasn't successfull, try again",
					Toast.LENGTH_SHORT).show();
		}
    }

	public void createAccountOnClick(View view) {
		Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();

		// TODO: implement createAccountOnClick()
	}

	private boolean doLogin() {
		if (!updateCredentials()) {
			return false;
		}

		authResponse = requestMaker.login(credentials);
		return authResponse.getLogin();
	}

	private boolean updateCredentials() {
		if (loginEditText.getText().toString().equals("")
				&& passwordEditText.getText().toString().equals("")) {
			return false;
		}

		credentials.setEmail(loginEditText.getText().toString());
		credentials.setPassword(passwordEditText.getText().toString());

		return true;
	}

	private void openTasksBoardActivity() {
		Intent intent = new Intent(Login.this, TasksBoard.class);
		// Pass authResponse object to TasksBoard activity
		intent.putExtra("auth", authResponse);
		Login.this.startActivity(intent);

	}
}
