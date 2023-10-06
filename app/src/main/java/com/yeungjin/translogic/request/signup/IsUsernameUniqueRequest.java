package com.yeungjin.translogic.request.signup;

import android.widget.EditText;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class IsUsernameUniqueRequest extends Request {
    public IsUsernameUniqueRequest(EditText username, Response.Listener<String> listener) {
        super("IsUsernameUnique", listener);

        parameters.put("username", username.getText().toString());
    }
}
