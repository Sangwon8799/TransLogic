package com.yeungjin.translogic.request.employee;

import com.android.volley.Response;
import com.yeungjin.translogic.request.Request;

public class GetEmployeeRequest extends Request {
    public GetEmployeeRequest(int index, Response.Listener<String> listener) {
        super("GetEmployee.db", listener);

        parameters.put("index", String.valueOf(index));
    }
}
