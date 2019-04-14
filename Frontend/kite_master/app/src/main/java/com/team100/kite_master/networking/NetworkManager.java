package com.team100.kite_master.networking;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.team100.kite_master.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NetworkManager
{
    private static NetworkManager instance = null;
    private String url;

    //for Volley API
    public RequestQueue requestQueue;



    private NetworkManager(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context)
    {
        if (null == instance)
            instance = new NetworkManager(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void setUrl(String ip){
        url = "http://" + ip + ":5000";
    }



    //LOGIN

    //calls to /api/status to check if the ip is actually a kite server
    public void testIP(String ip, final VolleyListener<JSONObject> listener) {
        setUrl(ip);
        String URL = url+ "/api/status";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }



    //tries to login with credentials
    public void login(final String username, final String password, final VolleyListener<JSONObject> listener) {
        String URL = url+ "/api/auth/login";

        JSONObject LoginCredentials = new JSONObject();
        try {
            LoginCredentials.put("Username", username);
            LoginCredentials.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                String credentials = username + ":" + password;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };
        requestQueue.add(getRequest);
    }



    //POSTS


    public void requestPost(String post_id, final VolleyListener<JSONObject> listener) {
        String URL = url + "api/v2/posts/" + post_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }








}