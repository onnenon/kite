package com.example.createuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView Status;
    private EditText EnterUsername;
    private EditText EnterBio;
    private EditText NumPosts;
    private Switch AdminBool;
    private Switch ModerBool;
    private Button PostUser;

    private RequestQueue PostRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Status = (TextView) findViewById(R.id.Status);
        EnterUsername = (EditText) findViewById(R.id.EnterUsername);
        EnterBio = (EditText) findViewById(R.id.EnterBio);
        NumPosts = (EditText) findViewById(R.id.NumPosts);
        AdminBool = (Switch) findViewById(R.id.AdminBool);
        ModerBool = (Switch) findViewById(R.id.ModerBool);
        PostUser = (Button) findViewById(R.id.PostUser);

        PostRequests = Volley.newRequestQueue(this);

        PostUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                jsonParse();

                // Status.setText("Pressed");
            }
        });
    }

    private void jsonParse() {

        // jsonParse material from video
        String URL = "http://kite.onn.sh/api/user";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            String userName = EnterUsername.getText().toString();
                            boolean isAdmin = AdminBool.isEnabled();
                            boolean isMod = ModerBool.isEnabled();
                            int postCount = Integer.parseInt(NumPosts.getText().toString());
                            String bio = EnterBio.getText().toString();

                            JSONObject newUser = new JSONObject();

                            newUser.put("username", userName);
                            newUser.put("is_admin", isAdmin);
                            newUser.put("is_mod", isMod);
                            newUser.put("post_count", postCount);
                            newUser.put("bio", bio);

                            System.out.println(newUser.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        PostRequests.add(postRequest);
    }
}
