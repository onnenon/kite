package com.team100.kite_master.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.team100.kite_master.MainActivity;
import com.team100.kite_master.R;
import com.team100.kite_master.messages.messages_data_classes.Message;

import java.util.Arrays;
import java.util.Objects;


public class MessagesFragment extends Fragment implements OutputHandler {

    private String LOCAL_IP_ADDRESS;
    private String[] userdata;
    private String username;

    private ScrollView scrollView;
    private LinearLayout messageList;

    private TextView errorTextView;
    private EditText messageText;
    private Button postButton;

    private WebSocketImplementation implementationWS;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final int DISTANCE_FROM_CLOSE_EDGE = 30;
        final int BLACK_COLOR = 0xff000000;

        View v = inflater.inflate(R.layout.messages_fragment, container, false);

        userdata = ((MainActivity) Objects.requireNonNull(getActivity())).currentUser.toArray();
        username = userdata[0];
        LOCAL_IP_ADDRESS = ((MainActivity) Objects.requireNonNull(getActivity())).getServerIP();

        //initialize user interface objects
        scrollView = (ScrollView) v.findViewById(R.id.message_scroll_view);
        messageList = (LinearLayout) v.findViewById(R.id.message_linear_layout);

        errorTextView = (TextView) v.findViewById(R.id.error_textView);

        messageText = (EditText) v.findViewById(R.id.message_edit_text);
        messageText.setBackgroundResource(R.drawable.message_edit_text_layout);
        messageText.setTextColor(BLACK_COLOR);
        messageText.setHintTextColor(BLACK_COLOR);
        messageText.setPadding(DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE);

        postButton = (Button) v.findViewById(R.id.message_button);
        postButton.setBackgroundResource(R.drawable.message_button_layout);
        postButton.setTextColor(BLACK_COLOR);
        postButton.setPadding(DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE);

        implementationWS = new WebSocketImplementation(this, username, LOCAL_IP_ADDRESS);

        //set on click listener
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageString = messageText.getText().toString();

                // Make sure the string isn't empty
                if (!messageString.equals("")) {

                    // Send the message
                    implementationWS.sendJSONText(messageString);

                    // Clear the message text
                    messageText.setText("");
                }
                else {

                    Toast.makeText(getActivity(), "Please enter a message" + " ", Toast.LENGTH_LONG).show();
                }
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
                String messageTime = msg.getMessageTime();
                String messageString =  msg.getUsername() + ": " + msg.getText();

                // Create a new LinearLayout object
                LinearLayout timeHolder = setupMessageHolder(username);
                LinearLayout messageHolder = setupMessageHolder(username);

                // Create a message, and set it up
                TextView time = setupTimeTextView(username, messageTime);
                RelativeLayout message = setupMessage(username, messageString);

                // Add the message to the Linearlayout
                timeHolder.addView(time);
                messageHolder.addView(message);

                // Add the messageHolder to the linearLayout
                messageList.addView(timeHolder);
                messageList.addView(messageHolder);

