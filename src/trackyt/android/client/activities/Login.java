package trackyt.android.client.activities;

import trackyt.android.client.R;
import trackyt.android.client.models.AuthResponse;
import trackyt.android.client.models.Credentials;
import trackyt.android.client.utils.RequestMaker;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
	
	ProgressDialog progressDialog;
	Handler mHandler;

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
		mHandler = new Handler();

		loginEditText.setText("ebeletskiy@gmail.com");
		passwordEditText.setText("mikusya");
	}

	public void loginOnClick(View view) {
		Thread mThread = new Thread(new LoginJob());
		progressDialog = ProgressDialog.show(this, "Loging in...", "Please wait a bit", true, false);
		mThread.start();
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
		if (authResponse == null)
			return false;
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
		progressDialog.dismiss();
		Intent intent = new Intent(Login.this, TasksBoard.class);
		intent.putExtra("auth", authResponse);
		Login.this.startActivity(intent);
	}
	
	class LoginJob implements Runnable {

		public void run() {
			if (doLogin()) {
				openTasksBoardActivity();
			} else {
				mHandler.post(new Runnable() {
					public void run() { updateGUI(); }
				});
			}
		}
	}
	
	private void updateGUI() {
		Toast.makeText(this, "Login wasn't successful", Toast.LENGTH_SHORT).show();
		progressDialog.dismiss();
	}
}
