package com.example.feliz.magnified_pictures;

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




public class Register extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener
{
    private static final int REQUEST_LOGIN =0;
    private static final String TAG = "Register";

    private EditText name,surname,email,phone,password,verify;
    private TextView link_login;
    private String Username, Usersurname,Useremail,Userpassword,Userphone,Userverify;
     private  Button register;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        progressDialog = new ProgressDialog(this);

        register = (Button)(findViewById(R.id.btnRegister));
        link_login = (TextView)(findViewById(R.id.link_login));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });


        link_login.setOnClickListener(new View.OnClickListener() {
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

        name = (EditText) (findViewById(R.id.txtName));
        surname = (EditText) (findViewById(R.id.txtSurname));
        email = (EditText) (findViewById(R.id.txtEmail));
        password = (EditText) (findViewById(R.id.txtPassword));
        verify = (EditText) (findViewById(R.id.txtVerify));
        phone = (EditText)(findViewById(R.id.txtPhone));

        Username = name.getText().toString().trim();
        Usersurname = surname.getText().toString().trim();
        Useremail = email.getText().toString().trim();
        Userpassword = password.getText().toString().trim();
        Userphone = phone.getText().toString().trim();
        Userverify = verify.getText().toString().trim();

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
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage() +"Connection Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", Username);
                params.put("surname", Usersurname);
                params.put("password", Userpassword);
                params.put("email",Useremail);
                params.put("phone",Userphone);
                params.get(Username);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
    public void signup()
    {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        register.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Register.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


//Login
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }


    public void onSignupSuccess() {
        register.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        register.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        final String username = name.getText().toString().trim();
        final String Useremail = email.getText().toString().trim();
        final String Userpassword = password.getText().toString().trim();


        if (username.isEmpty() || username.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (Useremail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(Useremail).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if(!password.toString().equals(verify.toString()))
        {
            verify.setError("Password must match password");
            valid =false;
        }
        else
        {
            verify.setError(null);
        }


        if (phone.length() > 10 && phone.length() <10)
        {
            phone.setError("Must be 10 digits");
        }

        if (Userpassword.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
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
