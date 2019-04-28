package com.team100.kite_master;

import android.widget.TextView;

import com.team100.kite_master.messages.MessagesFragment;
import com.team100.kite_master.messages.WebSocketImplementation;
import com.team100.kite_master.messages.messages_data_classes.Message;
import com.team100.kite_master.messages.messages_data_classes.MessageLayoutSetup;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageFragmentAndroidTest {

    private MessagesFragment messageFrag;
    private WebSocketImplementation impWS;
    private Message realMessage;
    private MessageLayoutSetup layoutSetup;

    private MessagesFragment mockMessageFrag;
    private WebSocketImplementation mockImpWS;
    private Message mockMessage;



    // Setup
    @Before
    public void setup() {

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
        assertEquals(layoutSetup.getContext(), messageFrag.getContext());
        assertEquals(layoutSetup.getUsername(), messageFrag.getUsername());
    }

    @Test
    public void SetupMessageTextViewTest() {

        String message = "A very important message!";

        TextView textView = layoutSetup.setupMessageTextView(message);

        assertEquals(message, textView.getText()); // Check that the message is correct

    }
}
