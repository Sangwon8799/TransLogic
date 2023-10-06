package com.yeungjin.translogic.request.signup;

import android.widget.EditText;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class SubmitRequest extends Request {
    public SubmitRequest(EditText name, EditText username, EditText password, EditText contact_number, EditText email, EditText company, Response.Listener<String> listener) {
        super("Signup", listener);

        parameters.put("name", name.getText().toString());
        parameters.put("username", username.getText().toString());
        parameters.put("password", password.getText().toString());
        parameters.put("contact_number", contact_number.getText().toString());
        parameters.put("email", email.getText().toString());
        parameters.put("company", company.getText().toString());
    }
}
