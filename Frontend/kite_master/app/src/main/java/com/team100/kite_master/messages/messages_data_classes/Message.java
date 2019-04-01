package com.team100.kite_master.messages.messages_data_classes;

import android.widget.TextView;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Message {

    private int ID;
    private String username;
    private String message;
    private Date messageTime;

    public Message(String username, String message, int ID) {

        this.ID = ID;
        this.username = username;
        this.message = message;
        this.messageTime = Calendar.getInstance().getTime();
    }



    // Getter and setter methods
    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getMessage() {

        return this.message;
    }

    public void setMessage(String message) {

        this.message = message;
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
