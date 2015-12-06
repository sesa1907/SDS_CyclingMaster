package innoworld.cyclingmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sesar on 18/11/2015.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin;

    private TextInputLayout tUsername;
    private TextInputLayout tFirstname;
    private TextInputLayout tLastname;
    private TextInputLayout tEmail;
    private TextInputLayout tPassword;

    private EditText userName;
    private EditText firstName;
    private EditText lastName;
    private EditText emailRegister;
    private EditText passwordRegister;

    private Button registerButton;

    SessionManager session;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing Views
        registerButton = (Button) findViewById(R.id.doregister_button);

        tUsername = (TextInputLayout) findViewById(R.id.tUsername);
        tFirstname = (TextInputLayout) findViewById(R.id.tFirstname);
        tLastname = (TextInputLayout) findViewById(R.id.tLastname);
        tEmail = (TextInputLayout) findViewById(R.id.tEmail);
        tPassword = (TextInputLayout) findViewById(R.id.tPassword);

        userName = (EditText) findViewById(R.id.txtUsername);
        firstName = (EditText) findViewById(R.id.txtfirstname);
        lastName = (EditText) findViewById(R.id.txtlastname);
        emailRegister = (EditText) findViewById(R.id.txtemail);
        passwordRegister = (EditText) findViewById(R.id.txtPassword);

        tvLogin = (TextView) findViewById(R.id.tv_signin);

        //setting toolbar
        //Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolBar);

        tvLogin.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in
        if (session.isLoggedIn()) {
            // User is already logged in. Move to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    /*
    function to register user details in mysql database
     */
    private void registerUser(final String username, final String firstname, final String lastname, final String email,
                              final String password) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Appconfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("error");
//                    Log.d("isierur","isinya "+error);
//                    Log.d("json",jObj.toString());
                    if (error==0) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String username = user.getString("username");
                        //String firstname = user.getString("firstname");
                        //String lastname = user.getString("lastname");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        //writing the value to sharedpreference which we will be showing in main screen
                        AppController.setString(RegisterActivity.this, "uid", uid);
                        AppController.setString(RegisterActivity.this, "username", username);
                        // AppController.setString(RegisterActivity.this, "firstname", firstname);
                        //AppController.setString(RegisterActivity.this, "lastname", lastname);
                        AppController.setString(RegisterActivity.this, "email", email);
                        AppController.setString(RegisterActivity.this, "created_at", created_at);

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg+error, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("username", username);
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("password", password);
                params.put("email", email);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signin:
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(intent);
                finish();
            case R.id.doregister_button:
                String username = userName.getText().toString();
                String firstname = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String email = emailRegister.getText().toString();
                String password = passwordRegister.getText().toString();

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(username, firstname, lastname, email, password);
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}