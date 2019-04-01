package com.example.kite_master;

import com.team100.kite_master.messages.MessagesFragment;
import com.team100.kite_master.messages.messages_data_classes.Message;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class MessagesFragmentUnitTest {

    private MessagesFragment messageFragmentTest;
    private Message messageTest;

    @Before
    public void setup() {

        messageFragmentTest = mock(MessagesFragment.class);
        messageTest = mock(Message.class);
    }

    @Test
    public void foo() {


    }




}
