package com.team100.kite_master.forum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.team100.kite_master.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ForumFragment extends Fragment {

    ListView listView;
    ArrayList<Topic> topicList = new ArrayList<Topic>();
    private RequestQueue volleyqueue;
    private ForumHttp forumHttp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forum, container, false);

        listView = v.findViewById(R.id.list_view);

        //here call to get the list of posts and add it to topicList
        topicList.add(new Topic("tHe FirsT one", "The first entry"));

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Forum");
        //initialize volley queue
        volleyqueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        forumHttp = new ForumHttp(volleyqueue);
    }

    //custom adapter class
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return topicList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.topic_list_item, null);

            TextView topicTitle = (TextView) view.findViewById(R.id.text_title);
            TextView topicDescription = (TextView) view.findViewById(R.id.text_description);

            topicTitle.setText(topicList.get(i).getName());
            topicDescription.setText(topicList.get(i).getDescription());

            getTopics();

            return view;

        }
    }


    public void getTopics() {

        forumHttp.getAllTopics();
        System.out.println("topics gotten");
    }


    public void toastMessage(String message) {
        Toast.makeText(getActivity(), message + " ", Toast.LENGTH_LONG).show();
    }


}