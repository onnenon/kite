package com.team100.kite_master.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.team100.kite_master.R;
import com.team100.kite_master.messages.messages_data_classes.WebSocketImplementation;

import java.util.Arrays;
import java.util.Objects;


public class MessagesFragment extends Fragment {

    private String LOCAL_IP_ADDRESS;
    private String[] userdata;
    private String username;

    public LinearLayout messageView;
    private EditText messageText;
    private Button postButton;

    private WebSocketImplementation implementationWS;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.messages_fragment, container, false);

        //receive bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userdata = bundle.getStringArray("userData");
            username = userdata[0]; // Get the username of the user
            LOCAL_IP_ADDRESS = bundle.getString("serverIP");
            System.out.println("USER DATA:");
            System.out.println(Arrays.toString(userdata));
        }

        //initialize user interface objects
        messageView = (LinearLayout) v.findViewById(R.id.message_layout);
        // statusText = (TextView) v.findViewById(R.id.message_status_text_view);
        messageText = (EditText) v.findViewById(R.id.message_edit_text);
        postButton = (Button) v.findViewById(R.id.message_button);

        implementationWS = new WebSocketImplementation(username, getActivity(), getContext(), messageView, LOCAL_IP_ADDRESS);

        //set on click listener
        //postButton.setOnClickListener(this);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Send the message
                implementationWS.sendJSONText(messageText.getText().toString());

                // Clear the message text
                messageText.setText("");
            }
        });

        bundle.getString("serverIP");

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Messages");
    }

    // private void getTenRecentMessages() {}

}