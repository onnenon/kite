package com.example.signin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView LoginTitle;
    private EditText EnterUsername;
    private EditText EnterPassword;
    private Button SignInButton;
    private TextView LoginResult;

    private String URL = "http://kite.onn.sh/api/auth/login";
    private RequestQueue Requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginTitle = (TextView) findViewById(R.id.Login);
        EnterUsername = (EditText) findViewById(R.id.EnterUsername);
        EnterPassword = (EditText) findViewById(R.id.EnterPassword);
        SignInButton = (Button) findViewById(R.id.SignIn);
        LoginResult = (TextView) findViewById(R.id.LoginStatus);

        Requests = Volley.newRequestQueue(this);

        SignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username = EnterUsername.getText().toString();
                String password = EnterPassword.getText().toString();

                login(username, password);
            }
        });
    }

    private void login(String username, String password) {

        JSONObject LoginCredentials = new JSONObject();

        try {
            LoginCredentials.put("Username", username);
            LoginCredentials.put("Username", username);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, URL, LoginCredentials,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplication(), response + "", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, error + "", Toast.LENGTH_SHORT).show();
                    }
                });

        Requests.add(loginRequest);
    }
}
