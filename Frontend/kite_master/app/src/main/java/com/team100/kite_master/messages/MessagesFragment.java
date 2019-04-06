package com.team100.kite_master.messages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.team100.kite_master.R;
import com.team100.kite_master.messages.messages_data_classes.KiteWebSocketListener;
import com.team100.kite_master.messages.messages_data_classes.WebSocketImplementation;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class MessagesFragment extends Fragment {

    public String LOCAL_IP_ADDRESS;

    private LinearLayout messageView;
    private EditText messageText;
    private Button postButton;

    private WebSocketImplementation implementationWS;

    private OkHttpClient client;
    private Request request;
    private WebSocket webSocket;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.messages_fragment, container, false);

        /*

        //receive bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            topic = bundle.getString("selectedTopic"); //TODO
        }

        */

        //set local ip for testing
        LOCAL_IP_ADDRESS = "10.0.1.2";

        //initialize user interface objects
        messageView = (LinearLayout) v.findViewById(R.id.message_layout);
        // statusText = (TextView) v.findViewById(R.id.message_status_text_view);
        messageText = (EditText) v.findViewById(R.id.message_edit_text);
        postButton = (Button) v.findViewById(R.id.message_button);



        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build();
        // this.request = new okhttp3.Request.Builder().url("http://chat.kite.onn.sh").build();
        request = new okhttp3.Request.Builder().url("ws://echo.websocket.org").build();
        webSocket = client.newWebSocket(request, new KiteWebSocketListener());



        implementationWS = new WebSocketImplementation("ANewUser", getActivity(), getContext(), messageView, client, request, webSocket);



        //set on click listener
        //postButton.setOnClickListener(this);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                implementationWS.sendJSONText(messageText.getText().toString());
            }
        });

        return v;
    }

    // ws://echo.websocket.org

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Messages");
    }

    private class KiteWebSocketListener extends WebSocketListener {

        private static final int NORMAL_CLOSURE_STATUS = 1000;

        // Networking functionality
        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            implementationWS.sendJSONText(implementationWS.getUsername() + " has joined the chat");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {

            implementationWS.receiveJSONText(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {

            implementationWS.sendJSONText(implementationWS.getUsername() + " has left the chat");
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            implementationWS.output("Error : " + t.getMessage());
        }
    }

    // private void getTenRecentMessages() {}

}