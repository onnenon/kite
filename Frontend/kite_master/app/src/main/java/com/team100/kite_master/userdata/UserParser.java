package com.team100.kite_master.userdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class UserParser {

    //convert JSON object from backend to arraylist of topics
    public User parseUserInfo(JSONObject resp) throws JSONException {
        //get json array of user info
        JSONObject jinfo = resp.getJSONObject("data");
        User u = new User(
                jinfo.getString("username"),
                jinfo.getString("displayName"),
                jinfo.getString("bio"),
                jinfo.getInt("post_count"),
                jinfo.getBoolean("is_admin"),
                jinfo.getBoolean("is_mod")
        );
        return u;
    }
}
