package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class IsGroupMaxRequest extends Request {
    public IsGroupMaxRequest(long employee_number, Response.Listener<String> listener) {
        super("IsGroupMax", listener);

        parameters.put("employee_number", String.valueOf(employee_number));
    }
}
