package com.yeungjin.translogic.request.login;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class LoginRequest extends Request {
    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super("Login", listener);

        parameters.put("username", username);
        parameters.put("password", password);
    }
}
