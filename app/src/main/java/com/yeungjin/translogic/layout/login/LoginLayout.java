package com.yeungjin.translogic.layout.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonActivity;
import com.yeungjin.translogic.layout.MainLayout;
import com.yeungjin.translogic.object.view.EMPLOYEE_INFO;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginLayout extends CommonActivity {
    private EditText username;
    private EditText password;
    private TextView find_account;
    private CheckBox auto_login;
    private Button login;
    private TextView signup;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_login);
        init();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Session.width = metrics.widthPixels;
        Session.height = metrics.heightPixels;

        preferences = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);

        if (getIntent().getBooleanExtra("logout", false)) {
            preferences.edit().clear().apply();
        } else {
            String _username = preferences.getString("username", null);
            String _password = preferences.getString("password", null);

            if (_username != null && _password != null) {
                login(_username, _password);
            }
        }
    }

    @Override
    protected void setId() {
        username = findViewById(R.id.layout_login_login__username);
        password = findViewById(R.id.layout_login_login__password);
        find_account = findViewById(R.id.layout_login_login__find_account);
        auto_login = findViewById(R.id.layout_login_login__auto_login);
        login = findViewById(R.id.layout_login_login__login);
        signup = findViewById(R.id.layout_login_login__signup);
    }

    @Override
    protected void setListener() {
        find_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindAccountLayout.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _username = username.getText().toString();
                String _password = password.getText().toString();

                if (_username.isEmpty() || _password.isEmpty()) {
                    if (_username.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        username.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                    }
                } else {
                    if (auto_login.isChecked()) {
                        SharedPreferences.Editor editor = preferences.edit();

                        if (preferences.getString("username", null) != null || preferences.getString("password", null) != null) {
                            editor.clear().apply();
                        }

                        editor.putString("username", username.getText().toString());
                        editor.putString("password", password.getText().toString());

                        editor.apply();
                    }
                    login(_username, _password);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupLayout.class);
                startActivity(intent);
            }
        });
    }

    private void login(String username, String password) {
        new DBVolley(getApplicationContext(), "Login", new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
        }}, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                    try {
                        JSONObject object = new JSONObject(new DBThread("GetSession", new HashMap<String, Object>() {{
                            put("username", username);
                        }}).getResponse());
                        Session.USER = Json.from(object, EMPLOYEE_INFO.class);
                        Session.socket.emit("login", Session.USER.EMPLOYEE_NUMBER);

                        String[] chat_numbers = new DBThread("GetJoinedChat", new HashMap<String, Object>() {{
                            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                        }}).getResponse().split(",");
                        for (String chat_number : chat_numbers) {
                            Session.socket.emit("joined_chat", Long.parseLong(chat_number.trim()));
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), MainLayout.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "아이디가 존재하지 않거나 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
