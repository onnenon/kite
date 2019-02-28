package com.example.kitesocketcommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private WebSocket socket;

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private class EchoWebSocketListener extends WebSocketListener {

        String username = "Username";

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send(username + " has joined the chat");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {

            webSocket.send(username + " has left the chat");
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
        client = new OkHttpClient();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket = start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socket != null) {

                    socket.send(inputText.getText().toString());
                }
            }
        });
    }

    private WebSocket start() {

        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);

        return ws;
    }

    private void stop() {

        client.dispatcher().executorService().shutdown();

        System.exit(0);
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
