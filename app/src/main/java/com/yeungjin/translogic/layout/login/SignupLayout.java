package com.yeungjin.translogic.layout.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonActivity;
import com.yeungjin.translogic.utility.DBVolley;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;

public class SignupLayout extends CommonActivity {
    private ScrollView information;
    private EditText name;
    private EditText username;
    private EditText password;
    private EditText password_confirm;
    private EditText contact_number;
    private EditText email;
    private EditText company;
    private TextView username_note;
    private TextView password_note;
    private Button submit;
    private Button cancel;

    private boolean is_unique;
    private boolean is_same;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_signup);
        init();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                Session.height * 5 / 10);
        params.addRule(RelativeLayout.BELOW, R.id.layout_login_signup__TITLE);

        information.setLayoutParams(params);
    }

    @Override
    protected void setId() {
        information = findViewById(R.id.layout_login_signup__information);
        name = findViewById(R.id.layout_login_signup__name);
        username = findViewById(R.id.layout_login_signup__username);
        password = findViewById(R.id.layout_login_signup__password);
        password_confirm = findViewById(R.id.layout_login_signup__password_confirm);
        contact_number = findViewById(R.id.layout_login_signup__contact_number);
        email = findViewById(R.id.layout_login_signup__email);
        company = findViewById(R.id.layout_login_signup__company);
        username_note = findViewById(R.id.layout_login_signup__username_note);
        password_note = findViewById(R.id.layout_login_signup__password_note);
        submit = findViewById(R.id.layout_login_signup__submit);
        cancel = findViewById(R.id.layout_login_signup__cancel);
    }

    @Override
    protected void setListener() {
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !username.getText().toString().isEmpty()) {
                    if (username_note.getVisibility() == View.GONE) {
                        username_note.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 0;
                        username.setLayoutParams(params);
                    }

                    new DBVolley(getApplicationContext(), "IsUsernameUnique", new HashMap<String, Object>() {{
                        put("username", username);
                    }}, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                username_note.setTextColor(getColor(R.color.green));
                                username_note.setText("✓ 사용 가능한 아이디입니다.");
                                is_unique = true;
                            } else {
                                username_note.setTextColor(getColor(R.color.red));
                                username_note.setText("✕ 이미 사용중인 아이디입니다.");
                                is_unique = false;
                            }
                        }
                    });
                }
            }
        });
        password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence content, int start, int before, int count) {
                String _password = password.getText().toString();
                String _password_confirm = content.toString();

                if (!_password_confirm.isEmpty()) {
                    if (password_note.getVisibility() == View.GONE) {
                        password_note.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 0;
                        password_confirm.setLayoutParams(params);
                    }

                    if (_password.isEmpty() || !_password.equals(_password_confirm)) {
                        password_note.setTextColor(getColor(R.color.red));
                        is_same = false;

                        if (_password.isEmpty()) {
                            password_note.setText("✕ 사용할 비밀번호를 먼저 입력해주세요.");
                        } else {
                            password_note.setText("✕ 비밀번호가 일치하지 않습니다.");
                        }
                    } else {
                        password_note.setTextColor(getColor(R.color.green));
                        password_note.setText("✓ 비밀번호가 일치합니다.");
                        is_same = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean name_empty = name.getText().toString().isEmpty();
                boolean username_empty = username.getText().toString().isEmpty();
                boolean password_empty = password.getText().toString().isEmpty();
                boolean password_confirm_empty = password_confirm.getText().toString().isEmpty();
                boolean contact_number_empty = contact_number.getText().toString().isEmpty();
                boolean email_empty = email.getText().toString().isEmpty();
                boolean company_empty = company.getText().toString().isEmpty();

                if (name_empty || username_empty || password_empty || password_confirm_empty || contact_number_empty || email_empty || company_empty) {
                    Toast.makeText(getApplicationContext(), "비어있는 내용을 채우고 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

                    if (name_empty) {
                        name.requestFocus();
                    } else if (username_empty) {
                        username.requestFocus();
                    } else if (password_empty) {
                        password.requestFocus();
                    } else if (password_confirm_empty) {
                        password_confirm.requestFocus();
                    } else if (contact_number_empty) {
                        contact_number.requestFocus();
                    } else if (email_empty) {
                        email.requestFocus();
                    } else {
                        company.requestFocus();
                    }
                } else if (!is_unique) {
                    Toast.makeText(getApplicationContext(), "아이디를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                } else if (!is_same) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    password_confirm.requestFocus();
                } else {
                    new DBVolley(getApplicationContext(), "Submit", new HashMap<String, Object>() {{
                        put("name", name);
                        put("username", username);
                        put("password", password);
                        put("contact_number", contact_number);
                        put("email", email);
                        put("company", company);
                    }}, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "입력된 내용이 정확한지 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
