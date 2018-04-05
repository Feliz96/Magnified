package com.example.feliz.magnified_pictures;

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


/**
 * Created by Feliz on 2018/01/06.
 */

public class Login extends AppCompatActivity implements View.OnClickListener
{
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog progressDialog;
    private Button buy,rsvp;
    private TextView txtUsername,signupLink;
    private Button btnLogin;
    private EditText txtusername,txtpassword;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtusername = (EditText)  findViewById( R.id.txtUsername);
        txtpassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin =(Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }


    @Override
    public void onClick(View view) {

        if(view == btnLogin ){
            userLogin();

        }


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
                                                obj.getString("name"),
                                                obj.getString("email")
                                        );
              //                  startActivity(new Intent(getApplicationContext(), Book.class));
                             //   finish();

                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch
                                (JSONException e) {
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
                params.put("name", username);
                params.put("password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        startActivity(new Intent(getApplicationContext(), Book.class));
        finish();

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


    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnLogin.setEnabled(true);
    }


}



