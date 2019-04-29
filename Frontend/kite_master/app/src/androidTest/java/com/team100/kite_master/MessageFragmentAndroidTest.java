package com.team100.kite_master;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team100.kite_master.favorites.FavoriteStorageHandler;
import com.team100.kite_master.forum.forum_data_classes.Post;
import com.team100.kite_master.login.SaveSharedPreference;
import com.team100.kite_master.messages.MessagesFragment;
import com.team100.kite_master.messages.WebSocketImplementation;
import com.team100.kite_master.messages.messages_data_classes.Message;
import com.team100.kite_master.messages.messages_data_classes.MessageLayoutSetup;
import com.team100.kite_master.networking.NetworkManager;
import com.team100.kite_master.userdata.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MessageFragmentAndroidTest extends MainActivity {

    // Test variables
    private MessagesFragment messageFrag;
    private WebSocketImplementation impWS;
    private Message realMessage;
    private MessageLayoutSetup layoutSetup;

    private MessagesFragment mockMessageFrag;
    private WebSocketImplementation mockImpWS;
    private Message mockMessage;



    /*

    // Setup
    @Before
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Regular objects
        messageFrag = new MessagesFragment();
        messageFrag.setUsername("fadmin");
        messageFrag.setIPaddress("kite.onn.sh");

        impWS = new WebSocketImplementation(messageFrag, messageFrag.getUsername(), messageFrag.getIPaddress());

        realMessage = new Message("fadmin", "Text!");
        layoutSetup = new MessageLayoutSetup(messageFrag.getContext(), messageFrag.getUsername());
    }



    // MessageLayoutSetup class tests
    @Test
    public void MessageLayoutSetupInitializationTest() {

        messageFrag.setUsername("UserOne");

        layoutSetup = new MessageLayoutSetup(messageFrag.getContext(), messageFrag.getUsername());

        // Make sure that the instance variables of layoutSetup and messageFrag are the same.
        Assert.assertNotNull(messageFrag.getContext());
        Assert.assertNotNull(layoutSetup.getContext());
        assertEquals(layoutSetup.getContext(), messageFrag.getContext());
        assertEquals(layoutSetup.getUsername(), messageFrag.getUsername());
    }

    */

    /*

    @Test
    public void SetupMessageTextViewTest() {

        // layoutSetup = new MessageLayoutSetup(messageFrag.getContext(), messageFrag.getUsername());

        String message = "A very important message!";

        TextView textView = layoutSetup.setupMessageTextView(message); // WHY IS CONTEXT NULL???

        assertEquals(message, textView.getText()); // Check that the message is correct

    }

    @Test
    public void SetupTimeTextView() {

        TextView textView;

        String username = "UserOne";
        String timeText = "12:07 AM";

        textView = layoutSetup.setupTimeTextView(username, timeText);

        assertEquals(timeText, textView.getText()); // Check that the time is correct
    }

    @Test
    public void SetupRelativeLayoutTest() {

        RelativeLayout relativeLayout;

        String username = "UserOne";

        relativeLayout = layoutSetup.setupRelativeLayout(username);
    }

    */
}
