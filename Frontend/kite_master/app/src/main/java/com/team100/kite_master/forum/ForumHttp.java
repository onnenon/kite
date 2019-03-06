package com.team100.kite_master.forum;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class ForumHttp {


    private RequestQueue vqueue;
    private ArrayList<Topic> topicList;
    public ForumHttp(RequestQueue r) {
        vqueue = r;
    }




    //get json list of all users in the db
    public void requestTopics() {
        System.out.println("REQUESTING TOPICS");
        String URL = "http://" + "10.0.1.100" + ":5000/api/v2/topics";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE RECEIVED");
                        try {
                            parseTopics(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR" + error.toString());
                    }
                }
        );
        vqueue.add(getRequest);
    }


    public void parseTopics(JSONObject resp) throws JSONException {
        System.out.println("PARSING STARTED");
        ArrayList<Topic> tops = new ArrayList<Topic>();

        JSONArray topics = resp.getJSONObject("data").getJSONArray("topics");
        for (int i = 0; i < topics.length(); i++) {
            Topic t = new Topic(topics.getJSONObject(i).getString("name"), topics.getJSONObject(i).getString("descript"));
            tops.add(t);
        }
        System.out.println("PARSED: "+ tops.toString());
        topicList = new ArrayList<Topic>(tops);
    }

    public ArrayList<Topic> getTopics(){
        return topicList;
    }
}

