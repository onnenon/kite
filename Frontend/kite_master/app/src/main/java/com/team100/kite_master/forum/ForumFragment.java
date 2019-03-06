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

import com.team100.kite_master.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ForumFragment extends Fragment {


    ListView listView;
    List alist = new ArrayList();
    ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forum, container, false);

        listView = (ListView) v.findViewById(R.id.list_view);

        alist.add("one");
        alist.add("two");
        alist.add("three");
        alist.add("four");
        alist.add("five");
        alist.add("six");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");
        alist.add("seven");


        CustomAdapter customAdapter = new CustomAdapter();

        adapter = new ArrayAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, alist);
        listView.setAdapter(customAdapter);


        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Forum");
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return alist.size(); //TODO
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

            topicTitle.setText(alist.get(i).toString());
            topicDescription.setText(alist.get(i).toString());

            return view;

        }

    }


}