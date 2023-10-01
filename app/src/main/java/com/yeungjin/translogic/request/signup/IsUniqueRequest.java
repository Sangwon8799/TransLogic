package com.yeungjin.translogic.request.signup;

import android.widget.EditText;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class IsUniqueRequest extends Request {
    public IsUniqueRequest(EditText username, Response.Listener<String> listener) {
        super("IsUnique.db", listener);

        parameters.put("username", username.getText().toString());
    }
}
