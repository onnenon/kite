package com.team100.kite_master.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.team100.kite_master.MainActivity;
import com.team100.kite_master.R;
import com.team100.kite_master.forum.ForumPostListFragment;
import com.team100.kite_master.forum.ForumTopicListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LoginFragment extends Fragment implements View.OnClickListener {

    Button loginButton;
    EditText loginUsername;
    EditText loginPassword;
    private boolean successfulIP;

    private RequestQueue volleyqueue;
    private String webToken;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_screen, container, false);
        loginUsername = v.findViewById(R.id.login_username);
        loginPassword = v.findViewById(R.id.login_password);
        loginButton = (Button) v.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Login");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).hide();
        successfulIP = false;
        render();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        volleyqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        super.onActivityCreated(savedInstanceState);
    }


    //handle retry button click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                if(successfulIP) {
                    loginPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    loginUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    login(loginUsername.getText().toString(), loginPassword.getText().toString(), "http://" + LOCAL_IP_ADDRESS + ":5000/api/auth/login");
                } else {
                    checkIP(loginUsername.getText().toString());
                    loginUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
                break;
        }
    }



    private void render(){
        if(successfulIP){
            loginUsername.getText().clear();
            loginUsername.setHint("Username");
            loginUsername.setVisibility(View.VISIBLE);
            loginPassword.setVisibility(View.VISIBLE);
            loginButton.setText("Login");
        } else {
            loginUsername.setHint("IP Address (ex. 10.0.1.1)");
            loginUsername.setVisibility(View.VISIBLE);
            loginPassword.setVisibility(View.GONE);
            loginButton.setText("Set IP");
        }
    }


    private void checkIP(String ip){
        if(ip.length() > 0){
            successfulIP = true;
            Toast.makeText(getActivity(), "Connected!" + " ", Toast.LENGTH_LONG).show();
            render();
        } else {
            render();
        }
    }


    String LOCAL_IP_ADDRESS = "10.0.1.2";


    private void login(final String username, final String password, final String URL) {

        JSONObject LoginCredentials = new JSONObject();
        try {
            LoginCredentials.put("Username", username);
            LoginCredentials.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject token = response.getJSONObject("data");
                            webToken = token.getString("access_token");
                            System.out.println("TOKEN: " + webToken);
                            Toast.makeText(getActivity(), "Logging In!" + " ", Toast.LENGTH_LONG).show();
                            logIn(username);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.toString();
                        if (err.equals("com.android.volley.AuthFailureError")) {
                            Toast.makeText(getActivity(), "Incorrect Password" + " ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Network Error" + " ", Toast.LENGTH_LONG).show();
                        }
                        loginPassword.getText().clear();
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

        volleyqueue.add(loginRequest);
    }


    private void logIn(String username){
        SaveSharedPreference.setUserName(getActivity(), username);
        ((MainActivity) Objects.requireNonNull(getActivity())).currentUser.setUsername(username);
        Fragment fragment = new ForumTopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("curUser", username);
        fragment.setArguments(bundle);
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }



}