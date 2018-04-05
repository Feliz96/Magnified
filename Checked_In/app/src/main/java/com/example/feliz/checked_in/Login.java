package com.example.feliz.checked_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;



/**
 * Created by Feliz on 2017/08/04.
 */

public class Login extends AppCompatActivity implements View.OnClickListener
{
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog progressDialog;
    @InjectView(R.id.txtUsername) EditText txtusername;
    @InjectView(R.id.txtPassword) EditText txtpassword;
    @InjectView(R.id.btn_login) Button btnLogin;
    @InjectView(R.id.link_signup) TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);

      if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MapsActivity.class));
            return;
        }

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(this);

    }
    private void userLogin()
    {
       // validate();
        final String username = txtusername.getText().toString().trim();
        final String password = txtpassword.getText().toString().trim();

       // progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Authenticating...");
        final ProgressDialog progressDialog = new ProgressDialog(Login.this,R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("id"),
                                                obj.getString("userName"),
                                                obj.getString("email")
                                        );
                                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                finish();

                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName", username);
                params.put("password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
       startActivity(new Intent(getApplicationContext(), MapsActivity.class));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = txtusername.getText().toString();
        String password = txtpassword.getText().toString();

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            txtpassword.setError("between 3 and 10 alphanumeric characters");
            valid = false;
        } else {
            txtpassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogin ){
            userLogin();

        }



    }
}


