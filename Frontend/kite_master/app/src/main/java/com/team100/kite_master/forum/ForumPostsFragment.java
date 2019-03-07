package com.team100.kite_master.forum;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.team100.kite_master.MainActivity;
import com.team100.kite_master.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class ForumPostsFragment extends Fragment implements View.OnClickListener {

    public String LOCAL_IP_ADDRESS;

    ListView postListView;
    ProgressBar loadingCircle;
    TextView errMessage;
    Button retryTopics;
    String topic;

    ArrayList<Post> postList = new ArrayList<Post>();
    private RequestQueue volleyqueue;
    CustomAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forum_posts, container, false);
        //set local ip for testing
        LOCAL_IP_ADDRESS = "10.0.1.100";
        //set topic string for testing
        topic = "Cars";
        //link list view
        postListView = v.findViewById(R.id.list_view);
        loadingCircle = v.findViewById(R.id.topics_loading);
        errMessage = v.findViewById(R.id.error_message);
        //set back arrow button
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize volley queue
        volleyqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        //request topics from the backend
        requestPosts(topic); //TODO
        //show loading circle until topics received
        loadingCircle.setVisibility(View.VISIBLE);
        //hide error text view
        errMessage.setVisibility(View.GONE);
        //initialize button
        retryTopics = v.findViewById(R.id.retry_topics);
        //set button on click listener
        retryTopics.setOnClickListener(this);
        //hide button
        retryTopics.setVisibility(View.GONE);
        //initialize custom adapter and set it to list view
        topicAdapter = new CustomAdapter();
        postListView.setAdapter(topicAdapter);
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
            case R.id.retry_topics:
                retryTopics.setVisibility(View.GONE);
                errMessage.setVisibility(View.GONE);
                requestPosts(topic);
                loadingCircle.setVisibility(View.VISIBLE);
                break;
        }
    }

    //custom topic adapter class
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return postList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.post_list_item, null);
            // initialize text views
            TextView topicTitle = (TextView) view.findViewById(R.id.text_title);
            TextView topicAuthor = (TextView) view.findViewById(R.id.text_author);
            TextView topicDescription = (TextView) view.findViewById(R.id.text_snippet);
            // iterate through list to set topic entries
            topicTitle.setText(postList.get(i).getPostTitle());
            topicAuthor.setText(postList.get(i).getPostAuthor());
            topicDescription.setText(postList.get(i).getPostBody());
            return view;
        }
    }

    //NETWORKING
    //requests topic JSON object from backend
    public void requestPosts(String topic) {
        System.out.println("REQUESTING POSTS");
        String URL = "http://" + LOCAL_IP_ADDRESS + ":5000/api/v2/topics/"+topic;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //parse topics to array from json response
                            parseTopics(response);
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
                        retryTopics.setVisibility(View.VISIBLE);
                    }
                }
        );
        volleyqueue.add(getRequest);
    }


    //convert JSON object from backend to arraylist of topics
    public void parseTopics(JSONObject resp) throws JSONException {
        //create output list
        ArrayList<Post> receivedPosts = new ArrayList<Post>();
        //get json array of posts
        JSONObject jdata = resp.getJSONObject("data");
        JSONObject jtopic = jdata.getJSONObject("topic");
        JSONArray jposts = jtopic.getJSONArray("posts");
        //for each element in the array create a new topic object and add it to the array list
        for (int i = 0; i < jposts.length(); i++) {
            JSONObject curPost = jposts.getJSONObject(i);
            Post p = new Post(curPost.getString("id"), curPost.getString("title"), curPost.getString("body"), curPost.getString("author"), curPost.getBoolean("edited"), curPost.getString("topic_name"), curPost.getString("date"));
            receivedPosts.add(p);
        }
        //update global topic list
        postList = new ArrayList<Post>(receivedPosts);
        //sort topic list in alphabetical order
        Collections.sort(postList);
        //notify adapter to update its list with the new topics
        topicAdapter.notifyDataSetChanged();
        //hide loading circle
        loadingCircle.setVisibility(View.GONE);
    }

    //display a toast
    private void showToast(String message){
        Toast.makeText(getActivity(), message + " ", Toast.LENGTH_LONG).show();
    }
}