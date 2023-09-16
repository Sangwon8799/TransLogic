package com.yeungjin.translogic.request.login;

import android.widget.EditText;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class LoginRequest extends Request {
    public LoginRequest(EditText username, EditText password, Response.Listener<String> listener) {
        super("Login.db", listener);

        parameters.put("username", username.getText().toString());
        parameters.put("password", password.getText().toString());
    }
}
