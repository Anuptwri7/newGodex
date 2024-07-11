package com.godex.sample;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class CustomJsonObjectRequest extends JsonObjectRequest {
    private Response.Listener<NetworkResponse> responseListener;

    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener,
                                   Response.Listener<NetworkResponse> responseListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.responseListener = responseListener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        // Call the custom listener with the NetworkResponse
        responseListener.onResponse(response);
        return super.parseNetworkResponse(response);
    }
}
