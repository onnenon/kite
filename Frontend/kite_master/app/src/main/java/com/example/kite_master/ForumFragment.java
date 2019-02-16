package com.example.kite_master;


//android imports

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//json imports
import org.json.JSONException;
import org.json.JSONObject;

//volley imports
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//java imports
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class ForumFragment extends Fragment implements View.OnClickListener {

    public String LOCAL_IP_ADDRESS;

    //text fields
    private EditText create_username;
    private EditText create_pass;
    private EditText create_bio;
    private EditText get_single_username;
    private EditText delete_user_text;
    //buttons
    private Button get_user_button;
    private Button create_user_button;
    private Button update_user_button;
    private Button delete_user_button;
    private Button get_single_user_button;
    private CheckBox modCheckBox;
    private CheckBox adminCheckBox;
    //vars
    private RequestQueue volleyqueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forum, container, false);

        //set ip
        LOCAL_IP_ADDRESS = "10.0.1.2";

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

        //instantiate check boxes
        adminCheckBox = (CheckBox) v.findViewById(R.id.adminCheckBox);
        modCheckBox = (CheckBox) v.findViewById(R.id.modCheckBox);

        //instantiate edit text
        create_username = (EditText) v.findViewById(R.id.create_uname);
        create_pass = (EditText) v.findViewById(R.id.create_pass);
        create_bio = (EditText) v.findViewById(R.id.create_bio);
        get_single_username = (EditText) v.findViewById(R.id.get_single_uname);
        delete_user_text = (EditText) v.findViewById(R.id.delete_user_textedit);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        volleyqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Api Tests");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_user_button:
                System.out.println("Pushed get user");
                getAllUsers();
                break;
            case R.id.create_user_button:
                System.out.println("Pushed create user");
                createUser(create_username.getText().toString(), create_pass.getText().toString(), create_bio.getText().toString());
                break;
            case R.id.update_user_button:
                System.out.println("Pushed update user");
                updateUser(create_username.getText().toString(), create_pass.getText().toString(), create_bio.getText().toString(), modCheckBox.isChecked(), adminCheckBox.isChecked());
                break;
            case R.id.delete_user_button:
                System.out.println("Pushed delete user");
                deleteUser(delete_user_text.getText().toString());
                break;
            case R.id.get_single_user_button:
                System.out.println("Pushed get single user");
                getSingleUser(get_single_username.getText().toString());
                break;
        }
    }


    //VOLLEY METHODS

    //get json list of all users in the db
    public void getAllUsers() {

        String URL = "http://" + LOCAL_IP_ADDRESS + ":5000/api/user";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response + " ", Toast.LENGTH_LONG).show();
                        //System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error + " ", Toast.LENGTH_LONG).show();
                        //System.out.println("ERROR" + error.toString());

                    }
                }
        );
        volleyqueue.add(getRequest);
    }


    //create a single user
    public void createUser(String username, String password, String bio) {
        String URL = "http://" + LOCAL_IP_ADDRESS + ":5000/api/user";

        if (username.equals("") || password.equals("") || bio.equals("")) {
            Toast.makeText(getActivity(), "Please fill out all fields!", Toast.LENGTH_LONG).show();
            return;
        }

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
                        Toast.makeText(getActivity(), response + " ", Toast.LENGTH_LONG).show();
                        //System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString() + " ", Toast.LENGTH_LONG).show();
                        //System.out.println("ERROR" + error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        //set fields back to blank
        create_username.setText("");
        create_pass.setText("");
        create_bio.setText("");

        volleyqueue.add(postRequest);

    }

    //send put request to update password, bio, admin and mod status
    public void updateUser(String username, String password, String bio, boolean isMod, boolean isAdmin) {


        String URL = "http://" + LOCAL_IP_ADDRESS + ":5000/api/user/" + username;

        JSONObject jsonBody = new JSONObject();
        try {

            if (username.equals("")) {
                Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_LONG).show();
                return;
            }
            if (!password.equals("")) jsonBody.put("password", password);
            if (!bio.equals("")) jsonBody.put("bio", bio);
            jsonBody.put("is_admin", isAdmin);
            jsonBody.put("is_mod", isMod);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response + " ", Toast.LENGTH_LONG).show();
                        //System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "User Doesn't Exist\n" + error.toString() + " ", Toast.LENGTH_LONG).show();
                        //System.out.println("ERROR" + error.toString());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        //reset fields
        create_username.setText("");
        create_pass.setText("");
        create_bio.setText("");

        volleyqueue.add(postRequest);

    }

    //delete a single user given a username
    public void deleteUser(String username) {

        if (username.equals("")) {
            Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        }

        String URL = "http://" + LOCAL_IP_ADDRESS + ":5000/api/user/" + username;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response + " ", Toast.LENGTH_LONG).show();
                        //System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "User Doesn't Exist\n" + error.toString() + " ", Toast.LENGTH_LONG).show();
                        System.out.println("ERROR");
                        //System.out.println("ERROR" + error.toString());
                    }
                }
        );
        //set fields back to blank
        delete_user_text.setText("");

        volleyqueue.add(getRequest);
    }

    //get info for a single user given a username
    public void getSingleUser(String username) {

        if (username.equals("")) {
            Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        }

        String URL = "http://" + LOCAL_IP_ADDRESS + ":5000/api/user/" + username;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response + " ", Toast.LENGTH_LONG).show();
                        //System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "User Doesn't Exist\n" + error.toString() + " ", Toast.LENGTH_LONG).show();
                        //System.out.println("ERROR" + error.toString());
                    }
                }
        );
        //set field back to blank
        get_single_username.setText("");

        volleyqueue.add(getRequest);

    }

}