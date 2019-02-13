package com.example.kite_master;


//standard imports

import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//json imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//volley imports
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class ForumFragment extends Fragment implements View.OnClickListener {

    //textviews
    private TextView output_box;
    //textfields
    private EditText create_uname;
    private EditText create_pass;
    private EditText create_bio;
    //buttons
    private Button get_user_button;
    private Button create_user_button;
    private Button update_user_button;
    private Button delete_user_button;
    private Button get_single_user_button;
    //vars
    private RequestQueue volleyqueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forum, container, false);

        //instantiate buttons
        get_user_button = (Button) v.findViewById(R.id.get_user_button);
        create_user_button = (Button) v.findViewById(R.id.create_user_button);
        update_user_button = (Button) v.findViewById(R.id.update_user_button);
        delete_user_button = (Button) v.findViewById(R.id.delete_user_button);
        get_single_user_button = (Button) v.findViewById(R.id.get_single_user_button);
        //set onclick listeners to self
        get_user_button.setOnClickListener(this);
        create_user_button.setOnClickListener(this);
        update_user_button.setOnClickListener(this);
        delete_user_button.setOnClickListener(this);
        get_single_user_button.setOnClickListener(this);

        //instantiate edit text
        create_uname = (EditText) v.findViewById(R.id.create_uname);
        create_pass = (EditText) v.findViewById(R.id.create_pass);
        create_bio = (EditText) v.findViewById(R.id.create_bio);
        output_box = (TextView) v.findViewById(R.id.output_box);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        volleyqueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Forum");



        //getAllUsers();
        //createUser("joshua", "banana", "This is a bio");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_user_button:
                System.out.println("Pushed get user");
                break;
            case R.id.create_user_button:
                System.out.println("Pushed create user");
                createUser(create_uname.getText().toString(), create_pass.getText().toString(), create_bio.getText().toString());
                break;
            case R.id.update_user_button:
                System.out.println("Pushed update user");
                break;
            case R.id.delete_user_button:
                System.out.println("Pushed delete user");
                break;
            case R.id.get_single_user_button:
                System.out.println("Pushed get single user");
                break;
        }
    }


    public JSONObject getAllUsers() {

        final JSONObject responseJson = new JSONObject();
        String URL = "http://10.0.2.2:5000/api/user";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        output_box.setText(response.toString());
                        System.out.println(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR");
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        volleyqueue.add(getRequest);
        return responseJson;
    }


    public void createUser(String username, String password, String bio) {
        String URL = "http://10.0.2.2:5000/api/user";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("bio", bio);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        output_box.setText(response.toString());
                        System.out.println(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        volleyqueue.add(postRequest);
    }


}