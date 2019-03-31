package com.team100.kite_master.messages.messages_data_classes;

import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class KiteWebSocketListener extends WebSocketListener {

    private static final int NORMAL_CLOSURE_STATUS = 1000;



    private String username;
    private String statusText;

    public KiteWebSocketListener(String username, String statusText) {

        this.username = username;
        this.statusText = statusText;
    }


    
    // Networking functionality
    @Override
    public void onOpen(WebSocket webSocket, okhttp3.Response response) {

        sendJSONText(username + " has joined the chat");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {

        receiveJSONText(text);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {

        sendJSONText(username + " has left the chat");
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
        output("Error : " + t.getMessage());
    }



    // Getter and setter methods
    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getStatusText() {

        return statusText;
    }

    public void setStatusText(String statusText) {

        this.statusText = statusText;
    }



    // Helper methods for this class and outer classes
    public void sendJSONText(String TextString) {

        JsonObject JsonText = new JsonObject();

        JsonText.addProperty("username", username);
        JsonText.addProperty("text", TextString);

        websocket.send(JsonText.toString());
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

        new Thread() {

            @Override
            public void run() {

                setStatusText(txt);
            }
        }.run();

        /*

        new Thread() {

            @Override
            public void run() {

                TextView text = new TextView(getContext());

                text.setText(txt);

                messageView.addView(text);

                statusText.setText(txt);
            }
        }.run();

        */

        /*

        //android.app.Activity.runOnUiThread(new Runnable() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                TextView text = new TextView(getContext());

                text.setText(txt);

                messageView.addView(text);


                // outputText.setText(outputText.getText().toString() + "\n" + txt);
            }
        });

        */
    }
}
