package com.example.kite_master;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team100.kite_master.MainActivity;
import com.team100.kite_master.R;
import com.team100.kite_master.forum.ForumPostFragment;
import com.team100.kite_master.forum.ForumTopicListFragment;
import com.team100.kite_master.forum.forum_data_classes.Post;
import com.team100.kite_master.userdata.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForumTopicListTest {
    ForumTopicListFragment f;
    Activity a;
    User josh;


    @Before
    public void setUp() {
        f = mock(ForumTopicListFragment.class);
        a = mock(MainActivity.class);
        josh = new User("josh", "Josh Berg", "I am josh", 12, true, true);
    }


    @Test
    public void testParseUserInfo() throws JSONException {

        JSONObject postJson = new JSONObject();
        postJson.put("username", "josh");
        postJson.put("is_admin", true);
        postJson.put("is_mod", true);
        postJson.put("post_count", 12);
        postJson.put("bio", "I am josh");
        postJson.put("displayName", "Josh Berg");

        JSONObject mockdata = new JSONObject();
        mockdata.put("data", postJson);

        when(f.getsingleUser()).thenReturn(josh);
        when(f.parseUserInfo(mockdata)).thenCallRealMethod();

        f.parseUserInfo(mockdata);

        assertEquals(f.parseUserInfo(mockdata).getUsername(), "josh");

    }
}


