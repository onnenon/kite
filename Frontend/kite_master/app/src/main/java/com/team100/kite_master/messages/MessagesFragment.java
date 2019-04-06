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

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private KiteWebSocketListener KiteWSlistener;

    public String LOCAL_IP_ADDRESS;

    // Connection variables
    private OkHttpClient client;
    private okhttp3.Request request;
    private WebSocket websocket;

    private LinearLayout messageView;
    // private TextView statusText;
    private EditText messageText;
    private Button postButton;

    private String username;

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



        //set username
        username = "ANewUser";

        //set local ip for testing
        LOCAL_IP_ADDRESS = "10.0.1.2";

        //initialize user interface objects
        messageView = (LinearLayout) v.findViewById(R.id.message_layout);
        // statusText = (TextView) v.findViewById(R.id.message_status_text_view);
        messageText = (EditText) v.findViewById(R.id.message_edit_text);
        postButton = (Button) v.findViewById(R.id.message_button);

        //set on click listener
        //postButton.setOnClickListener(this);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KiteWSlistener.sendJSONText(messageText.getText().toString());
            }
        });

        //initialize websocket connection
        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build();
        request = new okhttp3.Request.Builder().url("http://chat.kite.onn.sh").build();
        // request = new okhttp3.Request.Builder().url("ws://echo.websocket.org").build();
        websocket = KiteWSlistener.getClient().newWebSocket(KiteWSlistener.getRequest(), KiteWSlistener);

        return v;
    }

    // ws://echo.websocket.org

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Messages");
    }

    // private void getTenRecentMessages() {}

}