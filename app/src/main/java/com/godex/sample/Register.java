package com.godex.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends Activity {

    private EditText emailField, passwordField, confirmPasswordField, phoneField;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        emailField = findViewById(R.id.emailReg);
        passwordField = findViewById(R.id.passwordReg);
        confirmPasswordField = findViewById(R.id.confirm_password);
        phoneField = findViewById(R.id.phone);
        submitButton = findViewById(R.id.submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming the phone should be at least 10 digits long
        if (phone.length() < 10) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create UserData object
        UserData userData = new UserData(password, confirmPassword, email, phone);

        // Send POST request
        sendPostRequest(userData);
    }

    private void sendPostRequest(UserData userData) {
        String url = ApiConstants.REGISTER_URL;;

        JSONObject postData = new JSONObject();
        try {
            postData.put("password", userData.getPassword());
            postData.put("confirm_password", userData.getConfirm_password());
            postData.put("email", userData.getEmail());
            postData.put("phone", userData.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the JSON response if needed
                        try {
                            // Example: Check for success message or data
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(Register.this, "Success!", Toast.LENGTH_SHORT).show();

                                int statusCode = response.getInt("status");
                                if (statusCode == 201) {
                                    finish();
                                }
                            } else {
                                String message = response.getString("message");
                                Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // Handle the network response directly if needed
                        int statusCode = response.statusCode;
                        Log.d("NetworkResponse", "Status Code: " + statusCode);
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}