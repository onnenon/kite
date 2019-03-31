package com.team100.kite_master.messages.messages_data_classes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class KiteWebSocketListener {

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    // Connection variables
    private OkHttpClient client;
    private Request request;
    private WebSocket websocket;

    private String username;



    public KiteWebSocketListener() {

        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build();
        request = new Request.Builder().url("http://chat.kite.onn.sh").build();

        websocket = client.newWebSocket(request, new KiteWebSocketListener());
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

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
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        output("Error : " + t.getMessage());
    }

    private void sendJSONText(String TextString) {

        JsonObject JsonText = new JsonObject();

        JsonText.addProperty("username", username);
        JsonText.addProperty("text", TextString);

        websocket.send(JsonText.toString());
    }

    private void receiveJSONText(String TextString) {

        JsonParser parser = new JsonParser();

        JsonObject JsonText = (JsonObject) parser.parse(TextString);

        JsonElement jsonUsername = JsonText.get("username");
        JsonElement jsonText = JsonText.get("text");

        String stringUsername = jsonUsername.getAsString();
        String stringText = jsonText.getAsString();

        output(stringUsername + ": " + stringText);
    }

    private void output(final String txt) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                outputText.setText(outputText.getText().toString() + "\n" + txt);
            }
        });
    }
}
