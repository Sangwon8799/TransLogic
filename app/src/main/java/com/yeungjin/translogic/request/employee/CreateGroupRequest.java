package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class CreateGroupRequest extends Request {
    public CreateGroupRequest(String name, long number, Response.Listener<String> listener) {
        super("CreateGroup", listener);

        parameters.put("name", name);
        parameters.put("number", String.valueOf(number));
    }
}
