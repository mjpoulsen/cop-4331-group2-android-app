package cop_4331_group2.dms_android_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import cop_4331_group2.dms_android_app.utilities.NetworkUtils;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = "LoginActivity";
  private static final int REQUEST_SIGNUP = 0;
  private static int LOGIN_STATUS = 0;
  private static AsyncTask<String, Void, String> loginTask;

  private static final int VALID_ACCOUNT = 200;

  // ERROR CODES:
  // TODO document error codes.
  private static final int INVALID_CREDS = -1;

  @BindView(R.id.input_email) EditText _emailText;
  @BindView(R.id.input_password) EditText _passwordText;
  @BindView(R.id.btn_login) Button _loginButton;
  @BindView(R.id.link_signup) TextView _signupLink;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    init();
  }

  public void init() {
    _loginButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        Log.e(TAG, "Clicked log in");
        int status = login();

        // TODO create better error codes.
        if (LOGIN_STATUS == INVALID_CREDS) {
          Log.e(TAG, "Status: " + LOGIN_STATUS);
          return;
        }

        if (LOGIN_STATUS == VALID_ACCOUNT) {
          Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
          startActivity(intent);
        } else {
          Log.e(TAG, "LOGIN_STATUS: " + LOGIN_STATUS + " and login() status: " + status);
        }

        // TODO is this necessary?
        if (!loginTask.isCancelled()) {
          loginTask.cancel(true);
        }

      }
    });

    _signupLink.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // Start the Sign-up activity
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
      }
    });
  }

  public int login() {
    if (!validate()) {
      onLoginFailed();
      return INVALID_CREDS;
    }

    _loginButton.setEnabled(false);

    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
        R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Authenticating...");
    progressDialog.show();

    // TODO: Implement your own authentication logic here.
    int status = 0;
    String[] user_details = new String[2];
    user_details[0] = _emailText.getText().toString();
    user_details[1] = _passwordText.getText().toString();
    Log.e(TAG, "Email: " + user_details[0] + " Password: " + user_details[1]);

    loginTask = new LoginTask();
    try {
      // Gets async response. No need to create busy-wait!
      String res = loginTask.execute(user_details).get();
      status = Integer.parseInt(res);
    } catch (InterruptedException e) {
      Log.e(TAG, e.toString());
    } catch (ExecutionException e) {
      Log.e(TAG, e.toString());
    }

    new android.os.Handler().postDelayed(
        new Runnable() {
          public void run() {
            // On complete call either onLoginSuccess or onLoginFailed
            if (LOGIN_STATUS == 200) {
              onLoginSuccess();
            } else {
              onLoginFailed();
            }
            progressDialog.dismiss();
          }
        }, 3000);

    return status;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_SIGNUP) {
      if (resultCode == RESULT_OK) {

        // TODO: Implement successful signup logic here
        // By default we just finish the Activity and log them in automatically
        this.finish();
      }
    }
  }

  @Override
  public void onBackPressed() {
    // disable going back to the MainActivity
    moveTaskToBack(true);
  }

  public void onLoginSuccess() {
    _loginButton.setEnabled(true);
    // finish() will prevent LoginActivity from being usable.
    finish();
  }

  public void onLoginFailed() {
    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

    _loginButton.setEnabled(true);
  }

  public boolean validate() {
    boolean valid = true;

    String email = _emailText.getText().toString();
    String password = _passwordText.getText().toString();

    // TODO decide if users should log in with email or username
    if (email.isEmpty() /*|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()*/) {
      _emailText.setError("enter a valid email address");
      valid = false;
    } else {
      _emailText.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      _passwordText.setError("between 4 and 10 alphanumeric characters");
      valid = false;
    } else {
      _passwordText.setError(null);
    }

    return valid;
  }

  public class LoginTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
      String email = params[0];
      String password = params[1];

      try {
        LOGIN_STATUS = NetworkUtils.postSignin(email, password);
      } catch (IOException e) {
        Log.e(TAG, e.toString());
      }
      return String.valueOf(LOGIN_STATUS);
    }

    @Override
    protected void onPostExecute(String response) {
      if (response != null && !response.equals("")) {
        Log.d(TAG, "POST_Response: " + response);
      }
    }
  }
}