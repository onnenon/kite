package com.team100.kite_master.forum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.team100.kite_master.MainActivity;
import com.team100.kite_master.R;
import com.team100.kite_master.forum.forum_data_classes.DateUtil;
import com.team100.kite_master.forum.forum_data_classes.Post;
import com.team100.kite_master.networking.NetworkManager;
import com.team100.kite_master.networking.VolleyListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ForumPostFragment extends Fragment implements View.OnClickListener {

    //declare global vars
    String postID;

    //declare error layout items
    ProgressBar loadingCircle;
    TextView errMessage;
    Button retry;
    Post currentPost;
    boolean isFavorited;

    //declare layout items
    ScrollView postScrollView;
    ImageView postImageView;
    TextView postTitleView;
    TextView postTimeView;
    TextView postAuthorView;
    TextView postBodyView;
    MenuItem favorite;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_post, container, false);
        //receive bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            postID = bundle.getString("selected_post");
        }

        //LOCAL DEBUGGING
        System.out.println(" ");
        System.out.println("POST FRAGMENT:");
        System.out.println("CURRENT POST: " + postID);
        System.out.println(" ");

        //initialize layout elements
        loadingCircle = v.findViewById(R.id.topics_loading);
        errMessage = v.findViewById(R.id.error_message);
        postImageView = v.findViewById(R.id.single_post_image);
        postTitleView = v.findViewById(R.id.single_post_title);
        postTimeView = v.findViewById(R.id.single_post_time);
        postAuthorView = v.findViewById(R.id.single_post_author);
        postBodyView = v.findViewById(R.id.single_post_body);
        postScrollView = v.findViewById(R.id.post_scroll_view);
        retry = v.findViewById(R.id.retry_topics);

        //initialize boolean
        isFavorited = false;

        //set button on click listener
        retry.setOnClickListener(this);

        //hide everything until post is gotten
        postScrollView.setVisibility(View.GONE);

        //request posts
        requestPost(postID);
        //show loading circle until topics received
        loadingCircle.setVisibility(View.VISIBLE);

        //show action menu
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();
        setHasOptionsMenu(true);

        //set current screen
        ((MainActivity) Objects.requireNonNull(getActivity())).setCurScreen("post");

        //hide error elements
        errMessage.setVisibility(View.GONE);
        retry.setVisibility(View.GONE);
        return v;
    }

    //creates action bar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_buttons, menu);
        MenuItem refresh = menu.findItem(R.id.menu_refresh);
        favorite = menu.findItem(R.id.menu_post_favorite);
        refresh.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Post");
    }


    //handle retry button click
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



    //handles click of buttons in the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_favorite:
                if(isFavorited){
                    ((MainActivity) Objects.requireNonNull(getActivity())).removeFavoritePost(currentPost);
                    favorite.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_menu_favorite));
                    isFavorited = false;
                } else {
                    ((MainActivity) Objects.requireNonNull(getActivity())).addFavoritePost(currentPost);
                    favorite.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_menu_star_filled));
                    isFavorited = true;
                }
                break;
        }
        return true;
    }



    //NETWORKING
    //requests topic JSON object from backend
    public void requestPost(String postid) {
        NetworkManager.getInstance().requestPost(postid, new VolleyListener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) {
                try {
                    ForumParser fp = new ForumParser();
                    setViewElements(fp.parsePost(object));
                } catch (JSONException e) {
                    displayErrorRetry(e.toString());
                }
            }

            @Override
            public void getError(VolleyError err) {
                displayErrorRetry(err.toString());
            }
        });
    }


    public void displayErrorRetry(String err) {
        Toast.makeText(getActivity(), err + " ", Toast.LENGTH_LONG).show();
        loadingCircle.setVisibility(View.GONE);
        errMessage.setText("Connection Error\n Make sure your forum server is running.");
        errMessage.setVisibility(View.VISIBLE);
        retry.setVisibility(View.VISIBLE);
    }


    public void setViewElements(Post p) {
        currentPost = p;
        loadingCircle.setVisibility(View.GONE);
        postTitleView.setText(p.getPostTitle());
        String atAuthor = "@" + p.getPostAuthor();
        postAuthorView.setText(atAuthor);
        DateUtil d = new DateUtil();
        String date = d.getCleanDate(Long.parseLong(p.getPostTime()), "MM/dd/yy hh:mma");
        postTimeView.setText(date);
        postBodyView.setText(p.getPostBody());
        postScrollView.setVisibility(View.VISIBLE);

        if (((MainActivity) Objects.requireNonNull(getActivity())).getFavoritePostIDList() != null) {
            System.out.println("HERE");
            if (((MainActivity) Objects.requireNonNull(getActivity())).getFavoritePostIDList().contains(p.getPostID())) {
                isFavorited = true;
                favorite.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_menu_star_filled));
            }
        }

    }
}
