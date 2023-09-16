package com.yeungjin.translogic.request.signup;

import android.widget.EditText;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class CheckUniqueRequest extends Request {
    public CheckUniqueRequest(EditText username, Response.Listener<String> listener) {
        super("CheckUnique.db", listener);

        parameters.put("username", username.getText().toString());
    }
}
