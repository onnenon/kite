package com.team100.kite_master.forum;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.team100.kite_master.R;
import com.team100.kite_master.forum.forum_data_classes.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ForumNewPostFragment extends Fragment implements View.OnClickListener {

    public String LOCAL_IP_ADDRESS;

    String topic;

    private RequestQueue volleyqueue;
    private EditText titleText;
    private EditText bodyText;
    private EditText authorText;
    private Button postButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_new_post, container, false);
        //receive bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            topic = bundle.getString("selectedTopic"); //TODO
        }


        //set local ip for testing
        LOCAL_IP_ADDRESS = "10.0.1.2";
        //set topic string for testing
        //link edit text views
        titleText = (EditText) v.findViewById(R.id.title_edit_text);
        bodyText = (EditText) v.findViewById(R.id.body_edit_text);
        authorText = (EditText) v.findViewById(R.id.text_author);
        postButton = v.findViewById(R.id.post_button);
        //set on click listener
        postButton.setOnClickListener(this);
        //initialize volley queue
        volleyqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        //request topics from the backend
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set title
        Objects.requireNonNull(getActivity()).setTitle(topic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_button:
                sendPost(titleText.getText().toString(), bodyText.getText().toString(), authorText.getText().toString()); //TODO
                confirmAndClose();
                break;
        }
    }


    public void confirmAndClose(){
        showToast("Post Sent Successfully");
        Fragment fragment = new ForumTopicListFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack("tag");
        ft.commit();
    }



    //NETWORKING

    //create a single user
    public void sendPost(String title, String body, String author) {
        String URL = "http://kite.onn.sh/api/v2/posts";

        if (title.equals("") || body.equals("") || author.equals("")) {
            Toast.makeText(getActivity(), "Please fill out all fields!", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("title", title);
            jsonBody.put("author", author);
            jsonBody.put("topic", "Cars");
            jsonBody.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showToast(response);
                        //System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.toString());
                        System.out.println("ERROR" + error.toString());
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
        volleyqueue.add(postRequest);

    }



    //display a toast
    private void showToast(String message) {
        Toast.makeText(getActivity(), message + " ", Toast.LENGTH_LONG).show();
    }
}