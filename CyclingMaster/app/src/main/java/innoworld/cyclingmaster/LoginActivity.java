package innoworld.cyclingmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import innoworld.cyclingmaster.model.LoginReponse;
import innoworld.cyclingmaster.network.NetworkService;
import retrofit.RestAdapter;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    Button registerHere;
    Button signIn;
    TextInputLayout emailLogin;
    TextInputLayout passwordLogin;
    EditText etEmailLogin;
    EditText etPasswordLogin;

    private ProgressDialog progressDialog;
    private SessionManager session;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initializing toolbar
        //Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolBar);
        //initializing views
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Appconfig.URL_LOGIN).build();
        networkService = restAdapter.create(NetworkService.class);


        registerHere = (Button) findViewById(R.id.registerhere_button);
        signIn = (Button) findViewById(R.id.signin_button);
        emailLogin = (TextInputLayout) findViewById(R.id.email_loginlayout);
        passwordLogin = (TextInputLayout) findViewById(R.id.password_loginlayout);
        etEmailLogin = (EditText) findViewById(R.id.email_login);
        etPasswordLogin = (EditText) findViewById(R.id.password_login);
        //setting onclick listeners
        registerHere.setOnClickListener(this);
        signIn.setOnClickListener(this);

        //setting progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        session = new SessionManager(getApplicationContext());


        //If the session is logged in move to MainActivity
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * function to verify login details
     */
    private class AsyncTask extends android.os.AsyncTask{

        private String email;

        private String password;

        public AsyncTask(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            Object o = networkService.loginReponses("login", email, password);
            LoginReponse loginReponse = (LoginReponse)o;
            return loginReponse;
        }

        @Override
        protected void onPostExecute(Object o) {

        }
    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        new AsyncTask(email,password).execute();

    }



    /*
    function to show dialog
     */
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /*
    function to hide dialog
     */
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //on clicking register button move to Register Activity
            case R.id.registerhere_button:
                Intent intent = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

            //on clicking the signin button check for the empty field then call the checkLogin() function
            case R.id.signin_button:
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                // Check for empty data
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // show snackbar to enter credentials
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}