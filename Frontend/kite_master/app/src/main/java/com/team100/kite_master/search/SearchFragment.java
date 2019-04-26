package com.team100.kite_master.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.team100.kite_master.MainActivity;
import com.team100.kite_master.R;
import com.team100.kite_master.forum.ForumNewPostFragment;
import com.team100.kite_master.forum.ForumParser;
import com.team100.kite_master.forum.ForumPostFragment;
import com.team100.kite_master.forum.ForumPostListFragment;
import com.team100.kite_master.forum.forum_data_classes.DateUtil;
import com.team100.kite_master.forum.forum_data_classes.Post;
import com.team100.kite_master.networking.NetworkManager;
import com.team100.kite_master.networking.VolleyListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;



public class SearchFragment extends Fragment {


    EditText searchBox;
    ArrayList<Post> postList = new ArrayList<>();
    ArrayList<Post> resultList = new ArrayList<>();
    CustomAdapter searchAdapter;
    ListView postListView;
    ProgressBar loadingCircle;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);


        postListView = v.findViewById(R.id.list_view);
        loadingCircle = v.findViewById(R.id.topics_loading);


        loadingCircle.setVisibility(View.GONE);
        searchAdapter = new CustomAdapter();
        postListView.setAdapter(searchAdapter);

        searchBox = v.findViewById(R.id.search_box);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    System.out.println("SEARCHING");
                    return true;
                }
                return false;
            }
        });



        ((MainActivity) Objects.requireNonNull(getActivity())).setCurScreen("search");
        ((MainActivity) Objects.requireNonNull(getActivity())).setDrawerItemSelection(1);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Search");

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
            view = getLayoutInflater().inflate(R.layout.forum_post_list_card, null);
            // initialize text views
            TextView topicTitle = view.findViewById(R.id.text_title);
            TextView topicAuthor = view.findViewById(R.id.text_author);
            TextView topicTime = view.findViewById(R.id.text_time);
            // iterate through list to set topic entries
            topicTitle.setText(postList.get(i).getPostTitle());
            topicAuthor.setText(postList.get(i).getPostAuthor());
            DateUtil d = new DateUtil();
            String timeago = d.getTimeAgo(Long.parseLong(postList.get(i).getPostTime()));
            topicTime.setText(timeago);
            //add images here when support is added
            return view;
        }
    }





    //switch to post fragment when one is clicked
    public void openPost(String postID) {
        Fragment fragment = new ForumPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selected_post", postID);
        fragment.setArguments(bundle);
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack("tag");
        ft.commit();
    }





}