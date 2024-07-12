package com.godex.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getActionBar().hide();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerTextView = findViewById(R.id.textView4);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, FrontPlateActivity.class);
//                startActivity(intent);
                progressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                login();
            }
        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        sendPostRequest(email, password);
    }

    private void sendPostRequest(String email, String password) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiConstants.LOGIN_URL, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject subscription = response.getJSONObject("subscription");
                            String startDateStr = subscription.getString("start_date");
                            String endDateStr = subscription.getString("end_date");
                            progressBar.setVisibility(View.GONE);
                            loginButton.setEnabled(true);
                            // Parse the date using java.time API
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                                LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                                LocalDate endDate = LocalDate.parse(endDateStr, formatter);
                                LocalDate currentDate = LocalDate.now();

                                Log.d("LoginActivity", "Start Date: " + startDate.toString());
                                Log.d("LoginActivity", "End Date: " + endDate.toString());
                                Log.d("LoginActivity", "Current Date: " + currentDate.toString());

                                // Calculate remaining days
                                long remainingDays = Duration.between(currentDate.atStartOfDay(), endDate.atStartOfDay()).toDays();

                                Log.d("LoginActivity", "Remaining Days: " + remainingDays);

                                // Store remaining days in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putLong("remaining_days", remainingDays);
                                editor.apply();

                                // Navigate to the main page
                                Intent intent = new Intent(LoginActivity.this, FrontPlateActivity.class);
                                intent.putExtra("remaining_days", remainingDays);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        if (networkResponse != null) {
                            try {
                                String responseBody = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                                Log.e("VolleyError", "Status Code: " + networkResponse.statusCode + " Response: " + responseBody);
                                Toast.makeText(LoginActivity.this, "Error: " + networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
