package com.team100.kite_master.messages.messages_data_classes;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team100.kite_master.messages.MessagesFragment;

import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class KiteWebSocketListener extends WebSocketListener {

    /*

    // Connection Variables
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    public KiteWebSocketListener() {


    }

    // Networking functionality
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
    public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
        output("Error : " + t.getMessage());
    }

    */
}
