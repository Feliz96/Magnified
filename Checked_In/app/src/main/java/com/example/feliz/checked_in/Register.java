package com.example.feliz.checked_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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



public class Register extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener
{
    private static final int REQUEST_LOGIN =0;
    private static final String TAG = "Register";
    private String Gender;
    private RadioButton radioButton;
    @InjectView(R.id.txtUsername) EditText txtname;
    @InjectView(R.id.txtPassword) EditText txtpassword;
    @InjectView(R.id.txtVerify) EditText txtverify;
    @InjectView(R.id.txtEmail) EditText txtemail;
    @InjectView(R.id.btnRegister) Button btnRegister;
    @InjectView(R.id.link_login) TextView loginLink;
    @InjectView(R.id.txtFirst) EditText txtfirst;
    @InjectView(R.id.txtLast) EditText txtlast;
    @InjectView(R.id.txtAge) EditText txtage;
    @InjectView(R.id.txtPhone) EditText txtphone;



    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.inject(this);



        progressDialog = new ProgressDialog(this);

       btnRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               registerUser();

           }
       });


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
              //  finish();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser()
    {

        final String username = txtname.getText().toString().trim();
        final String email = txtemail.getText().toString().trim();
        final String password = txtpassword.getText().toString().trim();
        final String age = txtage.getText().toString().trim();
        final String phone = txtphone.getText().toString().trim();
        final String verify = txtverify.getText().toString().trim();
        final String gender = String.valueOf(1);
        final String firstname = txtfirst.getText().toString().trim();
        final String lastname = txtlast.getText().toString().trim();
        final String avatar = "default.png";

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName", username);
                params.put("email", email);
                params.put("password", password);
                params.put("firstName", firstname);
                params.put("lastName", lastname);
                params.put("phoneNumber",phone);
                params.put("age", age);
                params.put("gender",gender);
                params.put("avater", avatar);
                params.get(username);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }
    public void signup()
    {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnRegister.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Register.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = txtname.getText().toString();
        String email = txtemail.getText().toString();
        String password = txtpassword.getText().toString();


//Login
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }


    public void onSignupSuccess() {
        btnRegister.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnRegister.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        final String username = txtname.getText().toString().trim();
        final String email = txtemail.getText().toString().trim();
        final String password = txtpassword.getText().toString().trim();


        if (username.isEmpty() || username.length() < 3) {
          txtname.setError("at least 3 characters");
            valid = false;
        } else {
            txtname.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtemail.setError("enter a valid email address");
            valid = false;
        } else {
            txtemail.setError(null);
        }

        if(!password.toString().equals(txtverify.toString()))
        {
            txtverify.setError("Password must match password");
            valid =false;
        }
        else
        {
            txtverify.setError(null);
        }


        if (txtphone.length() > 10 && txtphone.length() <10)
        {
            txtphone.setError("Must be 10 digits");
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
           txtpassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            txtpassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gen = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
