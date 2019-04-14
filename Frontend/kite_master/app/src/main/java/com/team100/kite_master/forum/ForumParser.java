package com.team100.kite_master.forum;

import com.team100.kite_master.forum.forum_data_classes.Post;

import org.json.JSONException;
import org.json.JSONObject;

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

}
