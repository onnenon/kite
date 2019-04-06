package com.example.kite_master;

import com.team100.kite_master.messages.MessagesFragment;
import com.team100.kite_master.messages.messages_data_classes.Message;
import com.team100.kite_master.messages.messages_data_classes.WebSocketImplementation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MessagesFragmentUnitTest {

    private MessagesFragment messageFrag;
    private WebSocketImplementation impWS;

    private WebSocketImplementation ImplementationWS;
    private Message messageTest;

    @Before
    public void setup() {

        messageFrag = new MessagesFragment();
        impWS = new WebSocketImplementation("Username", messageFrag.getActivity(), messageFrag.getContext(), messageFrag.messageView);

        ImplementationWS = mock(WebSocketImplementation.class);
        messageTest = mock(Message.class);
    }

    @Test
    public void testMessageID() {

        when(messageTest.getID()).thenReturn(1111);
        assertEquals(1111, messageTest.getID());
    }

    @Test
    public void testUsername() {

        when(messageTest.getUsername()).thenReturn("fadmin");
        assertEquals("fadmin", messageTest.getUsername());
    }

    @Test
    public void testMessage() {

        for (int i = 0; i < 3; i++) {

            messageTest.setText("A message for someone special.");
        }

        verify(messageTest, times(3)).setText("A message for someone special.");
    }

    /*

    @Test
    public void testMessageTime() {

        when(messageTest.getMessageTime()).thenReturn(Calendar.getInstance().getTime());

        System.out.println(messageTest.getMessageTime());

        assertEquals(Calendar.getInstance().getTime(), messageTest.getMessageTime());
    }

    */





    @Test
    public void testRecentMessagesRetrieval() throws JSONException {

        // Setup
        JSONObject json1 = new JSONObject();
        json1.put("username", "NewProgrammer725");
        json1.put("text", "Help! My code doesn't work.");

        JSONObject json2 = new JSONObject();
        json2.put("username", "Professor195");
        json2.put("text", "Sucks to be you!");

        JSONObject json3 = new JSONObject();
        json3.put("username", "Teammate902");
        json3.put("text", "Don't talk to my teammate like that!");

        JSONArray arr = new JSONArray();
        arr.put(json1);
        arr.put(json2);
        arr.put(json3);


        // Test
        when(ImplementationWS.getMessagesString(3)).thenReturn(arr);

        JSONArray testArr = ImplementationWS.getMessagesString(3);

        String firstMessage = impWS.getMessage(testArr, 0);
        String secondMessage = impWS.getMessage(testArr, 1);
        String thirdMessage = impWS.getMessage(testArr, 2);

        assertEquals("NewProgrammer725: Help! My code doesn't work.", firstMessage);
        assertEquals("Professor195: Sucks to be you!", secondMessage);
        assertEquals("Teammate902: Don't talk to my teammate like that!", thirdMessage);
    }

    // Mock the return value of the address
    // Mock the return value of HTTP request?





}
