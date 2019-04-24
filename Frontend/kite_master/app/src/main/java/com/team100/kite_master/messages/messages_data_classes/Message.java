package com.team100.kite_master.messages.messages_data_classes;

import java.util.Calendar;
import java.util.Date;

public class Message {

    private int ID;
    private String username;
    private String text;
    private Calendar date;

    public Message(String username, String text) {

        this.ID = ID;
        this.username = username;
        this.text = text;
        this.date = Calendar.getInstance();
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

    public String getMessageTime() {

        // Initialize time variables
        int year;
        int month;
        int day;

        int hour;
        int minute_int;
        String minute_string;

        int meridiem_int;
        String meridiem_string;

        // Get the date information
        year = this.date.get(Calendar.YEAR);
        month = this.date.get(Calendar.MONTH) + 1;
        day = this.date.get(Calendar.DAY_OF_MONTH);

        // Get the time information
        hour = this.date.get(Calendar.HOUR);
        minute_int = this.date.get(Calendar.MINUTE);
        meridiem_int = this.date.get(Calendar.AM_PM);

        // Set the string for minutes
        if (minute_int < 10) {

            minute_string = "0" + minute_int;
        }
        else {

            minute_string = minute_int + "";
        }

        // Set the string for meridiem
        if (meridiem_int == 0) {

            meridiem_string = "AM";
        }
        else {

            meridiem_string = "PM";
        }

        return month + "/" + day + "/" + year + " " + hour + ":" + minute_string + " " + meridiem_string;
    }
}
