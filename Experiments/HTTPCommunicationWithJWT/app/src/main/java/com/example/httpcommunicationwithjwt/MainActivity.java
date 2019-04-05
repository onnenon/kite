package com.example.httpcommunicationwithjwt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button SignInButton;

    private TextView LoginJWT;
    private TextView UserInfo;

    private EditText EnterUsername;
    private EditText EnterPassword;
    private EditText EnterBio;

    private Switch AdminBool;
    private Switch ModerBool;

    private Button SetPassword;
    private Button SetBio;
    private Button SetModer;
    private Button SetAdmin;
    private Button SetAll;
    private Button DeleteUser;
    private Button GetUserInfo;

    private HTTPInterface Implementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Implementation = new HTTPImplementation(getApplication(), this, "http://kite.onn.sh/api/v3/users");

        SignInButton = (Button) findViewById(R.id.SignIn);

        LoginJWT = (TextView) findViewById(R.id.LoginStatus);
        UserInfo = (TextView) findViewById(R.id.UserInfo);

        EnterUsername = (EditText) findViewById(R.id.EnterUsername);
        EnterPassword = (EditText) findViewById(R.id.EnterPassword);
        EnterBio = (EditText) findViewById(R.id.EnterBio);
        AdminBool = (Switch) findViewById(R.id.AdminBool);
        ModerBool = (Switch) findViewById(R.id.ModerBool);

        SetPassword = (Button) findViewById(R.id.SetPassword);
        SetBio = (Button) findViewById(R.id.SetBio);
        SetModer = (Button) findViewById(R.id.SetModer);
        SetAdmin = (Button) findViewById(R.id.SetAdmin);
        SetAll = (Button) findViewById(R.id.SetAll);
        DeleteUser = (Button) findViewById(R.id.DeleteUser);
        GetUserInfo = (Button) findViewById(R.id.GetUserInfo);

        SignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username = "fadmin";
                String password = "password";

                Implementation.login(username, password);
            }
        });

        GetUserInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = EnterUsername.getText().toString();

                String InfoString = Implementation.getUserInfo(userName);

                UserInfo.setText(InfoString);
            }
        });

        SetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = EnterUsername.getText().toString();
                String password = EnterPassword.getText().toString();

                Implementation.setPassword(userName, password);
            }
        });

        SetBio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = EnterUsername.getText().toString();
                String bio = EnterBio.getText().toString();

                Implementation.setBio(userName, bio);
            }
        });

        SetModer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = EnterUsername.getText().toString();
                boolean isModer = ModerBool.isChecked();

                Implementation.setModeratorStatus(userName, isModer);
            }
        });

        SetAdmin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = EnterUsername.getText().toString();
                boolean isAdmin = AdminBool.isChecked();

                Implementation.setAdministratorStatus(userName, isAdmin);
            }
        });

        SetAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = EnterUsername.getText().toString();
                String password = EnterPassword.getText().toString();
                String bio = EnterBio.getText().toString();
                boolean isModer = ModerBool.isChecked();
                boolean isAdmin = AdminBool.isChecked();

                Implementation.setAll(userName, password, bio, isModer, isAdmin);
            }
        });

        DeleteUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userName = EnterUsername.getText().toString();

                Implementation.deleteUser(userName);
            }
        });
    }
}