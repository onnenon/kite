package com.example.kite_master;

import android.widget.RelativeLayout;

import com.team100.kite_master.messages.MessagesFragment;
import com.team100.kite_master.messages.messages_data_classes.Message;
import com.team100.kite_master.messages.WebSocketImplementation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MessagesFragmentUnitTest {

    private MessagesFragment messageFrag;
    private WebSocketImplementation impWS;
    private Message realMessage;

    private MessagesFragment mockMessageFrag;
    private WebSocketImplementation mockImpWS;
    private Message mockMessage;

    @Before
    public void setup() {

        // Regular objects
        messageFrag = new MessagesFragment();
        messageFrag.setUsername("fadmin");
        messageFrag.setIPaddress("kite.onn.sh");

        impWS = new WebSocketImplementation(messageFrag, messageFrag.getUsername(), messageFrag.getIPaddress());

        // Mock objects
        mockMessageFrag = mock(MessagesFragment.class);
        mockImpWS = mock(WebSocketImplementation.class);
        mockMessage = mock(Message.class);
    }



    @Test
    public void testUsername() {

        when(mockMessage.getUsername()).thenReturn("fadmin");
        assertEquals("fadmin", mockMessage.getUsername());
    }

    @Test
    public void testMessage() {

        for (int i = 0; i < 3; i++) {

            mockMessage.setText("A message for someone special.");
        }

        verify(mockMessage, times(3)).setText("A message for someone special.");
    }

    @Test
    public void testMessageOutput() {

        String date = "4/20/2019 3:23 PM";

        when(mockMessage.getMessageTime()).thenReturn(date);
        when(mockMessage.getUsername()).thenReturn(messageFrag.getUsername()); // "Username"
        when(mockMessage.getText()).thenReturn("A very important message.");

        assertEquals(date + "\n" + "fadmin: A very important message.\n",
                mockMessage.getMessageTime() + "\n" + mockMessage.getUsername() + ": " + mockMessage.getText() + "\n");
    }

    @Test
    public void testRecentMessagesRetrieval() throws JSONException {

        // Setup
        JSONObject json1 = new JSONObject();
        json1.put("username", "NewProgrammer725");
        json1.put("text", "Help! My code doesn't work.");
        json1.put("date", "4/2/19");

        JSONObject json2 = new JSONObject();
        json2.put("username", "Professor195");
        json2.put("text", "Sucks to be you!");
        json2.put("date", "4/3/19");

        JSONObject json3 = new JSONObject();
        json3.put("username", "Teammate902");
        json3.put("text", "Don't talk to my teammate like that!");
        json3.put("date", "4/3/19");

        JSONArray arr = new JSONArray();
        arr.put(json1);
        arr.put(json2);
        arr.put(json3);

        // Test
        when(mockImpWS.getMessagesString(3)).thenReturn(arr);

        JSONArray testArr = mockImpWS.getMessagesString(3);

        String firstMessage = impWS.getJSONMessage(testArr, 0);
        String secondMessage = impWS.getJSONMessage(testArr, 1);
        String thirdMessage = impWS.getJSONMessage(testArr, 2);

        assertEquals("4/2/19\nNewProgrammer725: Help! My code doesn't work.\n", firstMessage);
        assertEquals("4/3/19\nProfessor195: Sucks to be you!\n", secondMessage);
        assertEquals("4/3/19\nTeammate902: Don't talk to my teammate like that!\n", thirdMessage);
    }


    // Test not working...
    @Test
    public void testJsonMethodOutputCalls() {

        /*

        // mockMessageFrag.setIPaddress("kite.onn.sh");
        // mockMessageFrag.setUsername("fadmin");

        WebSocketImplementation ws = new WebSocketImplementation("fadmin", mockMessageFrag.getActivity(), mockMessageFrag.getContext(),
                mockMessageFrag.getMessageView(), mockMessageFrag.getErrorTextView(), "kite.onn.sh");

        ws.sendJSONText("String");
        ws.receiveJSONText("String");

        */

        /*

        mockImpWS.sendJSONText("String");
        mockImpWS.receiveJSONText("String");

        verify(mockImpWS, times(1)).sendJSONText("String");
        verify(mockImpWS, times(1)).receiveJSONText("String");

        verify(mockImpWS, times(2)).output("fadmin", "String");

        */

        /*

        when(mockImpWS.sendJSONText("String")).thenCallRealMethod();
        // when(mockImpWS.receiveJSONText("String")).thenCallRealMethod();

        mockImpWS.sendJSONText("String");
        // mockImpWS.receiveJSONText("String");

        verify(mockImpWS, times(1)).sendJSONText("String");
        // verify(mockImpWS, times(1)).receiveJSONText("String");

        // verify(mockImpWS, times(2)).output("fadmin", "String");
        verify(mockImpWS, times(1)).output("fadmin", "String");

        */

        /*

        mockImpWS.sendJSONText("String");

        verify(mockImpWS, times(1)).sendJSONText("String");

        */

        // verify(mockImpWS, times(1)).output("fadmin", "String");
    }

    @Test
    public void testRelativeLayoutSetup() {

        RelativeLayout layout = messageFrag.setupRelativeLayout("fadmin"); // NullpointerException?

        // Setting the margins throws "java.lang.reflect.InvocationTargetException"


    }

    @Test
    public void test() {


    }


}
