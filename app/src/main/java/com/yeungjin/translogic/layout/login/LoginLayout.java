package com.yeungjin.translogic.layout.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.find_account.FindAccountLayout;
import com.yeungjin.translogic.layout.main.MainLayout;
import com.yeungjin.translogic.layout.signup.SignupLayout;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.login.LoginRequest;

public class LoginLayout extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView findAccount;
    private Button login;
    private Button signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_login);
        setId();

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
                    LoginRequest request = new LoginRequest(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainLayout.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "아이디가 존재하지 않거나 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Request.queue = Volley.newRequestQueue(getApplicationContext());
                    Request.queue.add(request);
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

    private void setId() {
        username = (EditText) findViewById(R.id.layout_login_login__username);
        password = (EditText) findViewById(R.id.layout_login_login__password);
        findAccount = (TextView) findViewById(R.id.layout_login_login__find_account);
        login = (Button) findViewById(R.id.layout_login_login__login);
        signup = (Button) findViewById(R.id.layout_login_login__signup);
    }
}
