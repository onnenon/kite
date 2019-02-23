package com.example.websocketcommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {


    private class EchoWebSocketListener extends WebSocketListener {

        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello");
            webSocket.send("WebSocket communication!");

            webSocket.close(NORMAL_CLOSURE_STATUS, "Bye!");

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
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
    }
}
