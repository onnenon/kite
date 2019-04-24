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

}
