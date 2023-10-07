package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class CreateGroupRequest extends Request {
    public CreateGroupRequest(String name, long employee_number, Response.Listener<String> listener) {
        super("CreateGroup", listener);

        parameters.put("name", name);
        parameters.put("employee_number", String.valueOf(employee_number));
    }
}
