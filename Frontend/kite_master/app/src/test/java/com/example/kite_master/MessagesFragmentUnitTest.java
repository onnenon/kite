package com.example.kite_master;

import com.team100.kite_master.messages.MessagesFragment;
import com.team100.kite_master.messages.messages_data_classes.Message;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class MessagesFragmentUnitTest {

    private MessagesFragment messageFragmentTest;
    private Message messageTest;

    @Before
    public void setup() {

        messageFragmentTest = mock(MessagesFragment.class);
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

            messageTest.setMessage("A message for someone special.");
        }

        verify(messageTest, times(3)).setMessage("A message for someone special.");
    }

    @Test
    public void testMessageTime() {

        when(messageTest.getMessageTime()).thenReturn(Calendar.getInstance().getTime());

        System.out.println(messageTest.getMessageTime());

        assertEquals(Calendar.getInstance().getTime(), messageTest.getMessageTime());
    }






}
