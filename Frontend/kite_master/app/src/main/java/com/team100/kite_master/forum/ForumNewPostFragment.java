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
import android.view.inputmethod.EditorInfo;
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

    private String LOCAL_IP_ADDRESS;
    private String[] userdata;

    //declare global vars
    private RequestQueue volleyqueue;
    private String newPostTopicString;

    //declare layout items
    private EditText titleText;
    private EditText bodyText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_new_post, container, false);
        //receive bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userdata = bundle.getStringArray("userData");
            newPostTopicString = bundle.getString("newPostTopic");
        }

        //set local ip for testing
        LOCAL_IP_ADDRESS = "10.0.1.2";

        //link layout items
        titleText = (EditText) v.findViewById(R.id.title_edit_text);
        bodyText = (EditText) v.findViewById(R.id.body_edit_text);
        Button postButton = v.findViewById(R.id.post_button);
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
        Objects.requireNonNull(getActivity()).setTitle("New Post - " + newPostTopicString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_button:
                titleText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                bodyText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                sendPost(titleText.getText().toString(), bodyText.getText().toString(), userdata[0]); //TODO
                break;
        }
    }

    public void confirmAndClose(){
        Toast.makeText(getActivity(), "Post sent successfully!" + " ", Toast.LENGTH_LONG).show();
        Fragment fragment = new ForumPostListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedTopic", newPostTopicString);
        bundle.putStringArray("userData", userdata);
        getActivity().getSupportFragmentManager().popBackStack();
        //fragment.setArguments(bundle);
        //FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        //ft.replace(R.id.content_frame, fragment);
        //ft.commit();
    }


    //NETWORKING

    //send new post
    public void sendPost(String title, String body, String author) {
        String URL = "http://kite.onn.sh/api/v2/posts";

        if (title.equals("") || body.equals("") || author.equals("")) {
            Toast.makeText(getActivity(), "Please fill out all fields!", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("title", title);
            jsonBody.put("author", author); //TODO
            jsonBody.put("topic", newPostTopicString);
            jsonBody.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        confirmAndClose();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString() + " ", Toast.LENGTH_LONG).show();
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

}