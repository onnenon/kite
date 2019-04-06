package com.team100.kite_master.messages.messages_data_classes;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team100.kite_master.R;
import com.team100.kite_master.messages.MessagesFragment;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketImplementation {

    private Activity WSactivity;
    private Context WScontext;

    private String username;

    private OkHttpClient client;
    private Request request;
    private WebSocket webSocket;

    private LinearLayout messageView;

    public WebSocketImplementation(String username, Activity WSactivity, Context WScontext,
                                   LinearLayout messageView, OkHttpClient client, Request request, WebSocket webSocket) {

        this.WSactivity = WSactivity;
        this.WScontext = WScontext;

        this.username = username;

        this.messageView = messageView;

        this.client = client;
        this.request = request;
        this.webSocket = webSocket;
    }

    // Helper methods for this class and outer classes
    public void sendJSONText(String TextString) {

        JsonObject JsonText = new JsonObject();

        JsonText.addProperty("username", username);
        JsonText.addProperty("text", TextString);

        webSocket.send(JsonText.toString());
    }

    public void receiveJSONText(String TextString) {

        JsonParser parser = new JsonParser();

        JsonObject JsonText = (JsonObject) parser.parse(TextString);

        JsonElement jsonUsername = JsonText.get("username");
        JsonElement jsonText = JsonText.get("text");

        String stringUsername = jsonUsername.getAsString();
        String stringText = jsonText.getAsString();

        output(stringUsername + ": " + stringText);
    }

    public void output(final String txt) {

        WSactivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                TextView text = new TextView(WScontext);

                text.setText(txt);

                messageView.addView(text);
            }
        });
    }

    // Getter and setter methods
    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public OkHttpClient getClient() {

        return this.client;
    }

    public okhttp3.Request getRequest() {

        return this.request;
    }

    /*

    public WebSocket getWebsocket() {

        return this.webSocket;
    }

    */
}
