package com.team100.kite_master.forum;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.team100.kite_master.R;
import com.team100.kite_master.forum.forum_data_classes.DateUtil;
import com.team100.kite_master.forum.forum_data_classes.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class ForumPostListFragment extends Fragment implements View.OnClickListener {

    private String LOCAL_IP_ADDRESS;
    private String[] userdata;

    //view item instantiation
    ListView postListView;
    ProgressBar loadingCircle;
    TextView errMessage;
    Button retryTopics;
    String topic;

    FloatingActionButton newPostFab;

    //other instantiations
    ArrayList<Post> postList = new ArrayList<Post>();
    private RequestQueue volleyqueue;
    CustomAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_post_list, container, false);
        //receive bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userdata = bundle.getStringArray("userData");
            topic = bundle.getString("selectedTopic");
            LOCAL_IP_ADDRESS = bundle.getString("serverIP");
        }


        //DEBUGGING
        System.out.println(" ");
        System.out.println("POST LIST FRAGMENT:");
        System.out.println("CURRENT TOPIC: " + topic);
        System.out.println("USER: " + Arrays.toString(userdata));
        System.out.println("IP ADDRESS: " + LOCAL_IP_ADDRESS);
        System.out.println(" ");


        //link view items
        postListView = v.findViewById(R.id.list_view);
        loadingCircle = v.findViewById(R.id.topics_loading);
        errMessage = v.findViewById(R.id.error_message);
        newPostFab = v.findViewById(R.id.new_post_fab);
        retryTopics = v.findViewById(R.id.retry_topics);

        //set on click listeners
        newPostFab.setOnClickListener(this);
        retryTopics.setOnClickListener(this);

        //initialize volley queue
        volleyqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        //request topics from the backend




        //show loading circle until topics received
        loadingCircle.setVisibility(View.VISIBLE);
        //hide error text view
        errMessage.setVisibility(View.GONE);
        //hide fab
        newPostFab.show();
        //hide button
        retryTopics.setVisibility(View.GONE);

        //initialize custom adapter and set it to list view
        topicAdapter = new CustomAdapter();
        postListView.setAdapter(topicAdapter);

        //show action bar buttons
        setHasOptionsMenu(true);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(topic);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();

        //set on click listener for menu items
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPost(postList.get(position).getPostID());
                Animation animation1 = new AlphaAnimation(0.3f, 4.0f);
                animation1.setDuration(4000);
                view.startAnimation(animation1);
            }
        });



        //hides FAB when scrolling
        postListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 1 && newPostFab.isShown()){
                    newPostFab.hide();
                } else {
                    newPostFab.show();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        requestPosts(topic);

    }


    //creates action bar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_buttons, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    //handles fragment on click listeners
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_topics:
                retryTopics.setVisibility(View.GONE);
                errMessage.setVisibility(View.GONE);
                requestPosts(topic);
                loadingCircle.setVisibility(View.VISIBLE);
                break;
            case R.id.new_post_fab:
                openNewPost();
                break;
        }
    }

    //handles clicks of the refresh button in the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                loadingCircle.setVisibility(View.VISIBLE);
                requestPosts(topic);
                break;
        }
        return true;
    }


    //switch to new post fragment when fab is clicked
    public void openNewPost() {
        Fragment fragment = new ForumNewPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("newPostTopic", topic);
        bundle.putString("serverIP", LOCAL_IP_ADDRESS);
        bundle.putStringArray("userData", userdata);
        fragment.setArguments(bundle);
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        ft.replace(R.id.content_frame, fragment).addToBackStack("tag");
        ft.commit();
    }

    //switch to post fragment when one is clicked
    public void openPost(String postID) {
        Fragment fragment = new ForumPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedPost", postID);
        bundle.putString("serverIP", LOCAL_IP_ADDRESS);
        bundle.putStringArray("userData", userdata);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack("tag");
        ft.commit();
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

        public String getPostID(int i) {
            return postList.get(i).getPostID();
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.forum_post_list_card, null);
            // initialize text views
            TextView topicTitle = view.findViewById(R.id.text_title);
            TextView topicAuthor = view.findViewById(R.id.text_author);
            TextView topicTime = view.findViewById(R.id.text_time);
            //ImageView postImage = view.findViewById(R.id.image_postimage);
            // iterate through list to set topic entries
            topicTitle.setText(postList.get(i).getPostTitle());
            topicAuthor.setText(postList.get(i).getPostAuthor());
            topicTime.setText(DateUtil.getTimeAgo(Long.parseLong(postList.get(i).getPostTime())));
            //add images here when support is added
            return view;
        }
    }




    //NETWORKING
    //requests topic JSON object from backend
    public void requestPosts(String topic) {
        String URL = "http://" + LOCAL_IP_ADDRESS + ":5000/api/v2/topics/" + topic;
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
                        Toast.makeText(getActivity(), error.toString() + " ", Toast.LENGTH_LONG).show();
                        loadingCircle.setVisibility(View.GONE);
                        errMessage.setText("Connection Error\n Make sure your forum server is running.");
                        errMessage.setVisibility(View.VISIBLE);
                        retryTopics.setVisibility(View.VISIBLE);
                        newPostFab.hide();
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
            Post p = new Post(
                    curPost.getString("id"),
                    curPost.getString("title"),
                    curPost.getString("body"),
                    curPost.getString("author"),
                    curPost.getBoolean("edited"),
                    curPost.getString("topic_name"),
                    curPost.getString("date"));
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

        if (postList.size() == 0) {
            errMessage.setText("There are no posts in this topic");
            errMessage.setVisibility(View.VISIBLE);
        } else {
            errMessage.setVisibility(View.GONE);
        }
    }
}
