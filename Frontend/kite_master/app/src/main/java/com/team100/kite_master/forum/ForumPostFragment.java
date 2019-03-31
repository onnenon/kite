package com.team100.kite_master.forum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.team100.kite_master.R;
import com.team100.kite_master.forum.forum_data_classes.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ForumPostFragment extends Fragment implements View.OnClickListener {

        String LOCAL_IP_ADDRESS;
        ProgressBar loadingCircle;
        TextView errMessage;
        Button retry;
        String postID;
        RequestQueue volleyqueue;
        ImageView postImageView;
        TextView postTitleView;
        TextView postTimeView;
        TextView postAuthorView;
        TextView postBodyView;
        ScrollView postScrollView;



        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.forum_post, container, false);
            //receive bundle
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                postID = bundle.getString("selectedPost");
            }
            //set local ip for testing
            LOCAL_IP_ADDRESS = "10.0.1.2";


            //initialize error elements
            loadingCircle = v.findViewById(R.id.topics_loading);
            errMessage = v.findViewById(R.id.error_message);
            //initialize image and text views
            postImageView = v.findViewById(R.id.single_post_image);
            postTitleView = v.findViewById(R.id.single_post_title);
            postTimeView = v.findViewById(R.id.single_post_time);
            postAuthorView = v.findViewById(R.id.single_post_author);
            postBodyView = v.findViewById(R.id.single_post_body);
            postScrollView = v.findViewById(R.id.post_scroll_view);
            //hide everything until post is gotten
            displayElements(false);
            //initialize volley queue
            volleyqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            //request post from the backend
            requestPost(postID);
            //show loading circle until topics received
            loadingCircle.setVisibility(View.VISIBLE);
            //hide error text view
            errMessage.setVisibility(View.GONE);
            //initialize button
            retry = v.findViewById(R.id.retry_topics);
            //set button on click listener
            retry.setOnClickListener(this);
            //hide button
            retry.setVisibility(View.GONE);
            return v;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //set title
            Objects.requireNonNull(getActivity()).setTitle("Post");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.retry_topics:
                    retry.setVisibility(View.GONE);
                    errMessage.setVisibility(View.GONE);
                    requestPost(postID);
                    loadingCircle.setVisibility(View.VISIBLE);
                    break;
            }
        }


        //NETWORKING
        //requests topic JSON object from backend
        public void requestPost(String postid) {
            System.out.println("REQUESTING POST");
            String URL = "http://kite.onn.sh/api/v2/posts/" + postid;
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //parse topics to array from json response
                                parsePost(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showToast(error.toString());
                            loadingCircle.setVisibility(View.GONE);
                            errMessage.setText("Connection Error\n Make sure your forum server is running.");
                            errMessage.setVisibility(View.VISIBLE);
                            retry.setVisibility(View.VISIBLE);
                        }
                    }
            );
            volleyqueue.add(getRequest);
        }


        //convert JSON object from backend to arraylist of topics
        public void parsePost(JSONObject resp) throws JSONException {
            //get json object and convert to post
            ArrayList<Post> receivedPosts = new ArrayList<Post>();
            //get json array of posts
            JSONObject jdata = resp.getJSONObject("data");
            JSONObject jpost = jdata.getJSONObject("post");
            //create new post object from data
            Post p = new Post(jpost.getString("id"), jpost.getString("title"), jpost.getString("body"), jpost.getString("author"), jpost.getBoolean("edited"), jpost.getString("topic_name"), jpost.getString("date"));
            //hide loading circle
            loadingCircle.setVisibility(View.GONE);

            postTitleView.setText(p.getPostTitle());
            postAuthorView.setText(p.getPostAuthor());
            postTimeView.setText(p.getPostDate());
            postBodyView.setText(p.getPostBody());
            displayElements(true);
        }

        //display a toast
        private void showToast(String message) {
            Toast.makeText(getActivity(), message + " ", Toast.LENGTH_LONG).show();
        }

        private void displayElements(Boolean areDisplayed){
            if(areDisplayed){
                postScrollView.setVisibility(View.VISIBLE);
            } else {
                postScrollView.setVisibility(View.GONE);
            }
        }


    }
