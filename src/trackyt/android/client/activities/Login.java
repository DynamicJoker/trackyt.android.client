package trackyt.android.client.activities;

import trackyt.android.client.R;
import trackyt.android.client.TrackytApiAdapter;
import trackyt.android.client.TrackytApiAdapterFactory;
import trackyt.android.client.exceptions.NotAuthenticatedException;
import trackyt.android.client.models.ApiToken;
import trackyt.android.client.reponses.AuthenticationResponse;
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
	AuthenticationResponse authResponse;

	EditText loginEditText;
	EditText passwordEditText;
	Button loginButton;
	Button createAccountButton;
	
	ProgressDialog progressDialog;
	Handler mHandler;
	
	TrackytApiAdapter mAdapter;
	ApiToken token;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		requestMaker = RequestMaker.getInstance();
		loginEditText = (EditText) findViewById(R.id.login_edit_text);
		passwordEditText = (EditText) findViewById(R.id.password_edit_text);
		loginButton = (Button) findViewById(R.id.login_button);
		createAccountButton = (Button) findViewById(R.id.create_account_button);
		mAdapter = new TrackytApiAdapterFactory().createV11Adapter();

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

	private void doLogin() {
		try {
			token = mAdapter.authenticate("ebeletskiy@gmail.com", "mikusya");
		} catch (NotAuthenticatedException e) {
			// TODO: Show toast here
		}
	}

	private void openTasksBoardActivity() {
		progressDialog.dismiss();
		Intent intent = new Intent(Login.this, TasksBoard.class);
		intent.putExtra("token", token.getToken());
		Login.this.startActivity(intent);
	}
	
	class LoginJob implements Runnable {

		public void run() {
			doLogin();
			openTasksBoardActivity();
			}
	}
	
	private void updateGUI() {
		Toast.makeText(this, "Login wasn't successful", Toast.LENGTH_SHORT).show();
		progressDialog.dismiss();
	}
}
