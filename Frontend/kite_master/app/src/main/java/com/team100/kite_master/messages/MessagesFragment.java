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
import android.widget.TextView;

import com.team100.kite_master.R;
import com.team100.kite_master.messages.messages_data_classes.Message;
import com.team100.kite_master.messages.messages_data_classes.WebSocketImplementation;

import java.util.Arrays;
import java.util.Objects;


public class MessagesFragment extends Fragment implements OutputHandler {

    private String LOCAL_IP_ADDRESS;
    private String[] userdata;
    private String username;

    private LinearLayout messageView;
    private TextView errorTextView;
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
        errorTextView = (TextView) v.findViewById(R.id.error_textView);
        messageText = (EditText) v.findViewById(R.id.message_edit_text);
        postButton = (Button) v.findViewById(R.id.message_button);

        implementationWS = new WebSocketImplementation(this, username, LOCAL_IP_ADDRESS);

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

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Messages");
    }

    public void output(final String username, final String txt) {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Message msg = new Message(username, txt);
                String messageString = msg.getMessageTime() + "\n" + msg.getUsername() + ": " + msg.getText() + "\n";

                TextView text = new TextView(getContext());
                text.setText(messageString);
                messageView.addView(text);
            }
        });
    }

    public void setErrorText(String errorText) {

        errorTextView.setText(errorText);
    }



    // Getter and setter methods used for JUnit and Mockito testing
    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public LinearLayout getMessageView() {

        return this.messageView;
    }

    public void setMessageView(LinearLayout messageView) {

        this.messageView = messageView;
    }

    public String getIPaddress() {

        return this.LOCAL_IP_ADDRESS;
    }

    public void setIPaddress(String LOCAL_IP_ADDRESS) {

        this.LOCAL_IP_ADDRESS = LOCAL_IP_ADDRESS;
    }

    public TextView getErrorTextView() {

        return this.errorTextView;
    }

    public View getView() {

        return this.getView();
    }

    public WebSocketImplementation getWebSocketImplementation() {

        return this.implementationWS;
    }
}