                // Credit to this source: https://stackoverflow.com/questions/21926644/get-height-and-width-of-a-layout-programmatically
                // Scroll to bottom upon receiving new messages
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

            }

        });

    }

    public void setErrorText(String errorText) {

        errorTextView.setText(errorText);
    }

    public LinearLayout setupMessageHolder(String username) {

        final int DISTANCE_FROM_CLOSE_EDGE = 30;
        final int DISTANCE_FROM_FAR_EDGE = 240;

        int width;
        int height;

        LinearLayout messageHolder;
        LinearLayout.LayoutParams layoutParams;

        messageHolder = new LinearLayout(getContext());

        width = LinearLayout.LayoutParams.MATCH_PARENT;
        // width = LinearLayout.LayoutParams.MATCH_PARENT;
        height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams = new LinearLayout.LayoutParams(width, height);

        // Position the messages that you yourself send to the right
        // Position the messages of other users to the left
        if (username == getUsername()) {

            messageHolder.setGravity(Gravity.RIGHT);
        }
        else {

            messageHolder.setGravity(Gravity.LEFT);
        }

        messageHolder.setLayoutParams(layoutParams);
        messageHolder.requestLayout();

        return messageHolder;
    }

    public RelativeLayout setupMessage(String username, String messageString) {

        RelativeLayout messageLayout;
        TextView messageText;

        messageText = setupMessageTextView(messageString);

        messageLayout = setupRelativeLayout(username);
        messageLayout.addView(messageText);

        return messageLayout;
    }

    public RelativeLayout setupRelativeLayout(String username) {

        final int DISTANCE_FROM_CLOSE_EDGE = 30;
        final int DISTANCE_FROM_FAR_EDGE = 240;

        int width;
        int height;

        TextView messageText;
        RelativeLayout.LayoutParams relativeParams;

        RelativeLayout messageLayout = new RelativeLayout(getContext());

        // Credit to this source: https://stackoverflow.com/questions/18844418/add-margin-programmatically-to-relativelayout
        // Set parameters of relativeLayout object
        width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        // width = RelativeLayout.LayoutParams.MATCH_PARENT;
        height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        relativeParams = new RelativeLayout.LayoutParams(width, height);
        relativeParams.setMargins(DISTANCE_FROM_CLOSE_EDGE, 0, DISTANCE_FROM_CLOSE_EDGE, 0);

        // Position the messages that you yourself send to the right
        // Position the messages of other users to the left
        if (username == getUsername()) {

            relativeParams.setMarginStart(DISTANCE_FROM_FAR_EDGE);
            relativeParams.setMarginEnd(DISTANCE_FROM_CLOSE_EDGE);
            // messageLayout.setGravity(Gravity.END);

            messageLayout.setBackgroundResource(R.drawable.message_layout_this_user);
        }
        else {

            relativeParams.setMarginStart(DISTANCE_FROM_CLOSE_EDGE);
            relativeParams.setMarginEnd(DISTANCE_FROM_FAR_EDGE);
            // messageLayout.setGravity(Gravity.START);

            messageLayout.setBackgroundResource(R.drawable.message_layout);
        }

        messageLayout.setLayoutParams(relativeParams);
        messageLayout.requestLayout();
        messageLayout.setGravity(Gravity.BOTTOM);

        return messageLayout;
    }

    public TextView setupTimeTextView(String username, String messageTime) {

        final int DISTANCE_FROM_CLOSE_EDGE = 30;
        final int DISTANCE_FROM_FAR_EDGE = 240;
        final int BLACK_COLOR = 0xff000000;

        int width;
        int height;

        TextView messageText;
        LinearLayout.LayoutParams layoutParams;

        messageText = new TextView(getContext());
        messageText.setText(messageTime);
        messageText.setTextColor(BLACK_COLOR);
        messageText.setPadding(DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, 0);

        width = LinearLayout.LayoutParams.WRAP_CONTENT;
        // width = LinearLayout.LayoutParams.MATCH_PARENT;
        height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams = new LinearLayout.LayoutParams(width, height);

        if (username == getUsername()) {

            layoutParams.setMarginStart(DISTANCE_FROM_FAR_EDGE);
            layoutParams.setMarginEnd(DISTANCE_FROM_CLOSE_EDGE);
            // messageText.setGravity(Gravity.END);
        }
        else {

            layoutParams.setMarginStart(DISTANCE_FROM_CLOSE_EDGE);
            layoutParams.setMarginEnd(DISTANCE_FROM_FAR_EDGE);
            // messageText.setGravity(Gravity.START);
        }

        messageText.setLayoutParams(layoutParams);
        messageText.setGravity(Gravity.TOP);

        return messageText;
    }

    public TextView setupMessageTextView(String messageString) {

        final int DISTANCE_FROM_CLOSE_EDGE = 30;
        final int BLACK_COLOR = 0xff000000;

        int width;
        int height;

        TextView messageText;
        LinearLayout.LayoutParams layoutParams;

        messageText = new TextView(getContext());
        messageText.setText(messageString);
        messageText.setTextColor(BLACK_COLOR);
        messageText.setPadding(DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE, DISTANCE_FROM_CLOSE_EDGE);

        width = LinearLayout.LayoutParams.WRAP_CONTENT;
        height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams = new LinearLayout.LayoutParams(width, height);

        messageText.setLayoutParams(layoutParams);

        return messageText;
    }



    // Getter and setter methods used for JUnit and Mockito testing
    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    /*

    public LinearLayout getMessageView() {

        return this.messageList;
    }

    public void setMessageView(LinearLayout messageView) {

        this.messageList = messageView;
    }

    */

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