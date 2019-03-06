package com.team100.kite_master.forum;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ForumHttp {

    //get json list of all users in the db
    public void getAllUsers() {

        String URL = "http://" + "10.0.2.2" + ":5000/api/v2/topics";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(), response + " ", Toast.LENGTH_LONG).show();
                        //System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(getActivity(), error + " ", Toast.LENGTH_LONG).show();
                        //System.out.println("ERROR" + error.toString());

                    }
                }
        );
        volleyqueue.add(getRequest);
    }











}
