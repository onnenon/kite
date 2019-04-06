package com.team100.kite_master.messages.messages_data_classes;

import android.widget.TextView;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Message {

    private int ID;
    private String username;
    private String text;
    private Date messageTime;

    public Message(String username, String text, int ID) {

        this.ID = ID;
        this.username = username;
        this.text = text;
        this.messageTime = Calendar.getInstance().getTime();
    }



    // Getter and setter methods
    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getText() {

        return this.text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public int getID() {

        return this.ID;
    }

    public void setID(int ID) {

        this.ID = ID;
    }

    public Date getMessageTime() {

        return this.messageTime;
    }
}
