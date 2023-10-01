package com.yeungjin.translogic.layout.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.signup.IsUniqueRequest;
import com.yeungjin.translogic.request.signup.SubmitRequest;

public class SignupLayout extends AppCompatActivity {
    private ScrollView information;
    private EditText name;
    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    private EditText contactNumber;
    private EditText email;
    private EditText company;
    private TextView usernameNote;
    private TextView passwordNote;
    private Button submit;
    private Button cancel;

    private boolean isUnique;
    private boolean isSame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_signup);
        init();

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !username.getText().toString().isEmpty()) {
                    if (usernameNote.getVisibility() == View.GONE) {
                        usernameNote.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 0;
                        username.setLayoutParams(params);
                    }

                    IsUniqueRequest request = new IsUniqueRequest(username, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                usernameNote.setTextColor(getColor(R.color.green));
                                usernameNote.setText("✓ 사용 가능한 아이디입니다.");
                                isUnique = true;
                            } else {
                                usernameNote.setTextColor(getColor(R.color.red));
                                usernameNote.setText("✕ 이미 사용중인 아이디입니다.");
                                isUnique = false;
                            }
                        }
                    });
                    Request.queue = Volley.newRequestQueue(getApplicationContext());
                    Request.queue.add(request);
                }
            }
        });

        passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence content, int start, int before, int count) {
                String _password = password.getText().toString();
                String _passwordConfirm = content.toString();

                if (!_passwordConfirm.isEmpty()) {
                    if (passwordNote.getVisibility() == View.GONE) {
                        passwordNote.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 0;
                        passwordConfirm.setLayoutParams(params);
                    }

                    if (_password.isEmpty() || !_password.equals(_passwordConfirm)) {
                        passwordNote.setTextColor(getColor(R.color.red));
                        isSame = false;

                        if (_password.isEmpty()) {
                            passwordNote.setText("✕ 사용할 비밀번호를 먼저 입력해주세요.");
                        } else {
                            passwordNote.setText("✕ 비밀번호가 일치하지 않습니다.");
                        }
                    } else {
                        passwordNote.setTextColor(getColor(R.color.green));
                        passwordNote.setText("✓ 비밀번호가 일치합니다.");
                        isSame = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nameEmpty = name.getText().toString().isEmpty();
                boolean usernameEmpty = username.getText().toString().isEmpty();
                boolean passwordEmpty = password.getText().toString().isEmpty();
                boolean passwordConfirmEmpty = passwordConfirm.getText().toString().isEmpty();
                boolean contactNumberEmpty = contactNumber.getText().toString().isEmpty();
                boolean emailEmpty = email.getText().toString().isEmpty();
                boolean companyEmpty = company.getText().toString().isEmpty();

                if (nameEmpty || usernameEmpty || passwordEmpty || passwordConfirmEmpty || contactNumberEmpty || emailEmpty || companyEmpty) {
                    Toast.makeText(getApplicationContext(), "비어있는 내용을 채우고 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

                    if (nameEmpty) {
                        name.requestFocus();
                    } else if (usernameEmpty) {
                        username.requestFocus();
                    } else if (passwordEmpty) {
                        password.requestFocus();
                    } else if (passwordConfirmEmpty) {
                        passwordConfirm.requestFocus();
                    } else if (contactNumberEmpty) {
                        contactNumber.requestFocus();
                    } else if (emailEmpty) {
                        email.requestFocus();
                    } else {
                        company.requestFocus();
                    }
                } else if (!isUnique) {
                    Toast.makeText(getApplicationContext(), "아이디를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                } else if (!isSame) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    passwordConfirm.requestFocus();
                } else {
                    SubmitRequest request = new SubmitRequest(name, username, password, contactNumber, email, company, new Response.Listener<String>() {
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
                    Request.queue = Volley.newRequestQueue(getApplicationContext());
                    Request.queue.add(request);
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

    private void init() {
        information = (ScrollView) findViewById(R.id.layout_signup_signup__information);
        name = (EditText) findViewById(R.id.layout_signup_signup__name);
        username = (EditText) findViewById(R.id.layout_signup_signup__username);
        password = (EditText) findViewById(R.id.layout_signup_signup__password);
        passwordConfirm = (EditText) findViewById(R.id.layout_signup_signup__password_confirm);
        contactNumber = (EditText) findViewById(R.id.layout_signup_signup__contact_number);
        email = (EditText) findViewById(R.id.layout_signup_signup__email);
        company = (EditText) findViewById(R.id.layout_signup_signup__company);
        usernameNote = (TextView) findViewById(R.id.layout_signup_signup__username_note);
        passwordNote = (TextView) findViewById(R.id.layout_signup_signup__password_note);
        submit = (Button) findViewById(R.id.layout_signup_signup__submit);
        cancel = (Button) findViewById(R.id.layout_signup_signup__cancel);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                metrics.heightPixels * 5 / 10);
        params.addRule(RelativeLayout.BELOW, R.id.layout_signup_signup__TITLE);

        information.setLayoutParams(params);
    }
}
