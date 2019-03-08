package com.example.kitesocketcommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button stopButton;
    private EditText inputText;
    private Button sendButton;
    private TextView outputText;

    private OkHttpClient client;
    private Request request;
    WebSocket websocket;

    String username = "Username";

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private class KiteWebSocketListener extends WebSocketListener {

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        inputText = (EditText) findViewById(R.id.messageText);
        sendButton = (Button) findViewById(R.id.sendButton);
        outputText = (TextView) findViewById(R.id.outputText);

        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build();
        request = new Request.Builder().url("http://chat.kite.onn.sh").build();

        websocket = client.newWebSocket(request, new KiteWebSocketListener());

        /*

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build();
                request = new Request.Builder().url("http://chat.kite.onn.sh").build();
                websocket = client.newWebSocket(request, new KiteWebSocketListener());
            }
        });

        */

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.dispatcher().executorService().shutdown();

                websocket.close(NORMAL_CLOSURE_STATUS, null);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendJSONText(inputText.getText().toString());
            }
        });
    }

    private void sendJSONText(String TextString) {

        JSONObject JsonText = new JSONObject();

        try {

            JsonText.put("username", username);
            JsonText.put("text", TextString);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        websocket.send(JsonText.toString());
    }

    private void receiveJSONText(String TextString) {

        /*

        Gson parser = new Gson();

        JSONObject JsonText = TextString.;

        try {

            JsonText.getString("username");
            JsonText.getString("text");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        */

        // output(JsonText.toString());

        output(TextString);
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
