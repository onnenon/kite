package com.example.httpcommunicationwithjwt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button SignInButton;
    private TextView LoginJWT;

    private String URL = "http://kite.onn.sh/api/auth/login";
    private RequestQueue Requests;

    private String JWT;



    private TextView Status;
    private EditText EnterUsername;

    private Button GetUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton = (Button) findViewById(R.id.SignIn);
        LoginJWT = (TextView) findViewById(R.id.LoginStatus);



        Status = (TextView) findViewById(R.id.Status);
        EnterUsername = (EditText) findViewById(R.id.EnterUsername);

        GetUser = (Button) findViewById(R.id.GetUser);



        Requests = Volley.newRequestQueue(this);

        SignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username = "fadmin";
                String password = "pass";

                login(username, password);
            }
        });

        GetUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                jsonParse();
            }
        });
    }

    private void login(final String username, final String password) {

        JSONObject LoginCredentials = new JSONObject();

        try {
            LoginCredentials.put("Username", username);
            LoginCredentials.put("Password", password);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject token = response.getJSONObject("data");

                            JWT = token.getString("access_token");

                            LoginJWT.setText(JWT);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplication(), response + "", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        LoginJWT.setText("Username or password incorrect?");

                        Toast.makeText(MainActivity.this, error + "", Toast.LENGTH_SHORT).show();
                    }
                })

        {
            // Credit to the people at this source: https://stackoverflow.com/questions/44000212/how-to-send-authorization-header-in-android-using-volley-library
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String credentials = username + ":" + password;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
        };

        Requests.add(loginRequest);
    }

    private void jsonParse() {

        // jsonParse material from video
        String URL = "http://kite.onn.sh/api/v2/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject data = response.getJSONObject("data");

                            JSONArray userData = data.getJSONArray("users");

                            JSONObject user = userData.getJSONObject(0);

                            String userName = user.getString("username");
                            boolean isAdmin = user.getBoolean("is_admin");
                            boolean isMod = user.getBoolean("is_mod");
                            int postCount = user.getInt("post_count");
                            String bio = user.getString("bio");
                            String displayName = user.getString("displayName");

                            Status.setText(userName + ", " + bio + ", " + Integer.toString(postCount) + ", " + Boolean.toString(isAdmin) + ", " + Boolean.toString(isMod) + ", " + displayName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Status.setText(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Requests.add(request);
    }
}

/*

            // Credit to the people at this source: https://stackoverflow.com/questions/25941658/volley-how-to-send-jsonobject-using-bearer-accesstoken-authentication
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String credentials = username + ":" + password;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "Basic " + base64EncodedCredentials);

                return headers;
            }
 */