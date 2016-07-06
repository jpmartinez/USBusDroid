package tecnoinf.proyecto.grupo4.usbusdroid3.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private EditText _nameText;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _signupButton;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        TextView _loginLink = (TextView) findViewById(R.id.link_login);
        assert _nameText != null;
        assert _emailText != null;
        assert _passwordText != null;
        assert _signupButton != null;
        assert _loginLink != null;

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup() throws ExecutionException, InterruptedException, JSONException {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando Cuenta...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        JSONObject credentials = new JSONObject();
        //credentials.put("type", "twitter");
        credentials.put("username", name);
        credentials.put("tenantId", getString(R.string.tenantId));
        credentials.put("password", password);
        credentials.put("email", email);

        String registerClientRest = getString(R.string.URLregister, getString(R.string.URL_REST_API));
        AsyncTask<Void, Void, JSONObject> registerResult = new RestCallAsync(getApplicationContext(), registerClientRest, "POST", credentials, null).execute();
        JSONObject registerData = registerResult.get();

        if(registerData.getString("result").equalsIgnoreCase("OK")) {
            editor.putString("username", name);
            editor.putString("password", password);
            editor.apply();

            startActivity(new Intent(getBaseContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

//
//    public void onSignupSuccess() {
//        _signupButton.setEnabled(true);
//        setResult(RESULT_OK, null);
//        finish();
//    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 5) {
            _nameText.setError("al menos 5 caracteres");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("ingrese un email válido");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 5 || password.length() > 10) {
            _passwordText.setError("entre 5 y 10 caracteres alfanuméricos");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
