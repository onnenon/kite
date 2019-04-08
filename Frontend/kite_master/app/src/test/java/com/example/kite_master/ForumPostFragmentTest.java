package com.example.kite_master;

import com.team100.kite_master.forum.ForumPostFragment;
import com.team100.kite_master.forum.forum_data_classes.Post;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForumPostFragmentTest {
    ForumPostFragment f;

    @Before
    public void setUp() {
        f = mock(ForumPostFragment.class);
    }


    @Test
    public void testParsePost1() throws JSONException {
        JSONObject postJson = new JSONObject();
        postJson.put("id", "1234");
        postJson.put("title", "boats are cool");
        postJson.put("body", "i like boats");
        postJson.put("author", "captain");
        postJson.put("edited", false);
        postJson.put("topic_name", "boats");
        postJson.put("date", "01/01/01");

        JSONObject mockdata = new JSONObject();
        mockdata.put("post", postJson);

        JSONObject mockJson = new JSONObject();
        mockJson.put("data", mockdata);

        Post mockPost = new Post("1234", "boats are cool", "i like boats", "captain", false, "boats", "01/01/01");

        when(f.getPost()).thenReturn(mockPost);
        when(f.parsePost(mockJson)).thenCallRealMethod();

        Post outP = f.parsePost(mockJson);

        Post correctPost = new Post("1234", "boats are cool", "i like boats", "captain", false, "boats", "01/01/01");

        assertEquals(outP.toString(), correctPost.toString());
    }

    @Test
    public void testParsePost2() throws JSONException {
        JSONObject postJson = new JSONObject();
        postJson.put("id", "11111");
        postJson.put("title", "i like trains");
        postJson.put("body", "trains are very fast");
        postJson.put("author", "conductor");
        postJson.put("edited", true);
        postJson.put("topic_name", "trains");
        postJson.put("date", "12/12/12");

        JSONObject mockdata = new JSONObject();
        mockdata.put("post", postJson);

        JSONObject mockJson = new JSONObject();
        mockJson.put("data", mockdata);

        Post mockPost = new Post("1234", "boats are cool", "i like boats", "captain", false, "boats", "01/01/01");

        when(f.getPost()).thenReturn(mockPost);

        when(f.parsePost(mockJson)).thenCallRealMethod();

        Post outP = f.parsePost(mockJson);

        Post correctPost = new Post("11111", "i like trains", "trains are very fast", "conductor", true, "trains", "12/12/12");

        assertEquals(outP.toString(), correctPost.toString());
    }


}
