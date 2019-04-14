package com.team100.kite_master.forum;

import com.team100.kite_master.forum.forum_data_classes.Post;
import com.team100.kite_master.forum.forum_data_classes.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForumParser {

    //convert JSON object from backend to arraylist of topics
    public Post parsePost(JSONObject resp) throws JSONException {
        //get json array of posts
        JSONObject jdata = resp.getJSONObject("data");
        JSONObject jpost = jdata.getJSONObject("post");
        //create new post object from data
        Post po = new Post(jpost.getString("id"),
                jpost.getString("title"),
                jpost.getString("body"),
                jpost.getString("author"),
                jpost.getBoolean("edited"),
                jpost.getString("topic_name"),
                jpost.getString("date"));
        //hide loading circle
        return po;
    }


    //convert JSON object from backend to arraylist of topics
    public ArrayList<Topic> parseTopics(JSONObject resp) throws JSONException {
        //create output list
        ArrayList<Topic> tops = new ArrayList<Topic>();
        //get JSON array of topics
        JSONArray topics = resp.getJSONObject("data").getJSONArray("topics");
        //for each element in the array create a new topic object and add it to the array list
        for (int i = 0; i < topics.length(); i++) {
            Topic t = new Topic(topics.getJSONObject(i).getString("name"), topics.getJSONObject(i).getString("descript"));
            tops.add(t);
        }
        return tops;

    }

}
