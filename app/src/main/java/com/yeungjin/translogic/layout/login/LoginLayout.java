package com.yeungjin.translogic.layout.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonActivity;
import com.yeungjin.translogic.layout.MainLayout;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.ThreadRequest;
import com.yeungjin.translogic.request.login.LoginRequest;
import com.yeungjin.translogic.request.login.GetSession;
import com.yeungjin.translogic.utility.DateFormat;
import com.yeungjin.translogic.utility.Session;

import org.json.JSONObject;

public class LoginLayout extends CommonActivity {
    private EditText username;
    private EditText password;
    private TextView findAccount;
    private Button login;
    private Button signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_login);
        init();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Session.width = metrics.widthPixels;
        Session.height = metrics.heightPixels;
    }

    @Override
    protected void setId() {
        username = findViewById(R.id.layout_login_login__username);
        password = findViewById(R.id.layout_login_login__password);
        findAccount = findViewById(R.id.layout_login_login__find_account);
        login = findViewById(R.id.layout_login_login__login);
        signup = findViewById(R.id.layout_login_login__signup);
    }

    @Override
    protected void setListener() {
        findAccount.setOnClickListener(new View.OnClickListener() {
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
                    Request request = new LoginRequest(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                                ThreadRequest request = new GetSession(_username);
                                request.start();
                                try {
                                    request.join();

                                    JSONObject http = new JSONObject(request.getResponse());
                                    JSONObject object = http.getJSONObject("user");

                                    Session.user.EMPLOYEE_NUMBER = object.getLong("EMPLOYEE_NUMBER");
                                    Session.user.EMPLOYEE_NAME = object.getString("EMPLOYEE_NAME");
                                    Session.user.EMPLOYEE_USERNAME = object.getString("EMPLOYEE_USERNAME");
                                    Session.user.EMPLOYEE_PASSWORD = object.getString("EMPLOYEE_PASSWORD");
                                    Session.user.EMPLOYEE_CONTACT_NUMBER = object.getString("EMPLOYEE_CONTACT_NUMBER");
                                    Session.user.EMPLOYEE_EMAIL = object.getString("EMPLOYEE_EMAIL");
                                    Session.user.EMPLOYEE_COMPANY_NUMBER = object.getLong("EMPLOYEE_COMPANY_NUMBER");
                                    Session.user.EMPLOYEE_DEGREE = object.getString("EMPLOYEE_DEGREE");
                                    Session.user.EMPLOYEE_IMAGE = object.getString("EMPLOYEE_IMAGE");
                                    Session.user.EMPLOYEE_REGISTER_DATE = DateFormat.DATE.parse(object.getString("EMPLOYEE_REGISTER_DATE"));
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
                    Request.sendRequest(getApplicationContext(), request);
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
}
